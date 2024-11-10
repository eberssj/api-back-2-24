package com.example.api2024.controller;

import com.example.api2024.dto.PermissaoDto;
import com.example.api2024.entity.Permissao;
import com.example.api2024.service.PermissaoService;
import com.example.api2024.service.ArquivoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permissao")
@CrossOrigin(origins = "http://localhost:5173")
public class PermissaoController {

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private ArquivoService arquivoService;

    @Autowired
    private ObjectMapper objectMapper;

    // Endpoint para criar uma solicitação de criação de projeto
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

    // Endpoint para solicitar a edição de um projeto com upload de arquivos
    @PostMapping(value = "/solicitarEdicao", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Permissao> solicitarEdicaoProjeto(
            @RequestPart("solicitacao") String solicitacaoJson,
            @RequestPart(value = "propostas", required = false) MultipartFile propostas,
            @RequestPart(value = "contratos", required = false) MultipartFile contratos,
            @RequestPart(value = "artigos", required = false) MultipartFile artigos) {
        try {
            PermissaoDto solicitacaoDto = objectMapper.readValue(solicitacaoJson, PermissaoDto.class);
            Permissao permissao = permissaoService.solicitarEdicaoProjeto(
                    solicitacaoDto.getAdminSolicitanteId(),
                    solicitacaoDto.getStatusSolicitado(),
                    solicitacaoDto.getProjetoId(),
                    solicitacaoDto.getInformacaoProjeto(),
                    solicitacaoDto.getTipoAcao()
            );

            if (propostas != null && !propostas.isEmpty()) {
                arquivoService.salvarArquivo(propostas, permissao.getProjeto(), "Propostas", false);
            }
            if (contratos != null && !contratos.isEmpty()) {
                arquivoService.salvarArquivo(contratos, permissao.getProjeto(), "Contratos", false);
            }
            if (artigos != null && !artigos.isEmpty()) {
                arquivoService.salvarArquivo(artigos, permissao.getProjeto(), "Artigos", false);
            }

            return ResponseEntity.status(201).body(permissao);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    // Endpoint para aceitar uma solicitação
    @PostMapping("/aceitar/{id}")
    public ResponseEntity<Permissao> aceitarSolicitacao(
            @PathVariable Long id,
            @RequestParam Long adminAprovadorId) {
        System.out.println("Aceitando solicitação ID: " + id + " com adminAprovadorId: " + adminAprovadorId);
        try {
            Permissao permissao = permissaoService.aceitarSolicitacao(id, adminAprovadorId);
            return ResponseEntity.ok(permissao);
        } catch (Exception e) {
            System.err.println("Erro ao aceitar solicitação: " + e.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }



    // Endpoint para listar todas as solicitações pendentes
    @GetMapping("/pedidos")
    public ResponseEntity<List<PermissaoDto>> listarPedidosPendentes() {
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
    }

    // Endpoint para solicitar exclusão de um projeto
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


    // Endpoint para aprovar exclusão de um projeto
    @PostMapping("/aprovarExclusao/{id}")
    public ResponseEntity<Permissao> aprovarExclusao(@PathVariable Long id, @RequestParam Long adminAprovadorId) {
        Permissao permissao = permissaoService.aceitarSolicitacao(id, adminAprovadorId);
        return ResponseEntity.ok(permissao);
    }

    // Endpoint para negar uma solicitação
    @PostMapping("/negar/{id}")
    public ResponseEntity<Permissao> negarSolicitacao(@PathVariable Long id, @RequestParam Long adminAprovadorId) {
        Permissao permissao = permissaoService.negarSolicitacao(id, adminAprovadorId);
        return ResponseEntity.ok(permissao);
    }
}
