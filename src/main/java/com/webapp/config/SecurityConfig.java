package com.webapp.config;

import com.webapp.jwt.AwsCognitoJwtAuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
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

        http.headers().cacheControl();

        http
          .csrf().disable()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .authorizeRequests()
          .antMatchers(HttpMethod.GET, "/cities/**").permitAll()
          .antMatchers(HttpMethod.GET, "/movies/**").permitAll()
          .antMatchers(HttpMethod.GET, "/theaters/**").permitAll()
          .antMatchers(HttpMethod.GET, "/v1/screens/**").permitAll()
          .antMatchers(HttpMethod.GET, "/v1/seats/**").permitAll()
          .antMatchers("/signUp", "/login", "/forgot", "/reset", "/forgotPassword").permitAll()
          .antMatchers("/v1/bookings/**", "/changePassword", "/logoutUser","/v1/users/**").authenticated()
          .anyRequest().hasRole("ADMIN")
          .and()
          .addFilterBefore(awsCognitoJwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
          .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login.html"));

    }
}
