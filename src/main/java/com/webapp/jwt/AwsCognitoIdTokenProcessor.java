package com.webapp.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AwsCognitoIdTokenProcessor {


    private final JwtConfiguration jwtConfiguration;
    private final String tokenName ="auth_token";
    private final ConfigurableJWTProcessor configurableJWTProcessor;

    public AwsCognitoIdTokenProcessor(JwtConfiguration jwtConfiguration, ConfigurableJWTProcessor configurableJWTProcessor) {
        this.jwtConfiguration = jwtConfiguration;
        this.configurableJWTProcessor = configurableJWTProcessor;
    }

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
          .filter(c -> c.getName().equals(tokenName))
          .findFirst();
//        String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
        if (cookie.isPresent()) {
            JWTClaimsSet claims = this.configurableJWTProcessor.process(cookie.get().getValue(),null);
            validateIssuer(claims);
            verifyIfIdToken(claims);
            String username = getUserNameFrom(claims);
            if (username != null) {
                List<GrantedAuthority> grantedAuthorities = List.of( new SimpleGrantedAuthority("ROLE_ADMIN"));
                User user = new User(username, "", List.of());
                return new JwtAuthentication(user, claims, grantedAuthorities);
            }
        }
        return null;
    }

    private String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
    }

    private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), this.jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

}