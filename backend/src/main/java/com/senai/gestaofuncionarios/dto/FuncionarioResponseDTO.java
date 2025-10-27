package com.senai.gestaofuncionarios.dto;

import com.senai.gestaofuncionarios.model.Funcionario;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioResponseDTO(
        Long id,
        String nome,
        String email,
        String cargo,
        BigDecimal salario,
        LocalDate dataAdmissao,
        Boolean ativo,
        Long idDepartamento,
        String nomeDepartamento,
        Boolean ativoDepartamento
) {
    public FuncionarioResponseDTO(Funcionario funcionario) {
        this(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getCargo(),
                funcionario.getSalario(),
                funcionario.getDataAdmissao(),
                funcionario.getAtivo(),
                funcionario.getDepartamento().getId(),
                funcionario.getDepartamento().getNome(),
                funcionario.getDepartamento().getAtivo()
        );
    }
}