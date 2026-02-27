package com.example.booking.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(SecurityContextHolder.getContext().getAuthentication().getName() +
                " : request to " + request.getRequestURI() + " - ip: " +
                Optional.ofNullable(request.getHeader("X-Forwarded-For")).orElse(request.getRemoteAddr()));
        filterChain.doFilter(request, response);
    }
}
