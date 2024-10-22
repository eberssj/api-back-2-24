package com.example.api2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api2024.dto.PermissaoDto;
import com.example.api2024.entity.Permissao;
import com.example.api2024.service.PermissaoService;

@RestController
@RequestMapping("/permissao")
public class PermissaoController {

    @Autowired
    private PermissaoService permissaoService;  // Use a injeção de dependência correta

    @PostMapping("/solicitar")
    public ResponseEntity<Permissao> solicitarAlteracao(@RequestBody PermissaoDto solicitacaoDTO) {
        // Chama o método no serviço, não diretamente na classe
        Permissao solicitacao = permissaoService.criarSolicitacao(
                solicitacaoDTO.getAdminSolicitanteId(),
                solicitacaoDTO.getProjetoId());
        return ResponseEntity.ok(solicitacao);
    }
}