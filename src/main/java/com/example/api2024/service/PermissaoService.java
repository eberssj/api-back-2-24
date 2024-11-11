package com.example.api2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api2024.entity.Adm;
import com.example.api2024.entity.Arquivo;
import com.example.api2024.entity.Permissao;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.AdmRepository;
import com.example.api2024.repository.ArquivoRepository;
import com.example.api2024.repository.PermissaoRepository;
import com.example.api2024.repository.ProjetoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final AdmRepository admRepository;
    private final ProjetoRepository projetoRepository;
    private final ArquivoRepository arquivoRepository;
    private final ObjectMapper objectMapper;

    // Criar uma nova solicitação de criação de projeto
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

    // Solicitar a edição de um projeto existente
    public Permissao solicitarEdicaoProjeto(Long adminSolicitanteId, String statusSolicitado, Long projetoId,
                                            String informacaoProjeto, String tipoAcao) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com ID: " + projetoId));

        Permissao permissao = new Permissao();
        permissao.setAdminSolicitanteId(adminSolicitanteId);
        permissao.setStatusSolicitado(statusSolicitado);
        permissao.setInformacaoProjeto(informacaoProjeto);
        permissao.setTipoAcao(tipoAcao);
        permissao.setProjeto(projeto);
        permissao.setDataSolicitacao(LocalDate.now());

        return permissaoRepository.save(permissao);
    }

    // Aceitar uma solicitação de criação, edição ou exclusão
    @Transactional
    public Permissao aceitarSolicitacao(Long permissaoId, Long adminAprovadorId) {
        Permissao permissao = permissaoRepository.findById(permissaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada"));

        Adm adminAprovador = admRepository.findById(adminAprovadorId)
                .orElseThrow(() -> new IllegalArgumentException("Administrador aprovador não encontrado"));

        if (!"Pendente".equals(permissao.getStatusSolicitado())) {
            throw new IllegalStateException("A solicitação já foi processada");
        }

        Projeto projeto = null;
        if (permissao.getInformacaoProjeto() != null) {
            try {
                projeto = objectMapper.readValue(permissao.getInformacaoProjeto(), Projeto.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Erro ao processar as informações do projeto", e);
            }
        }

        switch (permissao.getTipoAcao()) {
            case "Criacao":
                if (projeto != null) {
                    projeto.setAdm(adminAprovador);
                    projeto.setSituacao(projeto.getDataTermino().isAfter(LocalDate.now()) ? "Em Andamento" : "Encerrado");
                    Projeto novoProjeto = projetoRepository.save(projeto);
                    permissao.setProjeto(novoProjeto);
                }
                break;

            case "Editar":
                if (permissao.getProjeto() != null) {
                    Projeto projetoExistente = projetoRepository.findById(permissao.getProjeto().getId())
                            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

                    if (projeto != null) {
                        projetoExistente.setReferenciaProjeto(projeto.getReferenciaProjeto());
                        projetoExistente.setEmpresa(projeto.getEmpresa());
                        projetoExistente.setObjeto(projeto.getObjeto());
                        projetoExistente.setDescricao(projeto.getDescricao());
                        projetoExistente.setCoordenador(projeto.getCoordenador());
                        projetoExistente.setOcultarValor(projeto.getOcultarValor());
                        projetoExistente.setOcultarEmpresa(projeto.getOcultarEmpresa());
                        projetoExistente.setValor(projeto.getValor());
                        projetoExistente.setDataInicio(projeto.getDataInicio());
                        projetoExistente.setDataTermino(projeto.getDataTermino());
                        projetoExistente.setSituacao(projeto.getDataTermino().isAfter(LocalDate.now()) ? "Em Andamento" : "Encerrado");
                        projetoExistente.setAdm(adminAprovador);
                        projetoRepository.save(projetoExistente);
                        permissao.setProjeto(projetoExistente);
                    }
                }
                break;

            case "Exclusao":
                Long projetoId = permissao.getProjeto() != null ? permissao.getProjeto().getId() : projeto != null ? projeto.getId() : null;

                if (projetoId != null) {
                    Projeto projetoParaExcluir = projetoRepository.findById(projetoId)
                            .orElseThrow(() -> new RuntimeException("Projeto não encontrado para exclusão"));

                    // Excluir todas as permissões relacionadas ao projeto
                    permissaoRepository.deleteByProjetoId(projetoParaExcluir.getId());

                    // Excluir arquivos relacionados
                    arquivoRepository.deleteByProjetoId(projetoParaExcluir.getId());

                    // Excluir o projeto
                    projetoRepository.delete(projetoParaExcluir);
                } else {
                    throw new RuntimeException("ID do projeto não encontrado para exclusão");
                }
                break;


            default:
                throw new IllegalArgumentException("Tipo de ação desconhecido: " + permissao.getTipoAcao());
        }

        permissao.setStatusSolicitado("Aprovado");
        permissao.setDataAprovado(LocalDate.now());
        permissao.setAdm(adminAprovador);
        return permissaoRepository.save(permissao);
    }





    // Listar todas as solicitações pendentes
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