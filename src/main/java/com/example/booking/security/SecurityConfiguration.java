package com.example.booking.security;

import com.example.booking.security.filter.JwtTokenGeneratorFilter;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthenticationProvider authenticationProvider;
    private final JwtTokenGeneratorFilter tokenGeneratorFilter;
    private final JwtTokenValidatorFilter tokenValidatorFilter;

    @Autowired
    private Environment environment;


    @Bean
    SecurityFilterChain configure(HttpSecurity http) {
        if (environment.acceptsProfiles(Profiles.of("prod"))) {
            http.redirectToHttps(Customizer.withDefaults());
        }
        http.sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //gestione sessione
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults()); //TODO -> da togliere se si vuole usare solo jwt (da fare api però)
        http.addFilterAfter(tokenGeneratorFilter, BasicAuthenticationFilter.class);
        http.addFilterBefore(tokenValidatorFilter, BasicAuthenticationFilter.class);
        http.formLogin(form -> form.defaultSuccessUrl("/username", true));
        http.authenticationProvider(authenticationProvider);
        http.authorizeHttpRequests(
                c ->
                        c.requestMatchers("/info", "/error", "/booking/login").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                //.requestMatchers("/booking/**").hasRole("HOTEL_OWNER") //a parte booking/login
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
