package com.example.api2024.repository;

import com.example.api2024.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
    List<Arquivo> findByProjetoId(Long projetoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Arquivo a WHERE a.projeto.id = :projetoId")
    void deleteByProjetoId(@Param("projetoId") Long projetoId);
}
