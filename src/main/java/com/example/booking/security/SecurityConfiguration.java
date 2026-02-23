package com.example.booking.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthenticationProvider authenticationProvider;

    @Autowired
    private Environment environment;


    @Bean
    SecurityFilterChain configure(HttpSecurity http) {
        if (environment.acceptsProfiles(Profiles.of("prod"))) {
            http.redirectToHttps(Customizer.withDefaults());
        }
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(10)); //gestione sessione
        http.csrf(AbstractHttpConfigurer::disable); //per adesso lo ignoriamo
        http.httpBasic(Customizer.withDefaults());
        http.formLogin(form -> form.defaultSuccessUrl("/username", true));
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

    /*
    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                System.out.println("Timeout reale: " + se.getSession().getMaxInactiveInterval() + " sec");
            }
        });
    }
    */
}
