package com.example.booking.security.filter;

import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.example.booking.security.AuthenticationJwtService;
import com.example.booking.service.security.UserDetailsService;
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

    private final AuthenticationJwtService jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = getJwtToken(request);
            if (jwtToken == null) { //senza token è anonymous, se l'endpoint è protetto l’authorization lo rifiuterà
                filterChain.doFilter(request, response);
                return;
            }
            Map<String, Claim> claims = jwtUtil.validateToken(jwtToken);
            if (claims != null) {
                String username = claims.get(RegisteredClaims.SUBJECT).asString();
                String[] authorities = claims.get("authorities").asArray(String.class);
                //TODO capire sintassi lambda per map sottostante
                //quando facciamo new UsernamePasswordAuthenticationToken abbiamo authenticated a true
                // Usiamo CustomUserDetails come principal dell'Authentication
                // così Spring Security può iniettarlo con @AuthenticationPrincipal
                // nei controller, evitando di avere solo lo username come String.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetailsService.loadUserByUsername(username),
                                null,
                                Arrays.stream(authorities).map(a -> (GrantedAuthority) () -> a).toList());
                //è qui che dico a spring security che l'utente è autenticato,
                //-> poi controlla sull'AuthorizationFilter se è autorizzato altrimenti da eccezione
                // chiamando prima SecurityContextHolderFilter che fa delle cose..
                // -> SecurityContext viene preparato/caricato prima, poi AuthorizationFilter legge l’Authentication e decide se la request è autorizzata.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpiredException e) {
            log.trace("expired token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"TOKEN_EXPIRED\"}");
            return; //interrompo la catena dei filtri, (not working throw new responseStatusException nei filtri)
        } catch (Exception e) {
            log.trace("error in validation token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"INVALID_TOKEN\"}");
            return; //interrompo la catena dei filtri, (not working throw new responseStatusException nei filtri)
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElse(null);
    }

    /*
    potrei disattivare il filtro, ma lascio passare se il token è nullo, se l'endpoint è protetto fallira l'autenticazione
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return List.of("/info/login", "/info").contains(request.getRequestURI());

    }*/
}
