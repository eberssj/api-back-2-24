package com.example.api2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api2024.entity.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
