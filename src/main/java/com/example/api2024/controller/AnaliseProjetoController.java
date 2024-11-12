package com.example.api2024.controller;

import com.example.api2024.entity.AnaliseProjeto;
import com.example.api2024.service.AnaliseProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analises")
public class AnaliseProjetoController {

    @Autowired
    private AnaliseProjetoService analiseProjetoService;

    @PostMapping("/salvar")
    public ResponseEntity<AnaliseProjeto> salvarAnalise(@RequestBody AnaliseProjeto analise) {
        AnaliseProjeto novaAnalise = analiseProjetoService.salvarAnalise(analise);
        return ResponseEntity.ok(novaAnalise);
    }
}
