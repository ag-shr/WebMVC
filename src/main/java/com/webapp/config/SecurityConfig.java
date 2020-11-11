package com.webapp.config;

import com.webapp.jwt.AwsCognitoJwtAuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter;

    public SecurityConfig(AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter) {
        this.awsCognitoJwtAuthFilter = awsCognitoJwtAuthFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().cacheControl();

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .antMatchers("/v1/**").authenticated()
                .anyRequest().permitAll().and()
                .addFilterBefore(awsCognitoJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
