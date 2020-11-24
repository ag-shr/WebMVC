package com.webapp.services;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.webapp.config.CognitoConfiguration;
import com.webapp.exception.MovieBookingWebAppException;
import com.webapp.exception.UnauthorizedException;
import com.webapp.jwt.AwsCognitoIdTokenProcessor;
import com.webapp.jwt.JwtConstants;
import com.webapp.models.*;
import com.webapp.repository.UserTokensRepository;
import io.netty.util.internal.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class CognitoUserServiceImpl implements CognitoUserService {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    private final AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;

    private final ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

    private final UserTokensRepository repository;

    private final CognitoConfiguration cognitoConfiguration;

    private final UserService userService;

    public CognitoUserServiceImpl(AWSCognitoIdentityProvider cognitoIdentityProvider,
                                  AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor,
                                  ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor,
                                  UserTokensRepository repository,
                                  CognitoConfiguration cognitoConfiguration, UserService userService) {

        this.cognitoIdentityProvider = cognitoIdentityProvider;
        this.awsCognitoIdTokenProcessor = awsCognitoIdTokenProcessor;
        this.configurableJWTProcessor = configurableJWTProcessor;
        this.repository = repository;
        this.cognitoConfiguration =cognitoConfiguration;
        this.userService = userService;
    }

    @Override
    public UserDetails createUser(User user) throws UsernameExistsException {
        String cognitoUser;
        SignUpResult createUserResult;
        createUserResult = cognitoIdentityProvider.signUp(createSignUpRequest(user));
        cognitoUser = createUserResult.getUserSub();

        UserDetails userDetails = new UserDetails(user.getName(),user.getEmail(),user.getPhoneNumber(),user.getDateOfBirth());
        return userService.addUserDetails(userDetails);
    }

    private SignUpRequest createSignUpRequest(User user) {
        return new SignUpRequest()
          .withClientId(cognitoConfiguration.getClientId())
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
        if (cognitoConfiguration.getClientSecret() == null) {
            return null;
        }

        SecretKeySpec signingKey = new SecretKeySpec(
          cognitoConfiguration.getClientSecret().getBytes(StandardCharsets.UTF_8),
          HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(cognitoConfiguration.getClientId().getBytes(StandardCharsets.UTF_8));
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
        return new InitiateAuthRequest().withClientId(cognitoConfiguration.getClientId())
          .withAuthFlow(authFlowType)
          .withAuthParameters(authParams);
    }

    private ForgotPasswordRequest sendCodeForgotPassword(String username) {
        return new ForgotPasswordRequest()
          .withClientId(cognitoConfiguration.getClientId())
          .withSecretHash(calculateSecretHash(username))
          .withUsername(username);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ConfirmForgotPasswordRequest request = new ConfirmForgotPasswordRequest()
          .withClientId(cognitoConfiguration.getClientId())
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
        try {
            if (this.isEmailVerified(userEmail))
                cognitoIdentityProvider.forgotPassword(sendCodeForgotPassword(userEmail));
            else{
                cognitoIdentityProvider.resendConfirmationCode(createResendConfirmationCodeRequest(userEmail));
                return "Email Not Verified";
            }
            return "resetPassword";
        } catch (AWSCognitoIdentityProviderException e) {
            throw new MovieBookingWebAppException(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResendConfirmationCodeRequest createResendConfirmationCodeRequest(String userName) {
        return new ResendConfirmationCodeRequest().withClientId(cognitoConfiguration.getClientId()).withSecretHash(calculateSecretHash(userName))
          .withUsername(userName);
    }

    @Override
    public Boolean changePassword(UserChangePasswordRequest changePasswordRequest) {
        String userName = this.getUserNameFromSecurityContext();
        String accessToken = repository.findById(userName)
          .orElseThrow(() -> new UnauthorizedException("Unauthorized"))
          .getAccessToken();
        cognitoIdentityProvider.changePassword(createChangePasswordRequest(accessToken, changePasswordRequest));
        return true;
    }

    private boolean isEmailVerified(String email) {
        AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest()
          .withUserPoolId(cognitoConfiguration.getUserPoolId())
          .withUsername(email);
        AdminGetUserResult adminGetUserResult = cognitoIdentityProvider
          .adminGetUser(adminGetUserRequest);
        boolean isEmailVerified = false;
        for (AttributeType attr : adminGetUserResult.getUserAttributes())
            if (JwtConstants.EMAIL_VERIFIED.equals(attr.getName()))
                isEmailVerified = Boolean.parseBoolean(attr.getValue());
        return isEmailVerified;
    }

    private String getUserNameFromSecurityContext() {
        return ((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    private ChangePasswordRequest createChangePasswordRequest(String accessToken, UserChangePasswordRequest changePasswordRequest) {
        return new ChangePasswordRequest()
          .withAccessToken(accessToken)
          .withPreviousPassword(changePasswordRequest.getPrevPassword())
          .withProposedPassword(changePasswordRequest.getProposedPassword());
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
