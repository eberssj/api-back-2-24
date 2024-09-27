package com.example.api2024.dto;

public class ArquivoDto {

    private Long id;
    private String nomeArquivo;
    private String tipoDocumento;
    private Long projetoId;
    private String tipoArquivo;
    private byte[] conteudo;

    public ArquivoDto() {
    }

    public ArquivoDto(Long id, String nomeArquivo, String tipoDocumento, Long projetoId, String tipoArquivo) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.tipoDocumento = tipoDocumento;
        this.projetoId = projetoId;
        this.tipoArquivo = tipoArquivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(Long projetoId) {
        this.projetoId = projetoId;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }
}
