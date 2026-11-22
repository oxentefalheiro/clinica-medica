package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.MedicoRequestDTO;
import br.com.clinica.clinica_api.dto.MedicoResponseDTO;
import br.com.clinica.clinica_api.dto.MedicoUpdateDTO;
import br.com.clinica.clinica_api.model.Medico;
import br.com.clinica.clinica_api.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // Lista 10 médicos por página, em ordem alfabética
    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listarTodos(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return ResponseEntity.ok(medicoService.listarTodos(paginacao).map(MedicoResponseDTO::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new MedicoResponseDTO(medicoService.buscarPorId(id)));
    }

    @GetMapping("/especialidade/{especialidadeId}")
    public ResponseEntity<Page<MedicoResponseDTO>> listarPorEspecialidade(@PathVariable Long especialidadeId,
                                                                          @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return ResponseEntity.ok(medicoService.listarPorEspecialidade(especialidadeId, paginacao).map(MedicoResponseDTO::new));
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> cadastrar(@RequestBody @Valid MedicoRequestDTO dto, UriComponentsBuilder uriBuilder) {
        Medico medico = medicoService.cadastrar(dto);
        URI uri = uriBuilder.path("/medico/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new MedicoResponseDTO(medico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid MedicoUpdateDTO dto) {
        return ResponseEntity.ok(new MedicoResponseDTO(medicoService.atualizar(id, dto)));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<MedicoResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(new MedicoResponseDTO(medicoService.inativar(id)));
    }
}
