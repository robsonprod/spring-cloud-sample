package com.atlantico.desafio.users.resource;

import com.atlantico.desafio.persistence.model.User;
import com.atlantico.desafio.persistence.service.UserService;
import com.atlantico.desafio.users.domain.MessageEmail;
import com.atlantico.desafio.users.domain.Receiver;
import com.atlantico.desafio.users.domain.UserCreateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.atlantico.desafio.users.config.RabbitConfig.queueName;
import static com.atlantico.desafio.users.config.RabbitConfig.topicExchangeName;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class UserController {

    private final RabbitTemplate rabbitTemplate;
    private final UserService userService;
    private final Receiver receiver;

    @PostMapping("signIn")
    @ResponseBody
    public ResponseEntity<?> signIn(@Valid @RequestBody UserCreateDTO body) {
        // TODO: tratar esse tipo de exceção em {ErrorHandler}
        val user = Optional.of(userService.save(body.toUser()))
                .orElseThrow(() -> new IllegalArgumentException("Tivemos um problema ao salvar o usuário"));

        val response = new UserCreateDTO(user.getName(), user.getLogin(), user.getEmail(), null);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @ResponseBody
    public ResponseEntity<?> index(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        val paginate = userService.paginate(PageRequest.of(page, 10));

        val response = new LinkedHashMap<String, Object>(){{
            put("data", paginate.getContent());
            put("total", paginate.getTotalElements());
            put("page", paginate.getPageable().getPageNumber());
            put("size", paginate.getPageable().getPageSize());
        }};

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @ResponseBody
    public ResponseEntity<?> save(@Valid @RequestBody UserCreateDTO body) {

        return Optional.of(userService.save(body.toUser()))
                .map(UserCreateDTO::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Tivemos um problema ao salvar o usuário"));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @ResponseBody
    public ResponseEntity<?> update(@AuthenticationPrincipal User userCurrent,
                                    @Valid @RequestBody UserCreateDTO body,
                                    @PathVariable("id") Long id) {

        if (!userCurrent.getEmail().equals(body.getEmail()) && !userCurrent.isAdmin() && StringUtils.isNotEmpty(body.getPassword())) {
            throw new AuthorizationServiceException("Did not grants update user, user don't is admin");
        }

        return userService.findById(id)
                .map(u -> {
                    if (StringUtils.isNotEmpty(body.getPassword()))
                        u.setPassword(body.getPassword());

                    u.setEmail(body.getEmail());
                    u.setName(body.getName());

                    return u;
                })
                .map(userService::save)
                .map(UserCreateDTO::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> view(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Test :: " + id);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Test :: " + id);
    }

    @PostMapping("email")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @ResponseBody
    public ResponseEntity<?> publishEmail(@RequestBody @Valid MessageEmail message) throws InterruptedException {
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(topicExchangeName, queueName, message);
        receiver.getLatch().await(1000, MILLISECONDS);
        return ResponseEntity.ok("published");
    }
}
