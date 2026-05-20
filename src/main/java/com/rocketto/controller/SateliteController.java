package com.rocketto.controller;

import com.rocketto.dto.SateliteRequest;
import com.rocketto.model.Satelite;
import com.rocketto.service.SateliteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/satelites")
@CrossOrigin(origins = "*")
public class SateliteController {

    private final SateliteService service;

    public SateliteController(SateliteService service) {
        this.service = service;
    }

    // GET /api/satelites
    @GetMapping
    public List<Satelite> listar() {
        return service.listar();
    }

    // GET /api/satelites/{id}
    @GetMapping("/{id}")
    public Satelite buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/satelites
    @PostMapping
    public ResponseEntity<Satelite> criar(@RequestBody SateliteRequest req) {
        return ResponseEntity.ok(service.criar(req));
    }

    // PUT /api/satelites/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Satelite> atualizar(@PathVariable Long id,
                                               @RequestBody SateliteRequest req) {
        return ResponseEntity.ok(service.atualizar(id, req));
    }

    // DELETE /api/satelites/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/satelites/{id}/paineis
    @PostMapping("/{id}/paineis")
    public ResponseEntity<Satelite> ativarPaineis(@PathVariable Long id) {
        return ResponseEntity.ok(service.ativarPaineis(id));
    }
}
