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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests(
                c ->
                        c.requestMatchers("/info", "/error", "/auth/login", "/auth/refresh-token").permitAll()
                                // prima la più specifica
                                .requestMatchers("/room/view/**").hasAnyRole("USER", "HOTEL_OWNER")
                                // poi la più generale
                                .requestMatchers("/user/**").hasRole("ADMIN") //vedere lista utenti e aggiungere, togliere ..
                                .requestMatchers("/room/**").hasRole("HOTEL_OWNER") //aggiungere togliere stanza ..
                                .requestMatchers("/booking/**").hasRole("USER")
                                .anyRequest().authenticated()
        );
        return http.build();
    }

    //(va bene per develop)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of("*")); // tutti gli origin
        config.setAllowedMethods(List.of("*"));        // GET, POST, ecc.
        config.setAllowedHeaders(List.of("*"));        // tutti gli header
        config.setAllowCredentials(false);             // true solo se usi cookie/sessioni (niente cookie, niente sessoini..)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
