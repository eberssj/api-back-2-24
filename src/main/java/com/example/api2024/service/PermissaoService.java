package com.example.api2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.api2024.entity.Permissao;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.PermissaoRepository;
import com.example.api2024.repository.ProjetoRepository;

import java.time.LocalDate;

@Service
public class PermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

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
}
