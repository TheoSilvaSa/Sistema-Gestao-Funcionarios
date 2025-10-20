package com.senai.gestaofuncionarios.controller;

import com.senai.gestaofuncionarios.dto.FuncionarioRequestDTO;
import com.senai.gestaofuncionarios.dto.FuncionarioResponseDTO;
import com.senai.gestaofuncionarios.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@Tag(name = "Funcionários", description = "API para Gestão de Funcionários")
@CrossOrigin(origins = "http://localhost:4200")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    @Operation(summary = "Cadastra novo funcionário")
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@Valid @RequestBody FuncionarioRequestDTO requestDTO) {
        FuncionarioResponseDTO responseDTO = funcionarioService.cadastrar(requestDTO);
        URI location = URI.create(String.format("/api/funcionarios/%d", responseDTO.id()));
        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lista todos ou filtra por cargo/status")
    public ResponseEntity<List<FuncionarioResponseDTO>> listar(
            @RequestParam(required = false) String cargo, // [cite: 24]
            @RequestParam(required = false) Boolean ativo // [cite: 25]
    ) {
        return ResponseEntity.ok(funcionarioService.listar(cargo, ativo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna funcionário por ID")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza funcionário existente")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FuncionarioRequestDTO requestDTO) {
        return ResponseEntity.ok(funcionarioService.atualizar(id, requestDTO));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativa funcionário")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        funcionarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}