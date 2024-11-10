package com.example.api2024.repository;

import com.example.api2024.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
    List<Arquivo> findByProjetoId(Long projetoId);
    List<Arquivo> findByProjetoIdAndAprovadoTrue(Long projetoId);
    List<Arquivo> findByProjetoIdAndAprovadoFalse(Long projetoId);
}
