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

@RestController
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    //Vai fazer com que, ao listar, ele mostre 10 médicos por vez
    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listarTodos(@PageableDefault(size = 10, sort = {"nome"})Pageable paginacao) {
        Page<Medico> medicos = medicoService.listarTodos(paginacao);

        Page <MedicoResponseDTO> paginaMedicos = medicos.map(MedicoResponseDTO::new);
        return ResponseEntity.ok(paginaMedicos);
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> cadastrar(@RequestBody @Valid MedicoRequestDTO medico){
        Medico cadastrarMedico = medicoService.cadastrar(medico);

        return ResponseEntity.ok(new MedicoResponseDTO(cadastrarMedico));
    }

    @PutMapping
    public ResponseEntity<MedicoResponseDTO> atualizar(@RequestBody @Valid MedicoUpdateDTO medicoDto) {
        Medico atualizarMedico = medicoService.atualizar(medicoDto);

        return ResponseEntity.ok(new MedicoResponseDTO(atualizarMedico));
    }


}
