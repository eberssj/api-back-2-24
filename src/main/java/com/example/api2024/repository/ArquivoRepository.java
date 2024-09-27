package com.example.api2024.repository;

import com.example.api2024.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
    List<Arquivo> findByProjetoId(Long projetoId);
}
