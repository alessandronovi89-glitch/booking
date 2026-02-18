package com.example.booking.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain configure(HttpSecurity http) {
        //abilita l'autenticazione di base HTTP
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable); //per adesso lo ignoriamo
        http.formLogin(flc->flc.disable()); //disabilitiamo il form login di default
        //uso l'authenticazione personalizzata (gestisco cosa fare)
        http.authenticationProvider(authenticationProvider);
        http.authorizeHttpRequests(
                //TODO info public endpoint
                c -> c.requestMatchers("/info", "/error").permitAll()
                        .anyRequest().authenticated() //permitAll..
        );

        return http.build();
    }


}
