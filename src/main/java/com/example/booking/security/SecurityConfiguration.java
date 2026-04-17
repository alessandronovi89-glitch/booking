package com.example.booking.security;

import com.example.booking.security.filter.JwtTokenValidatorFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenValidatorFilter tokenValidatorFilter;

    @Autowired
    private Environment environment;


    @Bean
    SecurityFilterChain configure(HttpSecurity http) {
        if (environment.acceptsProfiles(Profiles.of("prod"))) {
            http.redirectToHttps(Customizer.withDefaults());
        }
        http.sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(tokenValidatorFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(
                c ->
                        c.requestMatchers("/info", "/error", "/auth/login").permitAll()
                                .requestMatchers("/user/**").hasRole("ADMIN") //vedere lista utenti e aggiungere, togliere ..
                                .requestMatchers("/room/**").hasRole("HOTEL_OWNER") //aggiungere togliere stanza ..
                                .requestMatchers("/booking/**").hasRole("USER")
                                .anyRequest().authenticated()
        );
        return http.build();
    }
}
