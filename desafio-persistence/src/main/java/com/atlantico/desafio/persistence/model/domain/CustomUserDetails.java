package com.atlantico.desafio.persistence.model.domain;

import com.atlantico.desafio.persistence.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CustomUserDetails extends User implements UserDetails {

    public CustomUserDetails(final User user) {
        super(user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getPassword(), user.isAdmin());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(getRole())
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority))
                .collect(toList());
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
