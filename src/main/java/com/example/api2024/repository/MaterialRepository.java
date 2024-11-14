package com.example.api2024.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.api2024.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByNome(String nome);
}
