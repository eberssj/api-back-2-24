package com.example.api2024.controller;

import com.example.api2024.dto.PermissaoDto;
import com.example.api2024.entity.Permissao;
import com.example.api2024.service.PermissaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permissao")
@CrossOrigin(origins = "http://localhost:5173")
public class PermissaoController {

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. Solicitar Criação
    @PostMapping(value = "/solicitarCriacao", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarSolicitacao(
            @RequestPart("solicitacao") String solicitacaoJson,
            @RequestPart(value = "propostas", required = false) MultipartFile propostas,
            @RequestPart(value = "contratos", required = false) MultipartFile contratos,
            @RequestPart(value = "artigos", required = false) MultipartFile artigos) {
        try {
            PermissaoDto solicitacaoDto = objectMapper.readValue(solicitacaoJson, PermissaoDto.class);
            Permissao permissao = permissaoService.criarSolicitacaoComArquivos(
                    solicitacaoDto.getAdminSolicitanteId(),
                    solicitacaoDto.getStatusSolicitado(),
                    solicitacaoDto.getInformacaoProjeto(),
                    "Criacao",
                    propostas,
                    contratos,
                    artigos
            );
            return ResponseEntity.status(201).body(permissao);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao criar solicitação de criação: " + e.getMessage());
        }
    }

    // 2. Solicitar Edição
    @PostMapping(value = "/solicitarEdicao", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> solicitarEdicaoProjeto(
            @RequestPart("solicitacao") String solicitacaoJson,
            @RequestPart(value = "propostas", required = false) MultipartFile propostas,
            @RequestPart(value = "contratos", required = false) MultipartFile contratos,
            @RequestPart(value = "artigos", required = false) MultipartFile artigos) {
        try {
            PermissaoDto solicitacaoDto = objectMapper.readValue(solicitacaoJson, PermissaoDto.class);
            Permissao permissao = permissaoService.solicitarEdicaoProjeto(
                    solicitacaoDto.getAdminSolicitanteId(),
                    "Pendente",
                    solicitacaoDto.getProjetoId(),
                    solicitacaoDto.getInformacaoProjeto(),
                    "Editar"
            );

            permissaoService.salvarArquivoPermissao(propostas, permissao, "Propostas");
            permissaoService.salvarArquivoPermissao(contratos, permissao, "Contratos");
            permissaoService.salvarArquivoPermissao(artigos, permissao, "Artigos");

            return ResponseEntity.status(201).body(permissao);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao solicitar edição: " + e.getMessage());
        }
    }

    // 3. Aprovar Solicitação (Criação, Edição ou Exclusão)
    @PostMapping("/aceitar/{id}")
    public ResponseEntity<?> aceitarSolicitacao(
            @PathVariable Long id,
            @RequestParam Long adminAprovadorId) {
        try {
            Permissao permissao = permissaoService.aceitarSolicitacao(id, adminAprovadorId);
            return ResponseEntity.ok(permissao);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao aceitar solicitação: " + e.getMessage());
        }
    }

    // 4. Solicitar Exclusão
    @PostMapping("/solicitarExclusao")
    public ResponseEntity<?> solicitarExclusao(@RequestBody PermissaoDto solicitacaoDto) {
        try {
            Permissao permissao = permissaoService.criarSolicitacao(
                    solicitacaoDto.getAdminSolicitanteId(),
                    "Pendente",
                    solicitacaoDto.getInformacaoProjeto(),
                    "Exclusao"
            );
            return ResponseEntity.status(201).body(permissao);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao solicitar exclusão: " + e.getMessage());
        }
    }

    // 5. Listar Solicitações Pendentes
    @GetMapping("/pedidos")
    public ResponseEntity<?> listarPedidosPendentes() {
        try {
            List<Permissao> pedidos = permissaoService.listarPedidosPendentes();
            List<PermissaoDto> pedidosDto = pedidos.stream()
                    .map(permissao -> new PermissaoDto(
                            permissao.getId(),
                            permissao.getAdminSolicitanteId(),
                            permissao.getStatusSolicitado(),
                            permissao.getDataSolicitacao(),
                            permissao.getDataAprovado(),
                            permissao.getAdm() != null ? permissao.getAdm().getId() : null,
                            permissao.getProjeto() != null ? permissao.getProjeto().getId() : null,
                            permissao.getInformacaoProjeto(),
                            permissao.getTipoAcao()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(pedidosDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao listar pedidos pendentes: " + e.getMessage());
        }
    }
}
