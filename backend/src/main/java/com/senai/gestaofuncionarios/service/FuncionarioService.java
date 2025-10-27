package com.senai.gestaofuncionarios.service;

import com.senai.gestaofuncionarios.dto.FuncionarioRequestDTO;
import com.senai.gestaofuncionarios.dto.FuncionarioResponseDTO;
import com.senai.gestaofuncionarios.exception.ConflictException;
import com.senai.gestaofuncionarios.exception.ResourceNotFoundException;
import com.senai.gestaofuncionarios.model.Departamento;
import com.senai.gestaofuncionarios.model.Funcionario;
import com.senai.gestaofuncionarios.repository.DepartamentoRepository;
import com.senai.gestaofuncionarios.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Transactional
    public FuncionarioResponseDTO cadastrar(FuncionarioRequestDTO requestDTO) {

        Departamento depto = departamentoRepository.findById(requestDTO.idDepartamento())
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com o ID: " + requestDTO.idDepartamento()));

        if (!depto.getAtivo()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Não é possível cadastrar funcionário em um departamento inativo.");
        }

        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findByEmail(requestDTO.email());
        Funcionario funcionarioParaSalvar;

        if (funcionarioExistente.isPresent()) {
            funcionarioParaSalvar = funcionarioExistente.get();
            if (funcionarioParaSalvar.getAtivo()) {
                throw new ConflictException("E-mail já cadastrado no sistema.");
            } else {
                funcionarioParaSalvar.setNome(requestDTO.nome());
                funcionarioParaSalvar.setCargo(requestDTO.cargo());
                funcionarioParaSalvar.setSalario(requestDTO.salario());
                funcionarioParaSalvar.setDataAdmissao(requestDTO.dataAdmissao());
                funcionarioParaSalvar.setAtivo(true);
                funcionarioParaSalvar.setDepartamento(depto);
            }
        } else {
            funcionarioParaSalvar = new Funcionario();
            funcionarioParaSalvar.setNome(requestDTO.nome());
            funcionarioParaSalvar.setEmail(requestDTO.email());
            funcionarioParaSalvar.setCargo(requestDTO.cargo());
            funcionarioParaSalvar.setSalario(requestDTO.salario());
            funcionarioParaSalvar.setDataAdmissao(requestDTO.dataAdmissao());
            funcionarioParaSalvar.setAtivo(true); // Todo novo cadastro é ativo
            funcionarioParaSalvar.setDepartamento(depto);
        }

        return new FuncionarioResponseDTO(funcionarioRepository.save(funcionarioParaSalvar));
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
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
        return new FuncionarioResponseDTO(funcionario);
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO requestDTO) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));

        if (!funcionario.getAtivo()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Não é possível editar um funcionário inativo.");
        }

        if (requestDTO.salario().compareTo(funcionario.getSalario()) < 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "O salário não pode ser reduzido.");
        }

        if (!funcionario.getEmail().equals(requestDTO.email())) {
            Optional<Funcionario> outroFuncionario = funcionarioRepository.findByEmail(requestDTO.email());
            if (outroFuncionario.isPresent() && outroFuncionario.get().getAtivo()) {
                throw new ConflictException("E-mail já cadastrado no sistema.");
            }
            funcionario.setEmail(requestDTO.email());
        }

        Departamento depto = departamentoRepository.findById(requestDTO.idDepartamento())
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com o ID: " + requestDTO.idDepartamento()));

        if (!depto.getAtivo()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Não é possível mover funcionário para um departamento inativo.");
        }

        funcionario.setNome(requestDTO.nome());
        funcionario.setCargo(requestDTO.cargo());
        funcionario.setSalario(requestDTO.salario());
        funcionario.setDataAdmissao(requestDTO.dataAdmissao());
        funcionario.setDepartamento(depto);

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