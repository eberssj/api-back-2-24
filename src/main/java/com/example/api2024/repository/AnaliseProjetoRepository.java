package com.example.api2024.repository;

import com.example.api2024.entity.AnaliseProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnaliseProjetoRepository extends JpaRepository<AnaliseProjeto, Long> {
}