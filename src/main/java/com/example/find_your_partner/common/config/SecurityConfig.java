package com.example.find_your_partner.common.config;

import com.example.find_your_partner.auth.jwt.JwtAuthenticationEntryPoint;
import com.example.find_your_partner.auth.jwt.JwtAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint){

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {

        httpSecurity
                        .csrf(csrf -> csrf.disable())

                        .sessionManagement(session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                        )

                        .exceptionHandling(exception ->
                                exception.authenticationEntryPoint(
                                        jwtAuthenticationEntryPoint
                                )
                        )

                        .authorizeHttpRequests(auth -> auth

                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll() //---
                            .requestMatchers(
                                    "/api/v1/auth/register",
                                            "/api/v1/auth/login",
                                            "/actuator/health",
                                            "/actuator/info"
                            ).permitAll()

                            .anyRequest().authenticated()
                        )

                        .addFilterBefore(
                                jwtAuthenticationFilter,
                                UsernamePasswordAuthenticationFilter.class
                        );

        return httpSecurity.build();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter
    ) {

        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>(filter);

        registration.setEnabled(false);
        return registration;
    }
}
