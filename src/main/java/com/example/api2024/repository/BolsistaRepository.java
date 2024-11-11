package com.example.api2024.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api2024.entity.Bolsista;

public interface BolsistaRepository extends JpaRepository<Bolsista, Long> {
    Optional<Bolsista> findByCpf(String cpf);
}