package com.senai.gestaofuncionarios.service;

import com.senai.gestaofuncionarios.dto.DepartamentoRequestDTO;
import com.senai.gestaofuncionarios.dto.DepartamentoResponseDTO;
import com.senai.gestaofuncionarios.exception.ConflictException;
import com.senai.gestaofuncionarios.exception.ResourceNotFoundException;
import com.senai.gestaofuncionarios.model.Departamento;
import com.senai.gestaofuncionarios.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Transactional(readOnly = true)
    public List<DepartamentoResponseDTO> listarTodos() {
        return departamentoRepository.findAll().stream()
                .map(DepartamentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DepartamentoResponseDTO> listarAtivos() {
        return departamentoRepository.findByAtivoTrue().stream()
                .map(DepartamentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    private Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com o ID: " + id));
    }

    @Transactional
    public DepartamentoResponseDTO cadastrar(DepartamentoRequestDTO requestDTO) {
        if (departamentoRepository.existsByNome(requestDTO.nome())) {
            throw new ConflictException("Já existe um departamento com este nome."); // [cite: 16]
        }

        Departamento depto = new Departamento();
        depto.setNome(requestDTO.nome());
        depto.setSigla(requestDTO.sigla());
        depto.setAtivo(true);

        return new DepartamentoResponseDTO(departamentoRepository.save(depto));
    }

    @Transactional
    public DepartamentoResponseDTO atualizar(Long id, DepartamentoRequestDTO requestDTO) {
        Departamento depto = buscarPorId(id);

        if (!depto.getNome().equals(requestDTO.nome()) &&
                departamentoRepository.existsByNome(requestDTO.nome())) {
            throw new ConflictException("Já existe um departamento com este nome.");
        }

        depto.setNome(requestDTO.nome());
        depto.setSigla(requestDTO.sigla());

        return new DepartamentoResponseDTO(departamentoRepository.save(depto));
    }

    @Transactional
    public void inativar(Long id) {
        Departamento depto = buscarPorId(id);
        depto.setAtivo(false);
        departamentoRepository.save(depto);
    }

    @Transactional(readOnly = true)
    public DepartamentoResponseDTO buscarPorIdDto(Long id) {
        return new DepartamentoResponseDTO(buscarPorId(id));
    }
}