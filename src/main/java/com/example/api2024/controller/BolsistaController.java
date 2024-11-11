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

    @PutMapping("/editar/{idBolsista}")
    public ResponseEntity<Map<String, String>> editarBolsista(
            @PathVariable Long idBolsista,
            @RequestBody Bolsista bolsistaAtualizado,
            @RequestParam Long idAdm) {

        // Verificar se o administrador tem permissão
        Optional<Adm> administrador = admRepository.findById(idAdm);
        if (administrador.isEmpty() || !"1".equals(administrador.get().getTipo())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado: Apenas administradores podem editar bolsistas."));
        }

        // Verificar se o bolsista existe
        Optional<Bolsista> bolsistaExistente = bolsistaRepository.findById(idBolsista);
        if (bolsistaExistente.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Erro: Bolsista não encontrado."));
        }

        // Atualizar os dados do bolsista
        Bolsista bolsista = bolsistaExistente.get();
        bolsista.setNome(bolsistaAtualizado.getNome());
        bolsista.setAreaAtuacao(bolsistaAtualizado.getAreaAtuacao());
        bolsista.setProjeto(bolsistaAtualizado.getProjeto());
        bolsista.setConvenio(bolsistaAtualizado.getConvenio());
        bolsista.setCidade(bolsistaAtualizado.getCidade());
        bolsista.setCpf(bolsistaAtualizado.getCpf());
        bolsista.setTelefone(bolsistaAtualizado.getTelefone());
        bolsista.setValorBolsa(bolsistaAtualizado.getValorBolsa());
        bolsista.setDuracaoBolsa(bolsistaAtualizado.getDuracaoBolsa());

        // Salvar o bolsista atualizado no banco de dados
        bolsistaRepository.save(bolsista);

        return ResponseEntity.ok(Map.of("message", "Bolsista editado com sucesso!"));
    }
}
