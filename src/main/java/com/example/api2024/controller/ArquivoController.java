package com.example.api2024.controller;

import com.example.api2024.dto.ArquivoDto;
import com.example.api2024.entity.Arquivo;
import com.example.api2024.service.ArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arquivos")
public class ArquivoController {

    @Autowired
    private ArquivoService arquivoService;

    // Endpoint to retrieve all files associated with a project
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<ArquivoDto>> getArquivosByProjetoId(@PathVariable Long projetoId) {
        List<ArquivoDto> arquivos = arquivoService.getArquivosByProjetoId(projetoId);
        return ResponseEntity.ok(arquivos);
    }

    // Download a file by its ID
    @GetMapping("/download/{arquivoId}")
    public ResponseEntity<ByteArrayResource> downloadArquivo(@PathVariable Long arquivoId) {
        Arquivo arquivo = arquivoService.getArquivoById(arquivoId)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

        String mimeType = arquivo.getTipoArquivo();

        // Se o tipo MIME estiver vazio ou for "pdf", defina como application/pdf
        if (mimeType == null || mimeType.isEmpty()) {
            mimeType = "application/pdf"; // ou "application/octet-stream" se preferir
        } else if (mimeType.equalsIgnoreCase("pdf")) {
            mimeType = "application/pdf"; // Certifique-se de que "pdf" é tratado como application/pdf
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
                .body(new ByteArrayResource(arquivo.getConteudo()));
    }
} // Certifique-se de que a classe está fechada corretamente aqui
