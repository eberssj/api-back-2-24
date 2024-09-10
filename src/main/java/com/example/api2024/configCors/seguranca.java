package com.example.api2024.configCors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class seguranca {

    private static final String[] ALLOWED_ORIGINS = { "/projeto/cadastrar", "/projeto/listar"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorize -> authorize.requestMatchers(ALLOWED_ORIGINS).permitAll());

        return http.build();
    }
    
}
