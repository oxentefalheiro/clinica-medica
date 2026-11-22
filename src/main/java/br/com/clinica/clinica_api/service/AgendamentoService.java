package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.dto.AgendamentoRequestDTO;
import br.com.clinica.clinica_api.dto.ReagendamentoRequestDTO;
import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.exception.ConflitoException;
import br.com.clinica.clinica_api.exception.RecursoNaoEncontradoException;
import br.com.clinica.clinica_api.exception.RegraNegocioException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    // Regras de funcionamento da clínica
    private static final LocalTime ABERTURA = LocalTime.of(7, 0);
    private static final LocalTime ULTIMA_CONSULTA = LocalTime.of(18, 30);
    private static final int DURACAO_CONSULTA_MIN = 30;

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PacienteService pacienteService, MedicoService medicoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento com o ID " + id + " não encontrado."));
    }

    public Page<Agendamento> listarTodos(Pageable paginacao) {
        return agendamentoRepository.findAll(paginacao);
    }

    public Page<Agendamento> listarPorStatus(StatusAgendamento status, Pageable paginacao) {
        return agendamentoRepository.findByStatusAgendamento(status, paginacao);
    }

    public Page<Agendamento> listarPorHoraConsulta(LocalTime horaConsulta, Pageable paginacao) {
        return agendamentoRepository.findByHoraConsulta(horaConsulta, paginacao);
    }

    public Page<Agendamento> listarDiaConsulta(LocalDate dataConsulta, Pageable paginacao) {
        return agendamentoRepository.findByDataConsulta(dataConsulta, paginacao);
    }

    // RF05 - Agenda do médico em um dia
    public List<Agendamento> agendaDoMedico(Long medicoId, LocalDate data) {
        Medico medico = medicoService.buscarPorId(medicoId);
        return agendamentoRepository.findByMedicoAndDataConsultaAndStatusAgendamentoNotOrderByHoraConsulta(
                medico, data, StatusAgendamento.CANCELADO);
    }

    // RF04 - Horários livres de um médico em uma data (intervalos de 30 minutos)
    public List<LocalTime> horariosLivres(Long medicoId, LocalDate data) {
        Medico medico = medicoService.buscarPorId(medicoId);

        if (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RegraNegocioException("A clínica não funciona aos domingos.");
        }
        if (data.isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Não é possível consultar horários de uma data passada.");
        }
        if (!medico.isAtivo()) {
            return List.of();
        }

        Set<LocalTime> ocupados = agendaDoMedico(medicoId, data).stream()
                .map(Agendamento::getHoraConsulta)
                .collect(Collectors.toSet());

        LocalTime inicio = medico.getHorarioInicio().isAfter(ABERTURA) ? medico.getHorarioInicio() : ABERTURA;
        LocalTime fimJornada = medico.getHorarioFim() != null ? medico.getHorarioFim() : LocalTime.of(19, 0);

        List<LocalTime> livres = new ArrayList<>();
        for (LocalTime h = inicio; !h.isAfter(ULTIMA_CONSULTA) && h.plusMinutes(DURACAO_CONSULTA_MIN).compareTo(fimJornada) <= 0;
             h = h.plusMinutes(DURACAO_CONSULTA_MIN)) {
            boolean jaPassou = data.equals(LocalDate.now()) && !h.isAfter(LocalTime.now());
            if (!ocupados.contains(h) && !jaPassou) {
                livres.add(h);
            }
        }
        return livres;
    }

    // RF03 - Agendamento com validação de conflito (fluxo que envolve Paciente, Médico e Agendamento)
    @Transactional
    public Agendamento agendar(AgendamentoRequestDTO dto) {
        // 1. Busca paciente e médico pelos IDs (404 se não existirem)
        Paciente paciente = pacienteService.buscarPorId(dto.getPacienteId());
        Medico medico = medicoService.buscarPorId(dto.getMedicoId());

        // 2. Valida todas as regras do horário
        validarHorario(paciente, medico, dto.getDataConsulta(), dto.getHoraConsulta());

        // 3. Monta e salva a entidade
        Agendamento novo = new Agendamento();
        novo.setPaciente(paciente);
        novo.setMedico(medico);
        novo.setDataConsulta(dto.getDataConsulta());
        novo.setHoraConsulta(dto.getHoraConsulta());
        novo.setStatusAgendamento(StatusAgendamento.AGENDADO);
        return agendamentoRepository.save(novo);
    }

    // RF07 - Reagendar: cancela a consulta atual e cria uma nova NA MESMA TRANSAÇÃO.
    // Se o novo horário for inválido, a exceção provoca ROLLBACK e o cancelamento é desfeito:
    // a consulta original continua AGENDADA, como se nada tivesse acontecido.
    @Transactional
    public Agendamento reagendar(Long id, ReagendamentoRequestDTO dto) {
        Agendamento atual = buscarPorId(id);
        validarPodeAlterar(atual);

        // Passo 1: libera o horário antigo
        atual.setStatusAgendamento(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(atual);

        // Passo 2: valida o novo horário (se falhar aqui -> rollback do passo 1)
        validarHorario(atual.getPaciente(), atual.getMedico(), dto.getNovaData(), dto.getNovaHora());

        // Passo 3: cria a nova consulta
        Agendamento nova = new Agendamento();
        nova.setPaciente(atual.getPaciente());
        nova.setMedico(atual.getMedico());
        nova.setDataConsulta(dto.getNovaData());
        nova.setHoraConsulta(dto.getNovaHora());
        nova.setStatusAgendamento(StatusAgendamento.AGENDADO);
        return agendamentoRepository.save(nova);
    }

    // RF06 - Recepção confirma a presença
    @Transactional
    public Agendamento confirmar(Long id) {
        Agendamento agendamento = buscarPorId(id);
        if (agendamento.getStatusAgendamento() != StatusAgendamento.AGENDADO) {
            throw new ConflitoException("Apenas agendamentos com status AGENDADO podem ser confirmados.");
        }
        agendamento.setStatusAgendamento(StatusAgendamento.CONFIRMADO);
        return agendamentoRepository.save(agendamento);
    }

    // RF06 - Médico registra o atendimento
    @Transactional
    public Agendamento realizar(Long id) {
        Agendamento agendamento = buscarPorId(id);
        validarPodeAlterar(agendamento);
        if (agendamento.getDataConsulta().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("Não é possível marcar como realizada uma consulta de data futura.");
        }
        agendamento.setStatusAgendamento(StatusAgendamento.REALIZADO);
        return agendamentoRepository.save(agendamento);
    }

    // RF07 - Cancelar libera o horário imediatamente (consultas canceladas não contam no conflito)
    @Transactional
    public Agendamento cancelar(Long id) {
        Agendamento agendamento = buscarPorId(id);
        validarPodeAlterar(agendamento);
        agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO);
        return agendamentoRepository.save(agendamento);
    }

    // ===== Regras de negócio reutilizadas =====

    private void validarPodeAlterar(Agendamento agendamento) {
        StatusAgendamento status = agendamento.getStatusAgendamento();
        if (status != StatusAgendamento.AGENDADO && status != StatusAgendamento.CONFIRMADO) {
            throw new ConflitoException("Consulta com status " + status + " não pode mais ser alterada.");
        }
    }

    private void validarHorario(Paciente paciente, Medico medico, LocalDate data, LocalTime hora) {
        if (!paciente.isAtivo()) {
            throw new RegraNegocioException("Paciente inativo não pode ter consultas agendadas.");
        }
        if (!medico.isAtivo()) {
            throw new RegraNegocioException("Médico inativo não pode receber agendamentos.");
        }

        // Funcionamento: segunda a sábado, 07:00 às 19:00 (última consulta 18:30)
        if (data.getDayOfWeek() == DayOfWeek.SUNDAY || hora.isBefore(ABERTURA) || hora.isAfter(ULTIMA_CONSULTA)) {
            throw new RegraNegocioException("Consulta fora do horário de funcionamento da clínica (segunda a sábado, das 07:00 às 19:00).");
        }
        // Consultas de 30 em 30 minutos (ex.: 08:00, 08:30)
        if (hora.getMinute() % DURACAO_CONSULTA_MIN != 0 || hora.getSecond() != 0) {
            throw new RegraNegocioException("As consultas devem começar em horários cheios ou meia hora (ex.: 08:00, 08:30).");
        }
        if (LocalDateTime.of(data, hora).isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("Não é possível agendar consulta em data/horário passado.");
        }
        // Jornada do médico
        if (hora.isBefore(medico.getHorarioInicio())
                || (medico.getHorarioFim() != null && hora.plusMinutes(DURACAO_CONSULTA_MIN).isAfter(medico.getHorarioFim()))) {
            throw new RegraNegocioException("Horário fora da jornada do médico (" + medico.getHorarioInicio() + " às " + medico.getHorarioFim() + ").");
        }
        // Conflitos (consultas canceladas não bloqueiam o horário)
        if (agendamentoRepository.existsByMedicoAndDataConsultaAndHoraConsultaAndStatusAgendamentoNot(
                medico, data, hora, StatusAgendamento.CANCELADO)) {
            throw new ConflitoException("O médico já possui consulta marcada neste dia e horário.");
        }
        if (agendamentoRepository.existsByPacienteAndDataConsultaAndHoraConsultaAndStatusAgendamentoNot(
                paciente, data, hora, StatusAgendamento.CANCELADO)) {
            throw new ConflitoException("O paciente já possui outra consulta neste dia e horário.");
        }
    }
}
