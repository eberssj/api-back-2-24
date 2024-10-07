package com.example.api2024.controller;

import com.example.api2024.dto.ProjetoDto;
import com.example.api2024.entity.Projeto;
import com.example.api2024.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    // Endpoint para listar todos os projetos
    @GetMapping("/listar")
    public List<Projeto> listarProjeto() {
        return projetoService.listarProjetos();
    }

    // Endpoint para cadastrar um novo projeto
    @PostMapping("/cadastrar")
    public void cadastrarProjeto(
            @RequestPart("projeto") ProjetoDto projetoDto,
            @RequestPart(value = "propostas", required = false) MultipartFile propostas,
            @RequestPart(value = "contratos", required = false) MultipartFile contratos,
            @RequestPart(value = "artigos", required = false) MultipartFile artigos) throws Exception {

        projetoService.cadastrarProjeto(projetoDto, propostas, contratos, artigos);
    }

    // Endpoint para editar um projeto
    @PutMapping("/editar/{id}")
    public Projeto editarProjeto(
            @PathVariable Long id,
            @RequestPart("projeto") ProjetoDto projetoDto,
            @RequestPart(value = "propostas", required = false) MultipartFile propostas,
            @RequestPart(value = "contratos", required = false) MultipartFile contratos,
            @RequestPart(value = "artigos", required = false) MultipartFile artigos) throws Exception {
        return projetoService.editarProjeto(id, projetoDto, propostas, contratos, artigos);
    }

    // Endpoint para excluir um projeto
    @DeleteMapping("/excluir/{id}")
    public void excluirProjeto(@PathVariable Long id) {
        projetoService.excluirProjeto(id);
    }
}

