package com.example.api2024.service;

import com.example.api2024.entity.Material;
import com.example.api2024.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    // Create
    public Material criarMaterial(Material material) {
        return materialRepository.save(material);
    }

    // Read by ID
    public Optional<Material> obterMaterialPorId(Long id) {
        return materialRepository.findById(id);
    }

    // Read All
    public List<Material> listarMateriais() {
        return materialRepository.findAll();
    }

    // Update
    public Material atualizarMaterial(Long id, Material materialAtualizado) {
        Optional<Material> materialExistente = materialRepository.findById(id);
        if (materialExistente.isPresent()) {
            Material material = materialExistente.get();
            material.setNome(materialAtualizado.getNome());
            material.setProjetoAssociado(materialAtualizado.getProjetoAssociado());
            material.setQuantidadeUsada(materialAtualizado.getQuantidadeUsada());
            material.setValor(materialAtualizado.getValor());
            material.setFornecedor(materialAtualizado.getFornecedor());
            material.setDescricao(materialAtualizado.getDescricao());
            return materialRepository.save(material);
        }
        return null;
    }

    // Delete
    public boolean deletarMaterial(Long id) {
        if (materialRepository.existsById(id)) {
            materialRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
