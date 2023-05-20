package ru.itis.bookmytable.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.bookmytable.dto.JwtTokenResponse;
import ru.itis.bookmytable.dto.SignInRequest;
import ru.itis.bookmytable.dto.SignUpRequest;
import ru.itis.bookmytable.entity.Role;
import ru.itis.bookmytable.entity.RoleNames;
import ru.itis.bookmytable.entity.User;
import ru.itis.bookmytable.exceptions.WrongRequestException;
import ru.itis.bookmytable.repository.RoleRepository;
import ru.itis.bookmytable.repository.UserRepository;

import java.util.Set;

import static ru.itis.bookmytable.config.security.JwtTokenFilter.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtTokenResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new WrongRequestException("Email is already taken");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Set.of(roleRepository.getReferenceByName(RoleNames.USER.name())))
                .build();

        User savedUser = userRepository.save(user);
        return new JwtTokenResponse(tokenService.createToken(savedUser.getEmail(), savedUser.getId(), RoleNames.USER));
    }


    @Transactional
    public JwtTokenResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new WrongRequestException("Wrong email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongRequestException("Wrong email or password");
        }

        return new JwtTokenResponse(tokenService.createToken(user.getEmail(), user.getId(), user.getRoles().stream().map(Role::getName).toList()));
    }

    public void logout(String token) {
        tokenService.revokeToken(token.substring(BEARER_PREFIX.length()));
    }
}
