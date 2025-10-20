package com.senai.gestaofuncionarios.repository;

import com.senai.gestaofuncionarios.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByEmail(String email);

    @Query("SELECT f FROM Funcionario f WHERE " +
            "(:cargo IS NULL OR f.cargo = :cargo) AND " +
            "(:ativo IS NULL OR f.ativo = :ativo) " +
            "ORDER BY f.nome ASC")
    List<Funcionario> findByCargoAndAtivoOrderByNomeAsc(
            @Param("cargo") String cargo,
            @Param("ativo") Boolean ativo
    );

    List<Funcionario> findAllByOrderByNomeAsc();
}