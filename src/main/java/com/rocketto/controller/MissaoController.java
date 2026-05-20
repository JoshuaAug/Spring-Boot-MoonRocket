package com.rocketto.controller;

import com.rocketto.dto.MissaoRequest;
import com.rocketto.model.Missao;
import com.rocketto.service.MissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/missoes")
@CrossOrigin(origins = "*")
public class MissaoController {

    private final MissaoService service;

    public MissaoController(MissaoService service) {
        this.service = service;
    }

    // GET /api/missoes
    @GetMapping
    public List<Missao> listar() {
        return service.listar();
    }

    // GET /api/missoes/{id}
    @GetMapping("/{id}")
    public Missao buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/missoes — cria a missão (valida peso automaticamente)
    @PostMapping
    public ResponseEntity<Missao> criar(@RequestBody MissaoRequest req) {
        return ResponseEntity.ok(service.criar(req));
    }

    // POST /api/missoes/{id}/iniciar — lança o foguete
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Missao> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(service.iniciar(id));
    }

    // POST /api/missoes/{id}/encerrar
    @PostMapping("/{id}/encerrar")
    public ResponseEntity<Missao> encerrar(@PathVariable Long id) {
        return ResponseEntity.ok(service.encerrar(id));
    }

    // POST /api/missoes/{id}/radar
    @PostMapping("/{id}/radar")
    public ResponseEntity<Map<String, Object>> radar(@PathVariable Long id) {
        return ResponseEntity.ok(service.usarRadar(id));
    }

    // POST /api/missoes/{id}/mensagem
    @PostMapping("/{id}/mensagem")
    public ResponseEntity<Missao> mensagem(@PathVariable Long id,
                                            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(service.enviarMensagem(id, body.get("mensagem")));
    }

    // POST /api/missoes/{id}/paineis
    @PostMapping("/{id}/paineis")
    public ResponseEntity<Missao> paineis(@PathVariable Long id) {
        return ResponseEntity.ok(service.ativarPaineis(id));
    }
}
