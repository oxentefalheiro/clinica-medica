package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.AgendamentoRequestDTO;
import br.com.clinica.clinica_api.dto.AgendamentoResponseDTO;
import br.com.clinica.clinica_api.dto.ReagendamentoRequestDTO;
import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.model.Agendamento;
import br.com.clinica.clinica_api.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarTodos(@PageableDefault(size = 10, sort = {"dataConsulta", "horaConsulta"}) Pageable paginacao) {
        return ResponseEntity.ok(agendamentoService.listarTodos(paginacao).map(AgendamentoResponseDTO::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new AgendamentoResponseDTO(agendamentoService.buscarPorId(id)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarPorStatus(@PathVariable StatusAgendamento status,
                                                                        @PageableDefault(size = 10, sort = {"dataConsulta"}) Pageable paginacao) {
        return ResponseEntity.ok(agendamentoService.listarPorStatus(status, paginacao).map(AgendamentoResponseDTO::new));
    }

    @GetMapping("/data/{dataConsulta}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarDiaConsulta(@PathVariable LocalDate dataConsulta,
                                                                          @PageableDefault(size = 10, sort = {"horaConsulta"}) Pageable paginacao) {
        return ResponseEntity.ok(agendamentoService.listarDiaConsulta(dataConsulta, paginacao).map(AgendamentoResponseDTO::new));
    }

    @GetMapping("/hora/{horaConsulta}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarHoraConsulta(@PathVariable LocalTime horaConsulta,
                                                                           @PageableDefault(size = 10, sort = {"dataConsulta"}) Pageable paginacao) {
        return ResponseEntity.ok(agendamentoService.listarPorHoraConsulta(horaConsulta, paginacao).map(AgendamentoResponseDTO::new));
    }

    // RF05 - Agenda do médico no dia. Ex.: GET /agendamento/medico/1?data=2026-12-01
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> agendaDoMedico(@PathVariable Long medicoId, @RequestParam LocalDate data) {
        return ResponseEntity.ok(agendamentoService.agendaDoMedico(medicoId, data).stream().map(AgendamentoResponseDTO::new).toList());
    }

    // RF04 - Horários livres. Ex.: GET /agendamento/horarios-livres?medicoId=1&data=2026-12-01
    @GetMapping("/horarios-livres")
    public ResponseEntity<List<LocalTime>> horariosLivres(@RequestParam Long medicoId, @RequestParam LocalDate data) {
        return ResponseEntity.ok(agendamentoService.horariosLivres(medicoId, data));
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> agendar(@RequestBody @Valid AgendamentoRequestDTO dto, UriComponentsBuilder uriBuilder) {
        Agendamento agendamento = agendamentoService.agendar(dto);
        URI uri = uriBuilder.path("/agendamento/{id}").buildAndExpand(agendamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new AgendamentoResponseDTO(agendamento));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(new AgendamentoResponseDTO(agendamentoService.confirmar(id)));
    }

    @PatchMapping("/{id}/realizar")
    public ResponseEntity<AgendamentoResponseDTO> realizar(@PathVariable Long id) {
        return ResponseEntity.ok(new AgendamentoResponseDTO(agendamentoService.realizar(id)));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(new AgendamentoResponseDTO(agendamentoService.cancelar(id)));
    }

    // Reagendamento transacional: devolve a NOVA consulta (a antiga fica CANCELADA)
    @PatchMapping("/{id}/reagendar")
    public ResponseEntity<AgendamentoResponseDTO> reagendar(@PathVariable Long id, @RequestBody @Valid ReagendamentoRequestDTO dto) {
        return ResponseEntity.ok(new AgendamentoResponseDTO(agendamentoService.reagendar(id, dto)));
    }
}
