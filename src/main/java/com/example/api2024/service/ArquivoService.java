package com.example.api2024.service;

import com.example.api2024.dto.ArquivoDto;
import com.example.api2024.entity.Arquivo;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.ArquivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;

    // Método atualizado para buscar arquivos por projeto sem filtro de 'aprovado'
    public List<ArquivoDto> getArquivosByProjetoId(Long projetoId) {
        List<Arquivo> arquivos = arquivoRepository.findByProjetoId(projetoId);
        return arquivos.stream()
                .map(arquivo -> new ArquivoDto(
                        arquivo.getId(),
                        arquivo.getNomeArquivo(),
                        arquivo.getTipoDocumento(),
                        arquivo.getProjeto().getId(),
                        arquivo.getTipoArquivo()
                )).collect(Collectors.toList());
    }

    // Método para obter um arquivo por ID
    public Optional<Arquivo> getArquivoById(Long arquivoId) {
        return arquivoRepository.findById(arquivoId);
    }

    // Método para salvar um novo arquivo
    public Arquivo salvarArquivo(MultipartFile file, Projeto projeto, String tipoDocumento) throws IOException {
        if (file != null && !file.isEmpty()) {
            Arquivo arquivo = new Arquivo();
            arquivo.setNomeArquivo(file.getOriginalFilename());
            arquivo.setTipoArquivo(file.getContentType());
            arquivo.setConteudo(file.getBytes());
            arquivo.setTipoDocumento(tipoDocumento);
            arquivo.setProjeto(projeto);
            return arquivoRepository.save(arquivo);
        }
        return null;
    }
}
