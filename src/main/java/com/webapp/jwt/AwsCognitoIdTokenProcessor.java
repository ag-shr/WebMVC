package com.webapp.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import com.webapp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

@Component
public class AwsCognitoIdTokenProcessor {

    private final JwtConfiguration jwtConfiguration;

    @Autowired
    private UserService userService;

    private final String tokenName = "auth_token";

    private final ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

    public AwsCognitoIdTokenProcessor(JwtConfiguration jwtConfiguration, ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor) {
        this.jwtConfiguration = jwtConfiguration;
        this.configurableJWTProcessor = configurableJWTProcessor;
    }

    public Authentication authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request == null || request.getCookies() == null) {
            return null;
        }
        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
          .filter(c -> c.getName().equals(tokenName))
          .findFirst();

        if (cookie.isPresent()) {
            JWTClaimsSet claims;
            try {
                claims = getJwtClaimsSet(cookie.get().getValue());
            } catch (BadJOSEException e) {
                if (e.getMessage().equals("Expired JWT")) {
                    System.out.println("Expired");
                    claims = handleExpiredToken(response, cookie.get());
                    if (claims == null)
                        return null;
                } else
                    throw new BadJOSEException(e.getMessage());
            }
            validateIssuer(claims);
            verifyIfIdToken(claims);
            String username = getUserNameFrom(claims);
            if (username != null) {
                List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
                User user = new User(username, "", List.of());
                return new JwtAuthentication(user, claims, grantedAuthorities);
            }
        }
        return null;
    }

    private JWTClaimsSet getJwtClaimsSet(String jwt) throws java.text.ParseException, BadJOSEException,
      com.nimbusds.jose.JOSEException {
        JWTClaimsSet claims;
        claims = this.configurableJWTProcessor.process(jwt, null);
        return claims;
    }

    private JWTClaimsSet handleExpiredToken(HttpServletResponse response, Cookie cookie) throws Exception {
        String token = new String(Base64.getDecoder().decode(cookie.getValue().split("\\.")[1]));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> claimSet = new HashMap<>();

        try {
            claimSet = mapper.readValue(token, new TypeReference<>() {});
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        String username = (String) claimSet.get("cognito:username");
        String jwt = userService.generateNewTokens(username, cookie.getValue());

        if (jwt == null)
            return null;

        cookie.setValue(jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return getJwtClaimsSet(jwt);
    }

    public String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
    }

    private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(),
              this.jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

}