package com.example.api2024.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthToken extends OncePerRequestFilter {
    @Autowired
    private JwtGenerate jwtGenerate;
}
