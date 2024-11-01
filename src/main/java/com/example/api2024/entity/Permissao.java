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
@Table(name = "permissao")  // Define o nome da tabela no banco
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_solicitante_id", nullable = false)
    private Long adminSolicitanteId;

    @Column(name = "status_solicitado", nullable = false, length = 50)
    private String statusSolicitado;  // Ex.: Pendente, Aprovado, Rejeitado

    @Column(name = "data_solicitacao", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")  // Para formatação ao inserir
    @JsonFormat(pattern = "dd-MM-yyyy")  // Para JSON de entrada e saída
    private LocalDate dataSolicitacao;

    @Column(name = "data_aprovado", nullable = true)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataAprovado;

    @ManyToOne(optional = true)  // Projeto pode ser nulo
    @JoinColumn(name = "id_projeto", referencedColumnName = "id")
    private Projeto projeto;

    @ManyToOne(optional = true)  // Adm pode ser nulo
    @JoinColumn(name = "id_adm", referencedColumnName = "id")
    private Adm adm;

    @Lob  // Permite armazenar grandes textos
    @Column(name = "informacao_projeto", nullable = false, columnDefinition = "TEXT")
    private String informacaoProjeto;  // Armazena os dados em JSON (texto)

    @Column(name = "tipo_acao", nullable = false, length = 50)
    private String tipoAcao;  // Ex.: Criação, Edição, Deleção
}
