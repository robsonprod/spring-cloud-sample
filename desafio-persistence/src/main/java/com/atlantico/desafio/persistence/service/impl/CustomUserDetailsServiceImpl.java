package com.atlantico.desafio.persistence.service.impl;

import com.atlantico.desafio.persistence.model.domain.CustomUserDetails;
import com.atlantico.desafio.persistence.service.CustomUserDetailsService;
import com.atlantico.desafio.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userService.findByLogin(s)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user by login"));
    }
}
