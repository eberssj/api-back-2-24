package com.example.api2024.controller;

import com.example.api2024.dto.ProjetoDto;
import com.example.api2024.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public List<com.example.api2024.entity.Projeto> listarProjeto() {
        return projetoRepository.findAll();
    }

    @PostMapping("/cadastrar")
    public void cadastrarProjeto(
            @RequestPart("projeto") ProjetoDto projetoDto,
            @RequestPart(value = "propostas", required = false) MultipartFile propostas,
            @RequestPart(value = "contratos", required = false) MultipartFile contratos,
            @RequestPart(value = "artigos", required = false) MultipartFile artigos) throws Exception {

        // Imprimir as informações recebidas
        System.out.println("Recebendo projeto: " + projetoDto);

        if (propostas != null) {
            System.out.println("Recebendo arquivo de propostas: " + propostas.getOriginalFilename());
        } else {
            System.out.println("Nenhum arquivo de propostas recebido.");
        }

        if (contratos != null) {
            System.out.println("Recebendo arquivo de contratos: " + contratos.getOriginalFilename());
        } else {
            System.out.println("Nenhum arquivo de contratos recebido.");
        }

        if (artigos != null) {
            System.out.println("Recebendo arquivo de artigos: " + artigos.getOriginalFilename());
        } else {
            System.out.println("Nenhum arquivo de artigos recebido.");
        }

        // Passar os arquivos diretamente para o serviço
        projetoService.cadastrarProjeto(projetoDto, propostas, contratos, artigos);
    }
}
