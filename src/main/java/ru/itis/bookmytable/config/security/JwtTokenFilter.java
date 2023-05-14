package ru.itis.bookmytable.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.bookmytable.dto.CommonResponseEntity;
import ru.itis.bookmytable.exceptions.JwtAuthenticationException;
import ru.itis.bookmytable.exceptions.TokenRevokedException;
import ru.itis.bookmytable.service.JwtTokenService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final List<RequestMatcher> reqMatcher = List.of(
            new AntPathRequestMatcher("/sign-in/**"),
            new AntPathRequestMatcher("/sign-up/**")
    );
    public static final String NO_HEADER_MSG = "No authorization header present";
    public static final String NOT_BEARER_PREFIX_MSG = "Authorization token must have \"Bearer\" prefix";

    private final JwtTokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader(AUTHORIZATION_HEADER);
            if (reqMatcher.stream().anyMatch(r -> r.matches(request))) {
                filterChain.doFilter(request, response);
                return;
            }
            if (token == null) {
                throw new JwtAuthenticationException(NO_HEADER_MSG);
            }
            if (!token.startsWith(BEARER_PREFIX)) {
                throw new JwtAuthenticationException(NOT_BEARER_PREFIX_MSG);
            }
            processToken(token);
        } catch (JwtAuthenticationException | TokenRevokedException | JwtException e) {
            log.error("Failed to authenticate user", e);
            sendErrorResponse(response, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) {
        response.setStatus(SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            CommonResponseEntity<String> msg = new CommonResponseEntity<>(SC_UNAUTHORIZED, message);
            response.getWriter().println(mapper.writeValueAsString(msg));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processToken(String token) {
        String userEmail = tokenService.parseToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(context);
    }
}
