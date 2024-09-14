package com.example.api2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.api2024.configCors.JwtGenerate;
import com.example.api2024.dto.AcessoDto;
import com.example.api2024.dto.authenticacaoDto;

@Service
public class AuthenticacaoService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerate jwtGenerate;

    public ResponseEntity<?> login(authenticacaoDto autenticacaoDto) {

        try {
            UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(autenticacaoDto.getEmail(), autenticacaoDto.getPassword());

            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(userAuth);

            AdmDetails admDetails = (AdmDetails) authentication.getPrincipal();

            String token = jwtGenerate.AdmTokenDetailsImpl(admDetails);

            AcessoDto acessoDto = new AcessoDto(token);

            return ResponseEntity.ok(acessoDto);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Credenciais inválidas", HttpStatus.UNAUTHORIZED);

        } catch (DisabledException e) {
            return new ResponseEntity<>("Usuário desabilitado", HttpStatus.UNAUTHORIZED);
        }

    }
}