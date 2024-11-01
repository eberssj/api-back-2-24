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
    public ResponseEntity<Permissao> solicitarCriacaoProjeto(@RequestBody PermissaoDto solicitacaoDto) {
        Permissao solicitacao = permissaoService.criarSolicitacao(
                solicitacaoDto.getAdminSolicitanteId(),
                solicitacaoDto.getStatusSolicitado(),
                solicitacaoDto.getDataSolicitacao(),
                solicitacaoDto.getInformacaoProjeto(),
                solicitacaoDto.getTipoAcao()
        );
        return ResponseEntity.status(201).body(solicitacao);
    }
}
