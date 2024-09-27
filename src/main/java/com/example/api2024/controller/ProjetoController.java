package com.example.api2024.controller;

import com.example.api2024.dto.ProjetoDto;
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

    @GetMapping("/listar")
    public List<com.example.api2024.entity.Projeto> listarProjeto() {
        return projetoService.listarProjetos();
    }

    @PostMapping("/cadastrar")
    public void cadastrarProjeto(
            @RequestPart("projeto") ProjetoDto projetoDto,
            @RequestPart(value = "propostas", required = false) MultipartFile propostas,
            @RequestPart(value = "contratos", required = false) MultipartFile contratos,
            @RequestPart(value = "artigos", required = false) MultipartFile artigos) throws Exception {

        projetoService.cadastrarProjeto(projetoDto, propostas, contratos, artigos);
    }
}
