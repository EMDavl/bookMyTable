package ru.itis.bookmytable.config.security.userDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.itis.bookmytable.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final String NOT_FOUND_MESSAGE = "User with provided phone number %s is not found";

    @Override
    @Cacheable(cacheNames = "USER_BY_USERNAME")
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return new UserDetailsData(userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_MESSAGE.formatted(phoneNumber))));
    }
}
