package com.example.api2024.entity;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissao")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_solicitante_id", nullable = false)
    private Long adminSolicitanteId;

    @Column(name = "status_solicitado", nullable = false, length = 50)
    private String statusSolicitado;

    @Column(name = "data_solicitacao", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataSolicitacao;

    @Column(name = "data_aprovado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAprovado;

    @ManyToOne
    @JoinColumn(name = "id_projeto", referencedColumnName = "id", nullable = true)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "id_adm", referencedColumnName = "id", nullable = true)
    private Adm adm;

    @Lob
    @Column(name = "informacao_projeto", nullable = false, columnDefinition = "TEXT")
    private String informacaoProjeto;

    @Column(name = "tipo_acao", nullable = false, length = 50)
    private String tipoAcao;

    @PrePersist
    public void prePersist() {
        this.dataSolicitacao = LocalDate.now(); // Define a data atual ao persistir
    }

    public void setProjetoId(Long projetoId) {
        this.projeto = new Projeto();
        this.projeto.setId(projetoId);
    }
}
