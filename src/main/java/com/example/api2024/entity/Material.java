package com.example.api2024.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "idProjeto", nullable = false)
    private Projeto projetoAssociado;

    @Column(nullable = false)
    private Integer quantidadeUsada;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String fornecedor;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Projeto getProjetoAssociado() {
        return projetoAssociado;
    }

    public void setProjetoAssociado(Projeto projetoAssociado) {
        this.projetoAssociado = projetoAssociado;
    }

    public Integer getQuantidadeUsada() {
        return quantidadeUsada;
    }

    public void setQuantidadeUsada(Integer quantidadeUsada) {
        this.quantidadeUsada = quantidadeUsada;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
