package com.example.api2024.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String nomeArquivo;

    @Column(nullable = false)
    private String tipoArquivo;

    @Lob
    @Column(name = "conteudo", columnDefinition = "LONGBLOB")
    private byte[] conteudo;

    @Column(nullable = false)
    private String tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = true) // Permitir nulo durante a solicitação
    private Projeto projeto;
}

