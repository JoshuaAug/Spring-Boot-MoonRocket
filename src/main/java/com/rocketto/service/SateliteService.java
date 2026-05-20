package com.rocketto.service;

import com.rocketto.dto.SateliteRequest;
import com.rocketto.model.Satelite;
import com.rocketto.repository.SateliteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SateliteService {

    private final SateliteRepository repo;

    public SateliteService(SateliteRepository repo) {
        this.repo = repo;
    }

    public List<Satelite> listar() {
        return repo.findAll();
    }

    public Satelite buscarPorId(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Satélite não encontrado: " + id));
    }

    public Satelite criar(SateliteRequest req) {
        if (repo.existsByNome(req.getNome())) {
            throw new RuntimeException("Já existe um satélite com o nome: " + req.getNome());
        }
        Satelite s = new Satelite(req.getNome(), req.getMassa());
        return repo.save(s);
    }

    public Satelite atualizar(Long id, SateliteRequest req) {
        Satelite s = buscarPorId(id);
        s.setNome(req.getNome());
        s.setMassa(req.getMassa());
        return repo.save(s);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }

    public Satelite ativarPaineis(Long id) {
        Satelite s = buscarPorId(id);
        s.ativarPaineis();
        return repo.save(s);
    }
}
