package com.example.api2024.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api2024.entity.Permissao;
import com.example.api2024.repository.PermissaoRepository;

@RestController
@RequestMapping("/permissao")
public class PermissaoController {
    
    @Autowired
    private PermissaoRepository permissaoRepository;

    @GetMapping("/listar")
    public List<Permissao> listarPermissao() { return permissaoRepository.findAll(); }
}
