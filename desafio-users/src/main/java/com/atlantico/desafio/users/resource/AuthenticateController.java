package com.atlantico.desafio.users.resource;

import com.atlantico.desafio.users.domain.UserCredentialsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "authenticate", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class AuthenticateController {

    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redis;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> login(@Valid @RequestBody UserCredentialsDTO user) {

        val token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        val auth= authenticationManager.authenticate(token);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        val userToken = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());

        redis.opsForHash().putIfAbsent("tokens", userToken, token.getPrincipal());

        return ResponseEntity.ok(new HashMap<String, String>(){{put("token", userToken);}});
    }
}
