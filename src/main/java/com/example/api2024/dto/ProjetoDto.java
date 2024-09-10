package com.example.api2024.dto;

import com.example.api2024.entity.Projeto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjetoDto {

    private String referenciaProjeto;

    private String empresa;

    private String objeto;

    private String descricao;

    private String coordenador;

    private double valor;

    private LocalDate dataInicio;

    private LocalDate dataTermino;

    private String propostaRelatorios;

    private String contratos;

    private String artigos;

    private double situacao;

    private Long idAdm;

    public Projeto toEntity() {
        Projeto projeto = new Projeto();
        return projeto;
    }
}
