package ru.itis.bookmytable.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.bookmytable.entity.User;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(type = LOAD, attributePaths = "roles")
    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberIgnoreCase(String phoneNumber);
}
