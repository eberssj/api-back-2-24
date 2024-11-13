package com.example.api2024.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.api2024.entity.Bolsista;

public interface BolsistaRepository extends JpaRepository<Bolsista, Long> {
    Optional<Bolsista> findByCpf(String cpf);

    // Valor total pago em bolsas
    @Query("SELECT SUM(b.valorBolsa) FROM Bolsista b")
    BigDecimal totalValorBolsa();

    // Quantidade de bolsistas por área de atuação
    @Query("SELECT b.areaAtuacao, COUNT(b) FROM Bolsista b GROUP BY b.areaAtuacao")
    List<Object[]> countBolsistasPorArea();
}