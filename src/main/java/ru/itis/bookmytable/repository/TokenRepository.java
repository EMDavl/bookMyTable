package ru.itis.bookmytable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.bookmytable.entity.JwtToken;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<JwtToken, Long> {
    boolean existsByValueAndRevoked(String token, boolean revoked);

    Optional<JwtToken> findByValue(String value);
}
