package com.example.booking.controller;

import com.example.booking.dto.LoginRequest;
import com.example.booking.dto.LoginResponse;
import com.example.booking.dto.RefreshTokenResponse;
import com.example.booking.security.AuthenticationJwtService;
import com.example.booking.security.SessionService;
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
    private final AuthenticationJwtService authenticationJwtService;
    private final SessionService sessionService;
    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest,
                                               HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authenticationJwtService.loginAuthentication(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                request);
        Cookie cookie = new Cookie(COOKIE_REFRESH_TOKEN, loginResponse.getRefreshToken());
        //cookie.setSecure(true); // use HTTPS in production -> (non adesso)
        cookie.setHttpOnly(true); //makes the cookie inaccessible to JavaScript (mitigates XSS).
        cookie.setPath("/"); //ensures the cookie is sent for all endpoints
        //TODO cookie.setSameSite("Strict"); -> da capire ancora
        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponse); //il refresh token viene passato solo tramite cookie
    }

    //"invalido il refresh token -> l'access token rimarrà valido fino alla sua scadenza (il frontend cancellarà i token memorizzati e l'utente dovrà effettuare un nuovo login)
    @PatchMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken) {
        sessionService.revokeSession(refreshToken);
        return ResponseEntity.ok("Logout successfully");
    }

    //il client chiamerà questo endpoint quando l'access token è scaduto, o eventualmente se il frontend si accorge che sta per scadere, chiama direttamente questo endpoint
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@CookieValue(COOKIE_REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        RefreshTokenResponse refreshTokenResponse = sessionService.refreshToken(refreshToken);
        Cookie cookie = new Cookie(COOKIE_REFRESH_TOKEN, refreshTokenResponse.getRefreshToken());
        //cookie.setSecure(true); // use HTTPS in production -> (non adesso)
        cookie.setHttpOnly(true); //makes the cookie inaccessible to JavaScript (mitigates XSS).
        cookie.setPath("/"); //ensures the cookie is sent for all endpoints
        //TODO cookie.setSameSite("Strict"); -> da capire ancora
        response.addCookie(cookie);
        return ResponseEntity.ok(refreshTokenResponse); //il refresh token viene passato solo tramite cookie
    }

    // refresh token? -> non è necessario nel nostro caso.. ma lo facciamo

    //POST /auth/refresh-token -> riceve un refresh token e restituisce un nuovo access token
    //controllo se il refresh token è valido e non expired, se sì genero un nuovo access token e lo restituisco al client
    //il refresh token vedo che è stato memorizzato nel db, ma è necessario? -> si, per poterlo invalidare ecc. è piu sicuro, diventa "stateless"..
    //per generare il refresh token, si potrebbe usare un uuid
    // un identificatore univoco per ogni refresh token, System.Security.Cryptography.RandomNumberGenerato
    //continuare..


    /*
    If this implementation is accepted, after the renewing the Jwt token process, the used refresh token should remain in the database/repository.
In the other hand, the longer lifetime means a higher potential for attackers. Assigning refresh tokens for only one-time use will be safer but requires a refresh token renewing process when the connected access token is being renewed.

    * */


}
