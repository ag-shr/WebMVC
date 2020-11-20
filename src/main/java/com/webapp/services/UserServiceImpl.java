package com.webapp.services;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.webapp.exception.MovieBookingWebAppException;
import com.webapp.jwt.AwsCognitoIdTokenProcessor;
import com.webapp.models.ResetPasswordRequest;
import com.webapp.models.User;
import com.webapp.models.UserLoginRequestObject;
import com.webapp.models.UserTokens;
import com.webapp.repository.UserTokensRepository;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
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

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

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
//        TODO: add multiple device functionality, then globalSignOut
        Optional<UserTokens> userTokens = repository.findById(userName);
        if (userTokens.isEmpty() || !userTokens.get().getIdToken().equals(lastIdToken))
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

    private ForgotPasswordRequest sendCodeForgotPassword(String username) {
        return new ForgotPasswordRequest()
          .withClientId(clientId)
          .withSecretHash(calculateSecretHash(username))
          .withUsername(username);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ConfirmForgotPasswordRequest request = new ConfirmForgotPasswordRequest()
          .withClientId(clientId)
          .withUsername(resetPasswordRequest.getUsername())
          .withPassword(resetPasswordRequest.getPassword())
          .withConfirmationCode(resetPasswordRequest.getCode())
          .withSecretHash(calculateSecretHash(resetPasswordRequest.getUsername()));
        try {
            cognitoIdentityProvider.confirmForgotPassword(request);
        } catch (AWSCognitoIdentityProviderException e) {
            throw new MovieBookingWebAppException(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String forgotPassword(String userEmail) throws MovieBookingWebAppException {
        AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest()
          .withUserPoolId(userPoolId)
          .withUsername(userEmail);
        try {
            var adminGetUserResult = cognitoIdentityProvider.adminGetUser(adminGetUserRequest);
//            adminGetUserResult.getUserAttributes()
//                    .stream().filter(attributeType -> attributeType.getName().equals("email_verified"))
//                    .findFirst().get();
            if (adminGetUserResult.getUserStatus().equals("CONFIRMED"))
                cognitoIdentityProvider.forgotPassword(sendCodeForgotPassword(userEmail));
            else if (adminGetUserResult.getUserStatus().equals("UNCONFIRMED")) {
                cognitoIdentityProvider.resendConfirmationCode(createResendConfirmationCodeRequest(userEmail));
                return "Your account is not verified yet. Please check your registered mail and click on the provided link to verify your account";
            } else
                throw new MovieBookingWebAppException("Invalid username or password", HttpStatus.BAD_REQUEST);
            return "resetPassword";
        } catch (AWSCognitoIdentityProviderException e) {
            throw new MovieBookingWebAppException(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResendConfirmationCodeRequest createResendConfirmationCodeRequest(String userName) {
        return new ResendConfirmationCodeRequest().withClientId(clientId).withSecretHash(calculateSecretHash(userName))
          .withUsername(userName);
    }

    @Override
    public void logout(Principal principal, HttpServletResponse response) {
        repository.deleteById(principal.getName());
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

}
