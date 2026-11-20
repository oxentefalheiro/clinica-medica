package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.AgendamentoRequestDTO;
import br.com.clinica.clinica_api.dto.AgendamentoResponseDTO;
import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.model.Agendamento;
import br.com.clinica.clinica_api.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {
    private final AgendamentoService agendamentoService;
    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarTodos(@PageableDefault(size = 10, sort = {"dataConsulta"}) Pageable paginacao){
        Page<Agendamento> agendamento = agendamentoService.listarTodos(paginacao);

        Page <AgendamentoResponseDTO> agendamentoPaginas = agendamento.map(AgendamentoResponseDTO::new);
        return ResponseEntity.ok(agendamentoPaginas);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarPorStatus(@PathVariable StatusAgendamento status, @PageableDefault(size = 10, sort ={"dataConsulta"}) Pageable paginacao) {
        Page<Agendamento> agendamentosStatus = agendamentoService.listarPorStatus(status, paginacao);

        return ResponseEntity.ok(agendamentosStatus.map(AgendamentoResponseDTO::new));
    }

    @GetMapping("/data/{dataConsulta}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarDiaConsulta (@PathVariable LocalDate dataConsulta, @PageableDefault(size = 10, sort = {"dataConsulta"}) Pageable paginacao) {
        Page<Agendamento> dataAgendada = agendamentoService.listarDiaConsulta(dataConsulta, paginacao);

        return ResponseEntity.ok(dataAgendada.map(AgendamentoResponseDTO::new));
    }

    @GetMapping("/hora/{horaConsulta}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarHoraConsulta (@PathVariable LocalTime horaConsulta, @PageableDefault(size = 10, sort = {"dataConsulta"}) Pageable paginacao) {
        Page<Agendamento> horaAgendada = agendamentoService.listarPorHoraConsulta(horaConsulta, paginacao);

        return ResponseEntity.ok(horaAgendada.map(AgendamentoResponseDTO::new));
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> agendar(@RequestBody @Valid AgendamentoRequestDTO agendamentoDto) {
        Agendamento agendarConsulta = agendamentoService.agendar(agendamentoDto);

        return ResponseEntity.ok(new AgendamentoResponseDTO(agendarConsulta));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(@PathVariable Long id) {
        Agendamento cancelarAgendamento = agendamentoService.cancelar(id);

        return ResponseEntity.ok(new AgendamentoResponseDTO(cancelarAgendamento));
    }


}
