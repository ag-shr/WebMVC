package com.webapp.services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.webapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.webapp.models.UserLoginRequestObject;

import io.netty.util.internal.StringUtil;

@Service
public class UserServiceImpl implements UserService {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.cognito.clientSecret}")
    private String clientSecret;

    public UserServiceImpl(AWSCognitoIdentityProvider cognitoIdentityProvider) {
        this.cognitoIdentityProvider = cognitoIdentityProvider;
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
    public String loginUser(UserLoginRequestObject user) throws NotAuthorizedException, UserNotConfirmedException {
        Map<String, String> authParams = new HashMap<String, String>();
        authParams.put("USERNAME", user.getEmail());
        authParams.put("PASSWORD", user.getPassword());
        authParams.put("SECRET_HASH", calculateSecretHash(user.getEmail()));
        InitiateAuthResult initiateAuthResult = cognitoIdentityProvider.initiateAuth(createInitiateAuthRequest(authParams));

        if (StringUtil.isNullOrEmpty(initiateAuthResult.getChallengeName())) {
            return initiateAuthResult.getAuthenticationResult().getIdToken() + "\n\n" +
              initiateAuthResult.getAuthenticationResult().getAccessToken() + "\n\n" +
              initiateAuthResult.getAuthenticationResult().getRefreshToken();
        }

        return null;
    }

    private InitiateAuthRequest createInitiateAuthRequest(Map<String, String> authParams) {
        return new InitiateAuthRequest().withClientId(clientId)
          .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
          .withAuthParameters(authParams);
    }

}
