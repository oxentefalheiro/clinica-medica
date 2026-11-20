package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.dto.AgendamentoRequestDTO;
import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.model.Agendamento;
import br.com.clinica.clinica_api.model.Medico;
import br.com.clinica.clinica_api.model.Paciente;
import br.com.clinica.clinica_api.repository.AgendamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PacienteService pacienteService, MedicoService medicoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Agendamento com o ID " + id + " não encontrado."));
    }

    public Page<Agendamento> listarTodos(Pageable paginacao) {
        return agendamentoRepository.findAll(paginacao);
    }

    public Page<Agendamento> listarPorStatus(StatusAgendamento status, Pageable paginacao){
        return agendamentoRepository.findByStatusAgendamento(status, paginacao);
    }

    public Page<Agendamento> listarPorHoraConsulta (LocalTime horaConsulta, Pageable paginacao) {
        return agendamentoRepository.findByHoraConsulta(horaConsulta, paginacao);
    }

    public Page<Agendamento> listarDiaConsulta (LocalDate dataConsulta, Pageable paginacao) {
        return agendamentoRepository.findByDataConsulta(dataConsulta, paginacao);
    }

    @Transactional
    public Agendamento agendar(AgendamentoRequestDTO agendamentoDto) {
        // 1. Pegamos os IDs dos objetos para buscá-los no B.D
        Paciente pacienteExistente = pacienteService.buscarPorId(agendamentoDto.getPacienteId());
        Medico medicoExistente = medicoService.buscarPorId(agendamentoDto.getMedicoId());

        // 2. Valida horário de funcionamento (Segunda a Sábado, das 07:00 às 19:00)
        boolean domingo = agendamentoDto.getDataConsulta().getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean antesDaAbertura = agendamentoDto.getHoraConsulta().getHour() < 7;
        boolean depoisDoEncerramento = agendamentoDto.getHoraConsulta().getHour() > 18; // última consulta às 18h

        if (domingo || antesDaAbertura || depoisDoEncerramento) {
            throw new RuntimeException("Consulta fora do horário de funcionamento da clínica (Segunda a Sábado, das 07:00 às 19:00).");
        }

        // 3. Verifica se o médico já está ocupado nesse dia e hora
        boolean horarioOcupado = agendamentoRepository.existsByMedicoAndHoraConsultaAndDataConsulta(
                medicoExistente, agendamentoDto.getHoraConsulta(), agendamentoDto.getDataConsulta());

        if (horarioOcupado) {
            throw new RuntimeException("Dia e horário solicitados já possuem consulta marcada.");
        }

        // 4. Monta a Entidade Agendamento com os dados validados
        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setPaciente(pacienteExistente);
        novoAgendamento.setMedico(medicoExistente);
        novoAgendamento.setDataConsulta(agendamentoDto.getDataConsulta());
        novoAgendamento.setHoraConsulta(agendamentoDto.getHoraConsulta());
        novoAgendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);

        // 5. Salva a ENTIDADE no B.D
        return agendamentoRepository.save(novoAgendamento);
    }

    @Transactional
    public Agendamento cancelar(Long id) {
        Agendamento agendamentoExistente = this.buscarPorId(id);

        if(agendamentoExistente.getStatusAgendamento() != StatusAgendamento.AGENDADO) {
            throw new RuntimeException("Apenas agendamentos com status AGENDADO podem ser cancelados.");
        }
        agendamentoExistente.setStatusAgendamento(StatusAgendamento.CANCELADO);

        return agendamentoRepository.save(agendamentoExistente);
    }

}
