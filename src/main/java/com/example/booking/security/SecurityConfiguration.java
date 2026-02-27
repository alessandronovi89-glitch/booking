package com.example.booking.security;

import com.example.booking.security.filter.CsrfCookieFilter;
import com.example.booking.security.filter.LoggingFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

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
        //http.securityContext(contextConfig->contextConfig.requireExplicitSave(false));
        http.sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .invalidSessionUrl("/invalidSession").maximumSessions(10)); //gestione sessione
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.csrf(crsfConfig -> crsfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())); //ignoringRequestMatchers("/booking/**")...
        http.httpBasic(Customizer.withDefaults());
        http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new LoggingFilter(), CsrfCookieFilter.class);
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
