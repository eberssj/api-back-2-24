package com.example.api2024.entity;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column
    private String objeto;

    @Column
    private String descricao;

    @Column(nullable = false)
    private String coordenador;

    @Column(nullable = false)
    private double valor;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataInicio;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataTermino;

    @Column
    private String propostaRelatorios;

    @Column
    private String contratos;

    @Column
    private String artigos;

    @Column
    private double situacao;

    @ManyToOne
    @JoinColumn(name = "idAdm")
    private Adm administrador;
}