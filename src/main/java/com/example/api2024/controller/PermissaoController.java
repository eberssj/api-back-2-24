package com.example.api2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.api2024.dto.PermissaoDto;
import com.example.api2024.entity.Permissao;
import com.example.api2024.service.PermissaoService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/permissao")
public class PermissaoController {

    @Autowired
    private PermissaoService permissaoService;

    // Rota para enviar uma solicitação de criação de projeto
    @PostMapping("/solicitarCriacao")
    public ResponseEntity<Permissao> criarSolicitacao(@RequestBody PermissaoDto solicitacaoDto) {
        // Criar a solicitação de permissão usando o serviço
        Permissao permissao = permissaoService.criarSolicitacao(
                solicitacaoDto.getAdminSolicitanteId(),
                solicitacaoDto.getStatusSolicitado(),
                solicitacaoDto.getDataSolicitacao(), // Adicione isso se estiver utilizando a data
                solicitacaoDto.getInformacaoProjeto(),
                solicitacaoDto.getTipoAcao()
        );

        // Retornar a resposta com status 201 (Criado)
        return ResponseEntity.status(201).body(permissao);
    }
}
