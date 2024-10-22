package com.example.api2024.service;

import com.example.api2024.dto.ArquivoDto;
import com.example.api2024.entity.Arquivo;
import com.example.api2024.repository.ArquivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArquivoService {

    @Autowired
    private ArquivoRepository arquivoRepository;

    // Fetch all files by project ID
    public List<ArquivoDto> getArquivosByProjetoId(Long projetoId) {
        List<Arquivo> arquivos = arquivoRepository.findByProjetoId(projetoId);
        return arquivos.stream().map(arquivo -> new ArquivoDto(
                arquivo.getId(),
                arquivo.getNomeArquivo(),
                arquivo.getTipoDocumento(),
                arquivo.getProjeto().getId(),
                arquivo.getTipoArquivo()
        )).collect(Collectors.toList());
    }

    // Get single file by ID
    public Optional<Arquivo> getArquivoById(Long arquivoId) {
        return arquivoRepository.findById(arquivoId);
    }
}
