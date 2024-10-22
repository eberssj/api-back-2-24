package com.example.api2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api2024.entity.Permissao;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.PermissaoRepository;
import com.example.api2024.repository.ProjetoRepository;

@Service
public class PermissaoService {
    
    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    public Permissao criarSolicitacao(Long adminSolicitanteId, Long projetoId) {
        Permissao permissao = new Permissao();
        permissao.setAdminSolicitanteId(adminSolicitanteId);

        // Obtém o objeto Projeto a partir do projetoId
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado com ID: " + projetoId));

        // Define o objeto Projeto na permissão
        permissao.setProjetoId(projeto);

        permissao.setStatusSolicitado("PENDENTE");
        return permissaoRepository.save(permissao);
    }
}
