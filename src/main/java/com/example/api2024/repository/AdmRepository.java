package com.example.api2024.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api2024.entity.Adm;

public interface AdmRepository extends JpaRepository<Adm, Long> {

    Optional<Adm> findByEmail(String email);
    boolean existsByEmail(String email);
    
}
