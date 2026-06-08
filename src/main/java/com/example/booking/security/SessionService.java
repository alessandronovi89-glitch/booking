package com.example.booking.security;

import com.example.booking.configuration.TokenProperties;
import com.example.booking.db.Session;
import com.example.booking.db.User;
import com.example.booking.dto.RefreshTokenResponse;
import com.example.booking.repository.SessionRepository;
import com.google.common.hash.Hashing;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final TokenProperties tokenProperties;

    public UUID generateSession(User user, HttpServletRequest request) {
        UUID refreshToken = UUID.randomUUID();
        Session session = new Session();
        session.setUser(user);
        session.setRevoked(false);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(tokenProperties.getRefreshTokenExpirationDays()));
        session.setRefreshTokenHash(hashToken(refreshToken.toString()));
        session.setUserAgent(request.getHeader("User-Agent"));
        session.setIpAddress(getIpAddress(request));
        return refreshToken;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return StringUtils.isNotBlank(ip) ? ip : request.getRemoteAddr();
    }

    public void revokeSession(String refreshToken) {
        Session session = sessionRepository.findByRefreshTokenHash(hashToken(refreshToken)).orElseThrow(() -> new RuntimeException("Session not found"));
        session.setRevoked(true);
        sessionRepository.save(session);
    }

    private String hashToken(String refreshToken) {
        return Hashing.sha256()
                .hashString(refreshToken, StandardCharsets.UTF_8)
                .toString();
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        Session session = sessionRepository.findByRefreshTokenHash(hashToken(refreshToken)).orElseThrow(() -> new RuntimeException("Session not found"));
        if (session.getRevoked() || session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Session is invalid");
        }
        UUID newTokenRefresh = UUID.randomUUID();
        session.setRefreshTokenHash(hashToken(newTokenRefresh.toString()));
        session.setExpiresAt(LocalDateTime.now().plusDays(tokenProperties.getRefreshTokenExpirationDays()));
        //session.setUpdate -> facoltativo, solo se vuoi fare tracking
        sessionRepository.save(session);
        //String jwtToken = generateToken(new CustomUserDetails(user));
        return RefreshTokenResponse.builder()
                .jwtToken("123") //TODO
                .message("token refreshed successfully")
                .refreshToken(refreshToken.toString()).build();


    }
}
