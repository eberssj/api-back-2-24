package com.example.api2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.api2024.dto.authenticacaoDto;
import com.example.api2024.service.AuthenticacaoService;

@RestController
@CrossOrigin
public class authenticarController {
    
    @Autowired
    private AuthenticacaoService authenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody authenticacaoDto autenticacaoDto) {
        ResponseEntity<?> response = authenticacaoService.login(autenticacaoDto);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.getBody());
    }
        return ResponseEntity.ok(response.getBody());
    }
}