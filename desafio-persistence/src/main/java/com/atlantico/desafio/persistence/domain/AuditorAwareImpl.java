package com.atlantico.desafio.persistence.domain;

import com.atlantico.desafio.persistence.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {

            try {
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                return Optional.ofNullable(String.format("%d:%s", user.getId(), user.getName().toUpperCase()));
            } catch (Exception e) {
                return Optional.empty();
            }

        }
}
