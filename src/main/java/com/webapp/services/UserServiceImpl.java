package com.webapp.services;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.webapp.jwt.AwsCognitoIdTokenProcessor;
import com.webapp.models.User;
import com.webapp.models.UserLoginRequestObject;
import com.webapp.models.UserTokens;
import com.webapp.repository.UserTokensRepository;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AWSCognitoIdentityProvider cognitoIdentityProvider;

    @Autowired
    private AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;

    @Autowired
    private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

    @Autowired
    private UserTokensRepository repository;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.cognito.clientSecret}")
    private String clientSecret;

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
            cookie.setPath("localhost:9090");
            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
            response.addCookie(cookie);

            return "Successfully Logged in, Show Dashboard " + userName;
        }
        return null;
    }

    @Override
    public String generateNewTokens(String userName, HttpServletResponse response) throws NotAuthorizedException, UserNotConfirmedException {
        Map<String, String> authParams = new HashMap<String, String>();
//        TODO: also take previous id token, to check whether the expired token is the last token stored in db.
        Optional<UserTokens> userTokens = repository.findById(userName);
        if (userTokens.isEmpty())
            return null;

        authParams.put("REFRESH_TOKEN", userTokens.get().getRefreshToken());
        authParams.put("SECRET_HASH", calculateSecretHash(userName));
        InitiateAuthResult initiateAuthResult = cognitoIdentityProvider
                .initiateAuth(createInitiateAuthRequest(authParams, AuthFlowType.REFRESH_TOKEN));

        if (StringUtil.isNullOrEmpty(initiateAuthResult.getChallengeName())) {
            String idToken = initiateAuthResult.getAuthenticationResult().getIdToken();
            Cookie cookie = new Cookie("auth_token", idToken);
            cookie.setPath("localhost:9090");
            cookie.setHttpOnly(true);
            userTokens.get().setAccessToken(initiateAuthResult.getAuthenticationResult().getAccessToken());
            repository.save(userTokens.get());
            response.addCookie(cookie);
            return idToken;
        }

        return "Login Again";
    }

    private InitiateAuthRequest createInitiateAuthRequest(Map<String, String> authParams, AuthFlowType authFlowType) {
        return new InitiateAuthRequest().withClientId(clientId)
                .withAuthFlow(authFlowType)
                .withAuthParameters(authParams);
    }

}
