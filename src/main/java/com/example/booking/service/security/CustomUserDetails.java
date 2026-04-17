package com.example.booking.service.security;

import com.example.booking.db.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // hashata
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // sempre valido
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // mai bloccato
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // credenziali sempre valide
    }

    @Override
    public boolean isEnabled() {
        return true; // account attivo
    }

    public Long getId() {
        return user.getId();
    }

}
