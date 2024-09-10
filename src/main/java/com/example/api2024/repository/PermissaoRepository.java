package com.example.api2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api2024.entity.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    
}
