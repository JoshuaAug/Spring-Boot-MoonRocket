package com.rocketto.controller;

import com.rocketto.dto.AgenteRequest;
import com.rocketto.model.Agente;
import com.rocketto.service.AgenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agentes")
@CrossOrigin(origins = "*")
public class AgenteController {

    private final AgenteService service;

    public AgenteController(AgenteService service) {
        this.service = service;
    }

    // GET /api/agentes
    @GetMapping
    public List<Agente> listar() {
        return service.listar();
    }

    // GET /api/agentes/disponiveis
    @GetMapping("/disponiveis")
    public List<Agente> listarDisponiveis() {
        return service.listarDisponiveis();
    }

    // GET /api/agentes/{id}
    @GetMapping("/{id}")
    public Agente buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/agentes
    @PostMapping
    public ResponseEntity<Agente> criar(@RequestBody AgenteRequest req) {
        return ResponseEntity.ok(service.criar(req));
    }

    // DELETE /api/agentes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}