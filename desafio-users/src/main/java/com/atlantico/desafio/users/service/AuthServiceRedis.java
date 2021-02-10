package com.atlantico.desafio.users.service;

import com.atlantico.desafio.persistence.service.CustomUserDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceRedis {

    private static final String BEARER_PREFIX = "Bearer ";

    private final RedisTemplate<String, String> redis;
    private final CustomUserDetailsService userDetailsService;

    public Optional<Authentication> authenticate(HttpServletRequest request) {
        return extractBearerTokenHeader(request).flatMap(this::lookup);
    }

    private Optional<Authentication> lookup(String token) {
        try {
            val userId = (String) this.redis.opsForHash().get("tokens", token);

            if (nonNull(userId)) {
                val user = Optional.of(this.userDetailsService.loadUserByUsername(userId))
                        .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));

                val authentication = createAuthentication(user);
                return Optional.of(authentication);
            }

            return Optional.empty();
        } catch (Exception e) {
            log.warn("Unknown error while trying to look up Redis token", e);
            return Optional.empty();
        }
    }

    private static Optional<String> extractBearerTokenHeader(@NonNull HttpServletRequest request) {
        try {
            val authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (nonNull(authorization)) {
                if (authorization.startsWith(BEARER_PREFIX)) {
                    val token = authorization.substring(BEARER_PREFIX.length()).trim();

                    if (!token.isEmpty()) {
                        return Optional.of(token);
                    }
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            log.error("An unknown error occurred while trying to extract bearer token", e);
            return Optional.empty();
        }
    }

    private static Authentication createAuthentication(UserDetails userDetails) {
        val principal = nonNull(userDetails.getUsername()) ? userDetails.getUsername() : "N/A";
        return new UsernamePasswordAuthenticationToken(principal,"N/A", userDetails.getAuthorities());
    }
}
