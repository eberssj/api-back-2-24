package com.example.api2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api2024.entity.Adm;
import com.example.api2024.entity.Permissao;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.AdmRepository;
import com.example.api2024.repository.PermissaoRepository;
import com.example.api2024.repository.ProjetoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

@Service
public class PermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired 
    private AdmRepository admRepository;

    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private ObjectMapper objectMapper; // Para converter JSON em objeto Java
    
    public Permissao criarSolicitacao(Long adminSolicitanteId, String statusSolicitado, LocalDate dataSolicitacao,
                                      String informacaoProjeto, String tipoAcao) {

        if ("Exclusao".equalsIgnoreCase(tipoAcao)) {
            statusSolicitado = "Pendente";
        }

        Permissao permissao = new Permissao();
        permissao.setAdminSolicitanteId(adminSolicitanteId);
        permissao.setStatusSolicitado(statusSolicitado);
        permissao.setDataSolicitacao(dataSolicitacao);
        permissao.setInformacaoProjeto(informacaoProjeto);
        permissao.setTipoAcao(tipoAcao);

        return permissaoRepository.save(permissao);
    }

    public Permissao aceitarSolicitacao(Long permissaoId, Long adminAprovadorId) {
        Permissao permissao = permissaoRepository.findById(permissaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));

        Adm adminAprovador = admRepository.findById(adminAprovadorId)
                .orElseThrow(() -> new IllegalArgumentException("Administrador aprovador não encontrado"));

        if (!"Pendente".equals(permissao.getStatusSolicitado())) {
            throw new IllegalStateException("A solicitação já foi processada");
        }

        if ("Exclusao".equalsIgnoreCase(permissao.getTipoAcao())) {
            return processarExclusaoProjeto(permissao, adminAprovador);
        } else {
            return processarCriacaoProjeto(permissao, adminAprovador);
        }
    }

    private Permissao processarCriacaoProjeto(Permissao permissao, Adm adminAprovador) {
        Projeto projeto = null;
        try {
            projeto = objectMapper.readValue(permissao.getInformacaoProjeto(), Projeto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao processar as informações do projeto", e);
        }

        projeto.setAdm(admRepository.findById(permissao.getAdminSolicitanteId()).get());
        Projeto projetoSalvo = projetoRepository.save(projeto);

        permissao.setStatusSolicitado("Aprovado");
        permissao.setDataAprovado(LocalDate.now());
        permissao.setAdm(adminAprovador);
        permissao.setProjeto(projetoSalvo);

        return permissaoRepository.save(permissao);
    }

    private Permissao processarExclusaoProjeto(Permissao permissao, Adm adminAprovador) {
        Long projetoId = permissao.getProjeto().getId();
        projetoRepository.deleteById(projetoId);

        permissao.setStatusSolicitado("Aprovado");
        permissao.setDataAprovado(LocalDate.now());
        permissao.setAdm(adminAprovador);

        return permissaoRepository.save(permissao);
    }

    public List<Permissao> listarPedidosPendentes() {
        return permissaoRepository.findByStatusSolicitado("Pendente");
    }

    public Permissao negarSolicitacao(Long permissaoId, Long adminAprovadorId) {
        Permissao permissao = permissaoRepository.findById(permissaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
    
        Adm adminAprovador = admRepository.findById(adminAprovadorId)
                .orElseThrow(() -> new IllegalArgumentException("Administrador aprovador não encontrado"));
    
        // Verificar se o pedido já foi processado
        if (!"Pendente".equals(permissao.getStatusSolicitado())) {
            throw new IllegalStateException("A solicitação já foi processada");
        }
    
        // Atualizar status para "Negado" e definir data de aprovação
        permissao.setStatusSolicitado("Negado");
        permissao.setDataAprovado(LocalDate.now());
        permissao.setAdm(adminAprovador);
    
        // Salvar a alteração
        return permissaoRepository.save(permissao);
    }    
}