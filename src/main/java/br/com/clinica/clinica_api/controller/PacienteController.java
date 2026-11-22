package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.PacienteRequestDTO;
import br.com.clinica.clinica_api.dto.PacienteResponseDTO;
import br.com.clinica.clinica_api.dto.PacienteUpdateDTO;
import br.com.clinica.clinica_api.model.Paciente;
import br.com.clinica.clinica_api.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResponseDTO>> listarTodos(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return ResponseEntity.ok(pacienteService.listarTodos(paginacao).map(PacienteResponseDTO::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new PacienteResponseDTO(pacienteService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid PacienteRequestDTO dto, UriComponentsBuilder uriBuilder) {
        Paciente paciente = pacienteService.cadastrar(dto);
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new PacienteResponseDTO(paciente));
    }

    // PUT: atualização cadastral (nome e telefone)
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PacienteUpdateDTO dto) {
        return ResponseEntity.ok(new PacienteResponseDTO(pacienteService.atualizar(id, dto)));
    }

    // PATCH: alteração parcial -> inativação lógica (não apaga o histórico de consultas)
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<PacienteResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(new PacienteResponseDTO(pacienteService.inativar(id)));
    }
}
