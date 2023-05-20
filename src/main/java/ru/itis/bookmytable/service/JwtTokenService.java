package ru.itis.bookmytable.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.bookmytable.entity.JwtToken;
import ru.itis.bookmytable.entity.RoleNames;
import ru.itis.bookmytable.exceptions.JwtAuthenticationException;
import ru.itis.bookmytable.exceptions.TokenRevokedException;
import ru.itis.bookmytable.exceptions.WrongRequestException;
import ru.itis.bookmytable.repository.TokenRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("jwt.signing-key")
    private String signingKey;

    private final TokenRepository tokenRepository;

    private byte[] getSigningKey() {
        return Base64.getEncoder().encode(signingKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Парсит токен, проверяя его на валидность
     *
     * @param token значение токена из заголовка Authorization
     * @return subject для заданного JWT токена
     */
    public String parseToken(String token) {
        try {
            throwIfTokenAlreadyRevoked(token);
            return getAllClaims(token).getSubject();
        } catch (Exception e) {
            throw new JwtAuthenticationException("Malformed token passed");
        }
    }

    private void throwIfTokenAlreadyRevoked(String token) {
        if (tokenRepository.existsByValueAndRevoked(token, true)) {
            throw new TokenRevokedException("Passed token already revoked");
        }
    }

    private Claims getAllClaims(String token) {
        return (Claims) Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .parse(token)
                .getBody();
    }

    public String createToken(String email, Long id, RoleNames role) {
        String tokenValue = Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("roles", List.of(role.name()))
                .compact();


        tokenRepository.save(JwtToken.builder()
                        .value(tokenValue)
                        .revoked(false)
                .build());

        return tokenValue;
    }

    public String createToken(String email, Long id, List<String> roles) {
        String tokenValue = Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("role", roles)
                .compact();

        tokenRepository.save(JwtToken.builder()
                        .revoked(false)
                        .value(tokenValue)
                .build());

        return tokenValue;
    }

    @Transactional
    public void revokeToken(String token) {
        JwtToken tokenFromDb = tokenRepository.findByValue(token)
                .orElseThrow(() -> new WrongRequestException("Token not found"));
        tokenFromDb.setRevoked(true);
        tokenRepository.save(tokenFromDb);
    }
}
