package com.example.api2024.repository;

import com.example.api2024.entity.PermissaoArquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PermissaoArquivoRepository extends JpaRepository<PermissaoArquivo, Long> {
    List<PermissaoArquivo> findByPermissaoId(Long permissaoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PermissaoArquivo pa WHERE pa.permissao.id = :permissaoId")
    void deleteByPermissaoId(@Param("permissaoId") Long permissaoId);
}
