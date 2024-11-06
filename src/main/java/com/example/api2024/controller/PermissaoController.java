package com.example.api2024.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/solicitarCriacao")
    public ResponseEntity<Permissao> criarSolicitacao(@RequestBody PermissaoDto solicitacaoDto) {
        Permissao permissao = permissaoService.criarSolicitacao(
                solicitacaoDto.getAdminSolicitanteId(),
                solicitacaoDto.getStatusSolicitado(),
                solicitacaoDto.getDataSolicitacao(),
                solicitacaoDto.getInformacaoProjeto(),
                solicitacaoDto.getTipoAcao()
        );

        return ResponseEntity.status(201).body(permissao);
    }

    @PostMapping("/aceitar/{id}")
    public ResponseEntity<Permissao> aceitarSolicitacao(@PathVariable Long id, @RequestParam Long adminAprovadorId) {
        Permissao permissao = permissaoService.aceitarSolicitacao(id, adminAprovadorId);
        return ResponseEntity.ok(permissao);
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<PermissaoDto>> listarPedidosPendentes() {
        List<Permissao> pedidos = permissaoService.listarPedidosPendentes();
        List<PermissaoDto> pedidosDto = pedidos.stream().map(permissao -> new PermissaoDto(
            permissao.getId(),
            permissao.getAdminSolicitanteId(),
            permissao.getStatusSolicitado(),
            permissao.getDataSolicitacao(),
            permissao.getDataAprovado(),
            permissao.getAdm() != null ? permissao.getAdm().getId() : null,
            permissao.getProjeto() != null ? permissao.getProjeto().getId() : null,
            permissao.getInformacaoProjeto(),
            permissao.getTipoAcao()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(pedidosDto);
    }

    @PostMapping("/solicitarExclusao")
    public ResponseEntity<Permissao> solicitarExclusao(@RequestBody PermissaoDto solicitacaoDto) {
        Permissao permissao = permissaoService.criarSolicitacao(
                solicitacaoDto.getAdminSolicitanteId(),
                "Pendente",
                LocalDate.now(),
                solicitacaoDto.getInformacaoProjeto(),
                "Exclusao"
        );

        return ResponseEntity.status(201).body(permissao);
    }

    @PostMapping("/aprovarExclusao/{id}")
    public ResponseEntity<Permissao> aprovarExclusao(@PathVariable Long id, @RequestParam Long adminAprovadorId) {
        Permissao permissao = permissaoService.aceitarSolicitacao(id, adminAprovadorId);
        return ResponseEntity.ok(permissao);
    }
}