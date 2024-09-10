package com.example.api2024.entity;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Adm {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Column
    private String tipo;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataCadastro;

    @Column
    private String ativo;
}
