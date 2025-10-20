package com.senai.gestaofuncionarios.service;

import com.senai.gestaofuncionarios.dto.FuncionarioRequestDTO;
import com.senai.gestaofuncionarios.dto.FuncionarioResponseDTO;
import com.senai.gestaofuncionarios.exception.ConflictException;
import com.senai.gestaofuncionarios.exception.ResourceNotFoundException;
import com.senai.gestaofuncionarios.model.Funcionario;
import com.senai.gestaofuncionarios.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Transactional
    public FuncionarioResponseDTO cadastrar(FuncionarioRequestDTO requestDTO) {
        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findByEmail(requestDTO.email());

        if (funcionarioExistente.isPresent()) {
            Funcionario f = funcionarioExistente.get();
            if (f.getAtivo()) {
                throw new ConflictException("E-mail já cadastrado no sistema.");
            } else {
                f.setNome(requestDTO.nome());
                f.setCargo(requestDTO.cargo());
                f.setSalario(requestDTO.salario());
                f.setDataAdmissao(requestDTO.dataAdmissao());
                f.setAtivo(true);
                return new FuncionarioResponseDTO(funcionarioRepository.save(f));
            }
        }

        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome(requestDTO.nome());
        novoFuncionario.setEmail(requestDTO.email());
        novoFuncionario.setCargo(requestDTO.cargo());
        novoFuncionario.setSalario(requestDTO.salario());
        novoFuncionario.setDataAdmissao(requestDTO.dataAdmissao());
        novoFuncionario.setAtivo(true);

        return new FuncionarioResponseDTO(funcionarioRepository.save(novoFuncionario));
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listar(String cargo, Boolean ativo) {
        List<Funcionario> funcionarios;

        if (cargo != null || ativo != null) {
            funcionarios = funcionarioRepository.findByCargoAndAtivoOrderByNomeAsc(cargo, ativo);
        } else {
            funcionarios = funcionarioRepository.findAllByOrderByNomeAsc();
        }

        return funcionarios.stream()
                .map(FuncionarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id)); // [cite: 81]
        return new FuncionarioResponseDTO(funcionario);
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO requestDTO) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));

        if (!funcionario.getAtivo()) {
            throw new org.springframework.web.client.HttpClientErrorException(org.springframework.http.HttpStatus.BAD_REQUEST, "Não é possível editar um funcionário inativo.");
        }

        if (requestDTO.salario().compareTo(funcionario.getSalario()) < 0) {
            throw new org.springframework.web.client.HttpClientErrorException(org.springframework.http.HttpStatus.BAD_REQUEST, "O salário não pode ser reduzido.");
        }

        if (!funcionario.getEmail().equals(requestDTO.email())) {
            Optional<Funcionario> outroFuncionario = funcionarioRepository.findByEmail(requestDTO.email());
            if (outroFuncionario.isPresent() && outroFuncionario.get().getAtivo()) {
                throw new ConflictException("E-mail já cadastrado no sistema.");
            }
            funcionario.setEmail(requestDTO.email());
        }

        funcionario.setNome(requestDTO.nome());
        funcionario.setCargo(requestDTO.cargo());
        funcionario.setSalario(requestDTO.salario());
        funcionario.setDataAdmissao(requestDTO.dataAdmissao());

        return new FuncionarioResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional
    public void inativar(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));

        funcionario.setAtivo(false);
        funcionarioRepository.save(funcionario);
    }
}