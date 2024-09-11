package com.example.api2024.adapt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AdmDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> tipo;

    public AdmDetails(String email, String senha, Collection<? extends GrantedAuthority> tipo) {
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.tipo;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
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
}