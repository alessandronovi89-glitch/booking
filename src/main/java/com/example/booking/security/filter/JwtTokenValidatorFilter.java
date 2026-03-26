package com.example.booking.security.filter;

import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.interfaces.Claim;
import com.example.booking.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
@AllArgsConstructor
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElse(null);
        Map<String, Claim> claims = jwtUtil.validateToken(jwtToken);
        if (claims != null) {
            String username = claims.get(RegisteredClaims.SUBJECT).asString();
            String[] authorities = claims.get("authorities").asArray(String.class);
            //TODO capire sintassi lambda per map sottostante
            //quando facciamo new UsernamePasswordAuthenticationToken abbiamo authenticated a true
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, null,
                            Arrays.stream(authorities).map(a -> (GrantedAuthority) () -> a).toList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/booking/login");
    }
}
