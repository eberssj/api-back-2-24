package com.example.api2024.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissaoDto {

    private Long id;
    private Long adminSolicitanteId;
    private String statusSolicitado;
    private LocalDate dataSolicitacao;
    private LocalDate dataAprovado;
    private Long admId;
    private Long projetoId;
    private String informacaoProjeto;  // Dados do projeto em JSON
    private String tipoAcao;  // Criação, Edição ou Deleção
}
