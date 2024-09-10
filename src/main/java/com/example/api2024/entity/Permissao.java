package com.example.api2024.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Permissao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String permissao;

    @Column
    private String statusSolicitado;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataSolicitacao;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataAprovado;

    @ManyToOne
    @JoinColumn(name = "id_adm")
    private Adm adm;

    @ManyToOne
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;
}
