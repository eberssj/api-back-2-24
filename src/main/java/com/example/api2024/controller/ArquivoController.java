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
        if (mimeType == null || mimeType.isEmpty()) {
            mimeType = "application/octet-stream"; // fallback if MIME type is missing
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
                .body(new ByteArrayResource(arquivo.getConteudo()));
    }
}
