package com.example.api2024.dto;

import java.time.LocalDate;

public class PermissaoDto {

    private Long id;
    private Long adminSolicitanteId;
    private String statusSolicitado;
    private LocalDate dataSolicitacao;
    private LocalDate dataAprovado;
    private Long admId;
    private Long projetoId;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminSolicitanteId() {
        return adminSolicitanteId;
    }

    public void setAdminSolicitanteId(Long adminSolicitanteId) {
        this.adminSolicitanteId = adminSolicitanteId;
    }

    public String getStatusSolicitado() {
        return statusSolicitado;
    }

    public void setStatusSolicitado(String statusSolicitado) {
        this.statusSolicitado = statusSolicitado;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDate getDataAprovado() {
        return dataAprovado;
    }

    public void setDataAprovado(LocalDate dataAprovado) {
        this.dataAprovado = dataAprovado;
    }

    public Long getAdmId() {
        return admId;
    }

    public void setAdmId(Long admId) {
        this.admId = admId;
    }

    public Long getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(Long projetoId) {
        this.projetoId = projetoId;
    }
}
