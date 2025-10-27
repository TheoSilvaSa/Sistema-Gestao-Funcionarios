package com.senai.gestaofuncionarios.repository;

import com.senai.gestaofuncionarios.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    boolean existsByNome(String nome);

    List<Departamento> findByAtivoTrue();
}