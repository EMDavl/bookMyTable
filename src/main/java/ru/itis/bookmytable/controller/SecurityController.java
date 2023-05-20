package ru.itis.bookmytable.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.bookmytable.dto.JwtTokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.bookmytable.dto.SignInRequest;
import ru.itis.bookmytable.dto.SignUpRequest;
import ru.itis.bookmytable.service.SecurityService;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/sign-up")
    public ResponseEntity<JwtTokenResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(securityService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(securityService.signIn(request));
    }

    @GetMapping
    public ResponseEntity<Object> logout(@Header("Authorization") String token) {
        securityService.logout(token);
        return ResponseEntity.ok().build();
    }

}
