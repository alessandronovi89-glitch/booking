package com.example.booking.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain configure(HttpSecurity http) {
        //abilita l'autenticazione di base HTTP
        http.httpBasic(Customizer.withDefaults());
        //uso l'authenticazione personalizzata (gestisco cosa fare)
        http.authenticationProvider(authenticationProvider);
        http.authorizeHttpRequests(
                //TODO info public endpoint
                c -> c.requestMatchers("/info").permitAll()
                        .anyRequest().authenticated() //permitAll..
        );

        return http.build();
    }
}
