package com.example.api2024.controller;

import com.example.api2024.entity.Material;
import com.example.api2024.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // Create
    @PostMapping
    public ResponseEntity<Map<String, String>> criarMaterial(
            @RequestBody Material novoMaterial,
            @RequestParam Long idAdm) {

        
        // Persistir o novo material no banco de dados
        materialService.criarMaterial(novoMaterial);

        // Retornar uma resposta de sucesso
        return ResponseEntity.ok(Map.of("message", "Material criado com sucesso!"));
    }

    // Read All
    @GetMapping
    public ResponseEntity<List<Material>> listarMateriais() {
        List<Material> materiais = materialService.listarMateriais();
        return ResponseEntity.ok(materiais);
    }

 
    // Update
    @PutMapping("/editar/{id}")
    public ResponseEntity<Map<String, String>> editarMaterial(
            @PathVariable Long id,
            @RequestBody Material materialAtualizado,
            @RequestParam Long idAdm) {

       
        // Verificar se o material existe e atualizá-lo
        Material materialEditado = materialService.atualizarMaterial(id, materialAtualizado);
        if (materialEditado != null) {
            return ResponseEntity.ok(Map.of("message", "Material editado com sucesso!"));
        }
        return ResponseEntity.status(404).body(Map.of("message", "Material não encontrado."));
    }

    // Delete
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Map<String, String>> deletarMaterial(
            @PathVariable Long id,
            @RequestParam Long idAdm) {

        
        boolean deletado = materialService.deletarMaterial(id);
        if (deletado) {
            return ResponseEntity.ok(Map.of("message", "Material excluído com sucesso!"));
        }
        return ResponseEntity.status(404).body(Map.of("message", "Material não encontrado."));
    }
}
