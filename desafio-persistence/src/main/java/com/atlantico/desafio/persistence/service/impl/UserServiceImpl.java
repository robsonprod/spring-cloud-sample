package com.atlantico.desafio.persistence.service.impl;

import com.atlantico.desafio.persistence.model.User;
import com.atlantico.desafio.persistence.repository.UserRepository;
import com.atlantico.desafio.persistence.service.UserService;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Page<User> paginate(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public <S extends User> S save(S s) {
        val bCrypt = new BCryptPasswordEncoder(12);

        if (Objects.nonNull(s.getId())) {
            val current = findById(s.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (StringUtils.isNotEmpty(s.getPassword()) && !s.getPassword().equals(current.getPassword())
                    || !bCrypt.matches(s.getPassword(), current.getPassword()))
                s.setPassword(bCrypt.encode(s.getPassword()));
        } else {
            s.setPassword(bCrypt.encode(s.getPassword()));
        }

        return userRepository.save(s);
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
        return userRepository.saveAll(iterable);
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return userRepository.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return userRepository.existsById(aLong);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> iterable) {
        return userRepository.findAllById(iterable);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteAll(Iterable<? extends User> iterable) {
        userRepository.deleteAll(iterable);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
