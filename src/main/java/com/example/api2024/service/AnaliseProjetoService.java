package com.example.api2024.service;

import com.example.api2024.entity.AnaliseProjeto;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.AnaliseProjetoRepository;
import com.example.api2024.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AnaliseProjetoService {

    @Autowired
    private AnaliseProjetoRepository analiseProjetoRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    public AnaliseProjeto salvarAnalise(AnaliseProjeto analise) {
        Projeto projeto = projetoRepository.findById(analise.getProjeto().getId())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        // Atualizar a data de término e o status do projeto
        projeto.setDataTermino(LocalDate.now());
        projeto.setSituacao("Encerrado");
        projetoRepository.save(projeto);

        // Salvar a análise do projeto
        return analiseProjetoRepository.save(analise);
    }
}
