package com.example.api2024.controller;

import com.example.api2024.entity.Adm;
import com.example.api2024.entity.Bolsista;
import com.example.api2024.repository.AdmRepository;
import com.example.api2024.repository.BolsistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bolsistas")
public class BolsistaController {

    @Autowired
    private AdmRepository admRepository;

    @Autowired
    private BolsistaRepository bolsistaRepository;

    @PostMapping("/criarBolsista")
    public ResponseEntity<Map<String, String>> criarBolsista(
            @RequestBody Bolsista novoBolsista,
            @RequestParam Long idAdm) {

        // Verificar se o administrador tem permissão
        Optional<Adm> administrador = admRepository.findById(idAdm);
        if (administrador.isEmpty() || !"1".equals(administrador.get().getTipo())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado: Apenas administradores podem criar novos bolsistas."));
        }

        // Validação básica do CPF para evitar duplicidade
        if (bolsistaRepository.findByCpf(novoBolsista.getCpf()).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("message", "Erro: CPF já cadastrado para outro bolsista."));
        }

        // Persistir o novo bolsista no banco de dados
        bolsistaRepository.save(novoBolsista);

        // Retornar uma resposta de sucesso
        return ResponseEntity.ok(Map.of("message", "Bolsista criado com sucesso!"));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Bolsista>> listarBolsistas() {
        List<Bolsista> bolsistas = bolsistaRepository.findAll();
        return ResponseEntity.ok(bolsistas);
    }

    @DeleteMapping("/deletar/{idBolsista}")
    public ResponseEntity<Map<String, String>> deletarBolsista(
            @PathVariable Long idBolsista,
            @RequestParam Long idAdm) {

        // Verificar se o administrador tem permissão
        Optional<Adm> administrador = admRepository.findById(idAdm);
        if (administrador.isEmpty() || !"1".equals(administrador.get().getTipo())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado: Apenas administradores podem excluir bolsistas."));
        }

        // Verificar se o bolsista existe
        Optional<Bolsista> bolsista = bolsistaRepository.findById(idBolsista);
        if (bolsista.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Erro: Bolsista não encontrado."));
        }

        // Excluir o bolsista
        bolsistaRepository.deleteById(idBolsista);
        return ResponseEntity.ok(Map.of("message", "Bolsista excluído com sucesso!"));
    }
}
