package com.example.booking.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "session")
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshTokenHash;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private Boolean revoked;

    private String ipAddress;

    private String userAgent;

}
