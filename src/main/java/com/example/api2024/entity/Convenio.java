package com.example.api2024.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@NoArgsConstructor
@Data
public class Convenio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipoConvenio;
    
    @Column(nullable = false)
    private String objetivo;

    @Column(nullable = false)
    private String instituicao;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate prazo;
    
}