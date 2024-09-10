package com.example.api2024.controller;

import com.example.api2024.dto.ProjetoDto;
import com.example.api2024.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.api2024.repository.ProjetoRepository;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {
    
    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private ProjetoService projetoService;

    @GetMapping("/listar")
    public List<com.example.api2024.entity.Projeto> listarProjeto() { return projetoRepository.findAll(); }

    @PostMapping("/cadastrar")
    public void cadastrarProjeto(@RequestBody ProjetoDto projeto) throws Exception{
        projetoService.cadastrarProjeto(projeto);
    }
}
