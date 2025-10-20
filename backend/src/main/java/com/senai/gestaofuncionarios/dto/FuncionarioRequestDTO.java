package com.senai.gestaofuncionarios.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioRequestDTO(
        @NotBlank(message = "O nome não pode conter apenas espaços em branco")
        @Size(min = 3, message = "O nome deve conter no mínimo 3 caracteres")
        String nome,

        @NotBlank(message = "O e-mail não pode estar em branco")
        @Email(message = "O e-mail deve ser válido")
        String email,

        @NotBlank(message = "O cargo não pode estar em branco")
        String cargo,

        @NotNull(message = "O salário é obrigatório")
        @Positive(message = "O salário deve ser positivo e maior que zero")
        BigDecimal salario,

        @NotNull(message = "A data de admissão é obrigatória")
        @PastOrPresent(message = "A data de admissão não pode ser posterior à data atual")
        LocalDate dataAdmissao
) {
}