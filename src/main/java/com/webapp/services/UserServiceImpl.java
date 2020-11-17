package com.webapp.services;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import com.webapp.jwt.AwsCognitoIdTokenProcessor;
import com.webapp.models.ResetPasswordRequest;
import com.webapp.models.User;
import com.webapp.models.UserLoginRequestObject;
import com.webapp.models.UserTokens;
import com.webapp.repository.UserTokensRepository;

import io.netty.util.internal.StringUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    private final AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;

    private final ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

    private final UserTokensRepository repository;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.cognito.clientSecret}")
    private String clientSecret;

    public UserServiceImpl(AWSCognitoIdentityProvider cognitoIdentityProvider,
                           AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor,
                           ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor,
                           UserTokensRepository repository) {

        this.cognitoIdentityProvider = cognitoIdentityProvider;
        this.awsCognitoIdTokenProcessor = awsCognitoIdTokenProcessor;
        this.configurableJWTProcessor = configurableJWTProcessor;
        this.repository = repository;
    }

    @Override
    public String createUser(User user) throws UsernameExistsException {
        String cognitoUser;
        SignUpResult createUserResult;
        createUserResult = cognitoIdentityProvider.signUp(createSignUpRequest(user));
        cognitoUser = createUserResult.getUserSub();
        return cognitoUser;
    }

    private SignUpRequest createSignUpRequest(User user) {
        return new SignUpRequest()
          .withClientId(clientId)
          .withSecretHash(calculateSecretHash(user.getEmail()))
          .withUsername(user.getEmail())
          .withPassword(user.getPassword())
          .withUserAttributes(
            new AttributeType()
              .withName("email")
              .withValue(user.getEmail()),
            new AttributeType()
              .withName("name")
              .withValue(user.getName())
          );
    }

    public String calculateSecretHash(String userName) {
        if (clientSecret == null) {
            return null;
        }

        SecretKeySpec signingKey = new SecretKeySpec(
          clientSecret.getBytes(StandardCharsets.UTF_8),
          HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(clientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Error while calculating ");
        }
    }

    @Override
    public String loginUser(UserLoginRequestObject user, HttpServletResponse response) throws NotAuthorizedException, UserNotConfirmedException, ParseException, JOSEException, BadJOSEException {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", user.getEmail());
        authParams.put("PASSWORD", user.getPassword());
        authParams.put("SECRET_HASH", calculateSecretHash(user.getEmail()));
        InitiateAuthResult initiateAuthResult = cognitoIdentityProvider
          .initiateAuth(createInitiateAuthRequest(authParams, AuthFlowType.USER_PASSWORD_AUTH));
        if (StringUtil.isNullOrEmpty(initiateAuthResult.getChallengeName())) {
            String idToken = initiateAuthResult.getAuthenticationResult().getIdToken();

            JWTClaimsSet claims = this.configurableJWTProcessor.process(idToken, null);
            String userName = awsCognitoIdTokenProcessor.getUserNameFrom(claims);

            repository.save(new UserTokens(userName
              , idToken
              , initiateAuthResult.getAuthenticationResult().getAccessToken()
              , initiateAuthResult.getAuthenticationResult().getRefreshToken()));

            Cookie cookie = new Cookie("auth_token", idToken);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
            response.addCookie(cookie);

            return "Successfully Logged in, Show Dashboard " + userName;
        }
        return null;
    }

    @Override
    public String generateNewTokens(String userName, String lastIdToken) throws NotAuthorizedException, UserNotConfirmedException {
        Map<String, String> authParams = new HashMap<>();
//        TODO: add multiple device functionality
        Optional<UserTokens> userTokens = repository.findById(userName);
        if (userTokens.isEmpty() /*|| !userTokens.get().getIdToken().equals(lastIdToken)*/)
            return null;

        authParams.put("REFRESH_TOKEN", userTokens.get().getRefreshToken());
        authParams.put("SECRET_HASH", calculateSecretHash(userName));
        InitiateAuthResult initiateAuthResult = cognitoIdentityProvider
          .initiateAuth(createInitiateAuthRequest(authParams, AuthFlowType.REFRESH_TOKEN));

        if (StringUtil.isNullOrEmpty(initiateAuthResult.getChallengeName())) {
            String idToken = initiateAuthResult.getAuthenticationResult().getIdToken();
            userTokens.get().setAccessToken(initiateAuthResult.getAuthenticationResult().getAccessToken());
            repository.save(userTokens.get());
            return idToken;
        }

        return null;
    }

    private InitiateAuthRequest createInitiateAuthRequest(Map<String, String> authParams, AuthFlowType authFlowType) {
        return new InitiateAuthRequest().withClientId(clientId)
          .withAuthFlow(authFlowType)
          .withAuthParameters(authParams);
    }

    public void sendCodeForgotPassword(String username) {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
          .withClientId(clientId)
          .withSecretHash(calculateSecretHash(username))
          .withUsername(username);

        cognitoIdentityProvider.forgotPassword(forgotPasswordRequest);
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ConfirmForgotPasswordRequest request = new ConfirmForgotPasswordRequest()
          .withClientId(clientId)
          .withUsername(resetPasswordRequest.getUsername())
          .withPassword(resetPasswordRequest.getPassword())
          .withConfirmationCode(resetPasswordRequest.getCode())
          .withSecretHash(calculateSecretHash(resetPasswordRequest.getUsername()));

        cognitoIdentityProvider.confirmForgotPassword(request);
    }


}
