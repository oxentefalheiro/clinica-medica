package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.model.Especialidade;
import br.com.clinica.clinica_api.service.EspecialidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidade")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;
    public EspecialidadeController(EspecialidadeService especialidadeService) {
        this.especialidadeService = especialidadeService;
    }

    @GetMapping
    public ResponseEntity<List<Especialidade>> listarTodas() {
        List<Especialidade> especialidade = especialidadeService.listarTodas();

        return ResponseEntity.ok(especialidade);
    }

    @PostMapping
    public ResponseEntity<Especialidade> cadastrar(@RequestBody Especialidade especialidade) {
        Especialidade cadastrarEspecialidade = especialidadeService.cadastrar(especialidade);

        return ResponseEntity.ok(cadastrarEspecialidade);
    }

}
