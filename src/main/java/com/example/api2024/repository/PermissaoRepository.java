package com.example.api2024.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.api2024.entity.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    List<Permissao> findByStatusSolicitado(String statusSolicitado);
    List<Permissao> findByProjetoId(Long projetoId); // Método adicionado
}
