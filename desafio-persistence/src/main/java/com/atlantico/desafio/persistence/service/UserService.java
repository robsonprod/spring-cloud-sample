package com.atlantico.desafio.persistence.service;


import com.atlantico.desafio.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserService extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Page<User> paginate(Pageable pageable);
}
