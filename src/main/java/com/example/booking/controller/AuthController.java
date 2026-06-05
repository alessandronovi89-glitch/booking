package com.example.booking.controller;

import com.example.booking.dto.LoginRequest;
import com.example.booking.dto.LoginResponse;
import com.example.booking.security.AuthenticationJwtService;
import com.example.booking.security.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationJwtService authenticationJwtService;
    private final SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                authenticationJwtService.loginAuthentication(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
    }

    //"invalido il refresh token, l'access token rimarrà valido fino alla sua scadenza
    //il frontend cancellarà i token memorizzati e l'utente dovrà effettuare un nuovo login
    @PatchMapping("/logout/{refreshToken}")
    public ResponseEntity<String> logout(@PathVariable String refreshToken) {
        sessionService.revokeSession(refreshToken);
        return ResponseEntity.ok("Logout successfully");
    }


    //TODO logout? -> non serve, non ha senso in jwt, è stateless
    // -> con il refresh token , facendo logut faremo un revoke della sessione..

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
