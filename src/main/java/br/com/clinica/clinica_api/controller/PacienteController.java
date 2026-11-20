package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.PacienteResponseDTO;
import br.com.clinica.clinica_api.dto.PacienteUpdateDTO;
import br.com.clinica.clinica_api.model.Paciente;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.clinica.clinica_api.service.PacienteService;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResponseDTO>> listarTodos(@PageableDefault (size = 10, sort = {"nome"}) Pageable paginacao) {
        Page<Paciente> pacientes = pacienteService.listarTodos(paginacao);

        Page<PacienteResponseDTO> pacientePaginas = pacientes.map(PacienteResponseDTO::new);

        return ResponseEntity.ok(pacientePaginas);
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody Paciente paciente) {
        Paciente cadastrarPaciente = pacienteService.cadastrar(paciente);

        return ResponseEntity.ok(new PacienteResponseDTO(cadastrarPaciente));
    }

    //Foi utilizado o @Put e não o @Patch por ser uma atualização cadastral.
    // Somente em casos específicos utilizaremos o @Patch (como excluir um id)
    @PutMapping
    public ResponseEntity<PacienteResponseDTO> atualizar(@RequestBody @Valid PacienteUpdateDTO pacienteDto) {
        Paciente pacienteAtualizado = pacienteService.atualizar(pacienteDto);

        return ResponseEntity.ok(new PacienteResponseDTO(pacienteAtualizado));
    }


}
