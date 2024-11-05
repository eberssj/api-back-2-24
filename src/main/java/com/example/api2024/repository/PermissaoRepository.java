package com.example.api2024.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api2024.entity.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    List<Permissao> findByStatusSolicitado(String statusSolicitado);
}
