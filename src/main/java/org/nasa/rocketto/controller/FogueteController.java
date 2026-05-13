package org.nasa.rocketto.controller;

import org.nasa.rocketto.dto.FogueteRequest;
import org.nasa.rocketto.model.Foguete;
import org.nasa.rocketto.service.FogueteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foguetes")
@CrossOrigin(origins = "*")
public class FogueteController {

    private final FogueteService service;

    public FogueteController(FogueteService service) {
        this.service = service;
    }

    // GET /api/foguetes
    @GetMapping
    public List<Foguete> listar() {
        return service.listar();
    }

    // GET /api/foguetes/{id}
    @GetMapping("/{id}")
    public Foguete buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/foguetes
    @PostMapping
    public ResponseEntity<Foguete> criar(@RequestBody FogueteRequest req) {
        return ResponseEntity.ok(service.criar(req));
    }

    // PUT /api/foguetes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Foguete> atualizar(@PathVariable Long id,
                                              @RequestBody FogueteRequest req) {
        return ResponseEntity.ok(service.atualizar(id, req));
    }

    // DELETE /api/foguetes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/foguetes/{id}/abastecer?quantidade=50
    @PostMapping("/{id}/abastecer")
    public ResponseEntity<Foguete> abastecer(@PathVariable Long id,
                                              @RequestParam float quantidade) {
        return ResponseEntity.ok(service.abastecer(id, quantidade));
    }
}
