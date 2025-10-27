package com.senai.gestaofuncionarios.controller;

import com.senai.gestaofuncionarios.dto.DepartamentoRequestDTO;
import com.senai.gestaofuncionarios.dto.DepartamentoResponseDTO;
import com.senai.gestaofuncionarios.service.DepartamentoService;
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
@RequestMapping("/api/departamentos")
@Tag(name = "Departamentos", description = "API para Gestão de Departamentos")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    @Operation(summary = "Lista todos os departamentos")
    public ResponseEntity<List<DepartamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(departamentoService.listarTodos());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Lista apenas departamentos ativos")
    public ResponseEntity<List<DepartamentoResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(departamentoService.listarAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca departamento por ID")
    public ResponseEntity<DepartamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(departamentoService.buscarPorIdDto(id));
    }

    @PostMapping
    @Operation(summary = "Cria novo departamento")
    public ResponseEntity<DepartamentoResponseDTO> cadastrar(@Valid @RequestBody DepartamentoRequestDTO requestDTO) {
        DepartamentoResponseDTO responseDTO = departamentoService.cadastrar(requestDTO);
        URI location = URI.create(String.format("/api/departamentos/%d", responseDTO.id()));
        return ResponseEntity.created(location).body(responseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza dados do departamento")
    public ResponseEntity<DepartamentoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody DepartamentoRequestDTO requestDTO) {
        return ResponseEntity.ok(departamentoService.atualizar(id, requestDTO));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativa departamento")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        departamentoService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}