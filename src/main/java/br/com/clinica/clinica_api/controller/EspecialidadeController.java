package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.EspecialidadeRequestDTO;
import br.com.clinica.clinica_api.dto.EspecialidadeResponseDTO;
import br.com.clinica.clinica_api.model.Especialidade;
import br.com.clinica.clinica_api.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/especialidade")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    public EspecialidadeController(EspecialidadeService especialidadeService) {
        this.especialidadeService = especialidadeService;
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadeResponseDTO>> listarTodas() {
        List<EspecialidadeResponseDTO> lista = especialidadeService.listarTodas().stream()
                .map(EspecialidadeResponseDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new EspecialidadeResponseDTO(especialidadeService.buscarPorId(id)));
    }

    // 201 Created + cabeçalho Location com o endereço do novo recurso
    @PostMapping
    public ResponseEntity<EspecialidadeResponseDTO> cadastrar(@RequestBody @Valid EspecialidadeRequestDTO dto, UriComponentsBuilder uriBuilder) {
        Especialidade especialidade = especialidadeService.cadastrar(dto);
        URI uri = uriBuilder.path("/especialidade/{id}").buildAndExpand(especialidade.getId()).toUri();
        return ResponseEntity.created(uri).body(new EspecialidadeResponseDTO(especialidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EspecialidadeRequestDTO dto) {
        return ResponseEntity.ok(new EspecialidadeResponseDTO(especialidadeService.atualizar(id, dto)));
    }

    // 204 No Content: exclusão sem corpo de resposta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        especialidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
