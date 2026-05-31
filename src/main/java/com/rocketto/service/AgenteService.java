package com.rocketto.service;

import com.rocketto.dto.AgenteRequest;
import com.rocketto.enums.StatusAgente;
import com.rocketto.model.Agente;
import com.rocketto.repository.AgenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgenteService {

    private final AgenteRepository repo;

    public AgenteService(AgenteRepository repo) {
        this.repo = repo;
    }

    public List<Agente> listar() {
        return repo.findAll();
    }

    public List<Agente> listarDisponiveis() {
        return repo.findByStatus(StatusAgente.NA_ESTACAO);
    }

    public Agente buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Agente não encontrado: " + id));
    }

    public Agente criar(AgenteRequest req) {
        Agente agente = new Agente(req.getNome(), req.getCargo());
        return repo.save(agente);
    }

    public void deletar(Long id) {
        Agente agente = buscarPorId(id);
        if (agente.getStatus() == StatusAgente.EM_MISSAO) {
            throw new RuntimeException("Não é possível deletar um agente em missão.");
        }
        repo.deleteById(id);
    }
}