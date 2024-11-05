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

        // Criar uma nova permissão e configurar os valores
        Permissao permissao = new Permissao();
        permissao.setAdminSolicitanteId(adminSolicitanteId);
        permissao.setStatusSolicitado(statusSolicitado);
        permissao.setDataSolicitacao(dataSolicitacao);
        permissao.setInformacaoProjeto(informacaoProjeto);
        permissao.setTipoAcao(tipoAcao);

        // Salvar a permissão no banco de dados
        return permissaoRepository.save(permissao);
    }

    public Permissao aceitarSolicitacao(Long permissaoId, Long adminAprovadorId) {
        // Busca a permissão pela ID
        Permissao permissao = permissaoRepository.findById(permissaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));
    
        // Busca o administrador pelo ID do administrador que fez o pedido
        Adm adminSolicitante = admRepository.findById(permissao.getAdminSolicitanteId())
                .orElseThrow(() -> new IllegalArgumentException("Administrador solicitante não encontrado"));
    
        // Busca o administrador pelo ID do administrador que está aprovando a solicitação
        Adm adminAprovador = admRepository.findById(adminAprovadorId)
                .orElseThrow(() -> new IllegalArgumentException("Administrador aprovador não encontrado"));
    
        // Verifica se a solicitação está pendente
        if (!"Pendente".equals(permissao.getStatusSolicitado())) {
            throw new IllegalStateException("A solicitação já foi processada");
        }
    
        // Converte o JSON informacoesProjeto em um objeto Projeto
        Projeto projeto = null;
        try {
            projeto = objectMapper.readValue(permissao.getInformacaoProjeto(), Projeto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao processar as informações do projeto", e);
        }
    
        // Associa o administrador solicitante ao projeto
        projeto.setAdm(adminSolicitante);
    
        // Salva o projeto no banco de dados
        Projeto projetoSalvo = projetoRepository.save(projeto);
    
        // Define o status da permissão como "Aprovado" e atualiza a data e o projeto relacionado
        permissao.setStatusSolicitado("Aprovado");
        permissao.setDataAprovado(LocalDate.now());
        permissao.setAdm(adminAprovador); // Associa o administrador aprovador como responsável pela aprovação
        permissao.setProjetoId(projetoSalvo.getId()); // Atribui o ID do projeto salvo à permissão
    
        // Salva a permissão atualizada no banco
        return permissaoRepository.save(permissao);
    }    

    public List<Permissao> listarPedidosPendentes() {
        return permissaoRepository.findByStatusSolicitado("Pendente");
    }
}
