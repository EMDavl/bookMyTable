package ru.itis.bookmytable.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // signUp - \
    // Должны быть доступны незалогиненному пользователю. Надо добавить возможность
    // login  - /

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   UserDetailsService userDetailsService,
                                                   JwtTokenFilter jwtTokenFilter) throws Exception {

        return http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin()
                .disable()
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests()
                    .requestMatchers("/sign-in/**", "/sign-up").permitAll()
                    .requestMatchers("/**").authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
