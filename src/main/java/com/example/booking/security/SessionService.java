package com.example.booking.security;

import com.example.booking.db.Session;
import com.example.booking.db.User;
import com.example.booking.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    // UUID refreshToken = UUID.randomUUID(); //volendo possiamo generare un token piu sicuro, ma uuid va bene
    //      sessionRepository.save(new Session(refreshToken, userDetails.getUsername(), Instant.now().plus(7, MINUTES))); //TODO scadenza refresh token, 7 giorni per esempio
    public UUID generateSession(User user) {
        UUID refreshToken = UUID.randomUUID();
        Session session = new Session();
        session.setUser(user);
        session.setRevoked(false);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(7)); //leggere la scadenza da properties
        session.setRefreshTokenHash(refreshToken.toString());
        session.setUserAgent("?");
        session.setIpAddress("?");
        return refreshToken;
    }

    public void revokeSession(String refreshToken) {
        //TODO hash
        Session session = sessionRepository.findByRefreshTokenHash(UUID.fromString(refreshToken)).orElseThrow(() -> new RuntimeException("Session not found"));
        session.setRevoked(true);
        sessionRepository.save(session);
    }
}
