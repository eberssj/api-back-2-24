package com.example.api2024.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import com.example.api2024.entity.Permissao;
import org.springframework.data.repository.query.Param;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    List<Permissao> findByStatusSolicitado(String statusSolicitado);
    List<Permissao> findByProjetoId(Long projetoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Permissao p WHERE p.projeto.id = :projetoId")
    void deleteByProjetoId(@Param("projetoId") Long projetoId);
}
