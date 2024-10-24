package com.example.api2024.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referenciaProjeto;

    @Column(nullable = false)
    private String empresa;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String objeto;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String coordenador;

    @Column(nullable = false)
    private BigDecimal valor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataTermino;


    @Column
    private String situacao;

    @ManyToOne
    @JoinColumn(name = "idAdm")
    private Adm adm;
}
