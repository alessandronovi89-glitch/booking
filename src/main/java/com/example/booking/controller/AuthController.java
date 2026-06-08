package com.example.booking.controller;

import com.example.booking.dto.LoginRequest;
import com.example.booking.dto.LoginResponse;
import com.example.booking.dto.RefreshTokenResponse;
import com.example.booking.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest,
                                               HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.loginAuthentication(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                request);
        //di default il sameSite è Lax
        Cookie cookie = new Cookie(COOKIE_REFRESH_TOKEN, loginResponse.getRefreshToken());
        //cookie.setSecure(true); // use HTTPS in production -> (non adesso)
        cookie.setHttpOnly(true); //makes the cookie inaccessible to JavaScript (mitigates XSS).
        cookie.setPath("/"); //ensures the cookie is sent for all endpoints
        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponse); //il refresh token viene passato solo tramite cookie
    }

    //"invalido il refresh token -> l'access token rimarrà valido fino alla sua scadenza (il frontend cancellarà i token memorizzati e l'utente dovrà effettuare un nuovo login)
    @PatchMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken) {
        authService.revokeSession(refreshToken);
        return ResponseEntity.ok("Logout successfully");
    }

    //il client chiamerà questo endpoint quando l'access token è scaduto, o eventualmente se il frontend si accorge che sta per scadere, chiama direttamente questo endpoint
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken,
                                                             HttpServletResponse response) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(refreshToken);
        Cookie cookie = new Cookie(COOKIE_REFRESH_TOKEN, refreshTokenResponse.getRefreshToken());
        //cookie.setSecure(true); // use HTTPS in production -> (non adesso)
        cookie.setHttpOnly(true); //makes the cookie inaccessible to JavaScript (mitigates XSS).
        cookie.setPath("/"); //ensures the cookie is sent for all endpoints
        response.addCookie(cookie);
        return ResponseEntity.ok(refreshTokenResponse); //il refresh token viene passato solo tramite cookie
    }

}
