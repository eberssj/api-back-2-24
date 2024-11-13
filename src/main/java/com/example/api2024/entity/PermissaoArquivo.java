package com.example.api2024.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class PermissaoArquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] conteudo;

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String tipoArquivo;

    @Column(nullable = false)
    private String tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "permissao_id", nullable = false)
    private Permissao permissao;

    @Column(nullable = false)
    private LocalDate dataUpload;
}
