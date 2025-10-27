package com.senai.gestaofuncionarios.dto;

import com.senai.gestaofuncionarios.model.Departamento;

public record DepartamentoResponseDTO(
        Long id,
        String nome,
        String sigla,
        Boolean ativo
) {
    public DepartamentoResponseDTO(Departamento depto) {
        this(depto.getId(), depto.getNome(), depto.getSigla(), depto.getAtivo());
    }
}