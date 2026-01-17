package com.birthday.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // disable csrf for the rest api
                                .csrf(csrf -> csrf.disable())
                                // enable cors with our config
                                .cors(Customizer.withDefaults())

                                // config who can access what - letting everything through for now
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/actuator/health", "/actuator/info").permitAll() // health
                                                                                                                   // check
                                                                                                                   // endpoints
                                                .anyRequest().permitAll() // just allow it all
                                )

                                // some extra security headers
                                .headers(headers -> headers
                                                // stop clickjacking
                                                .frameOptions(frame -> frame.deny())
                                                // stop mime-type sniffing
                                                .contentTypeOptions(contentType -> {
                                                })
                                                // xss protection
                                                .xssProtection(xss -> xss
                                                                .headerValue(
                                                                                org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                                // referrer policy
                                                .referrerPolicy(referrer -> referrer
                                                                .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                                                // tell the browser what features we use
                                                .permissionsPolicy(permissions -> permissions
                                                                .policy("geolocation=(), microphone=(), camera=()")));

                return http.build();
        }
}
