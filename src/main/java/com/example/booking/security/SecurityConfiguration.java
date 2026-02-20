package com.example.booking.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

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
                c ->
                        c.requestMatchers("/info", "/error").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                .requestMatchers("/hotel/**").hasRole("HOTEL_OWNER")
                                .anyRequest().authenticated()
        );

        return http.build();
    }


}
