package com.senai.gestaofuncionarios.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartamentoRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "A sigla é obrigatória")
        String sigla
) {
}