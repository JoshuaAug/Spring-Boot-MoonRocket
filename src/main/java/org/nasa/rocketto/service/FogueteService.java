package org.nasa.rocketto.service;

import org.nasa.rocketto.dto.FogueteRequest;
import org.nasa.rocketto.model.Foguete;
import org.nasa.rocketto.repository.FogueteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FogueteService {

    private final FogueteRepository repo;

    public FogueteService(FogueteRepository repo) {
        this.repo = repo;
    }

    public List<Foguete> listar() {
        return repo.findAll();
    }

    public Foguete buscarPorId(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Foguete não encontrado: " + id));
    }

    public Foguete criar(FogueteRequest req) {
        if (repo.existsByNome(req.getNome())) {
            throw new RuntimeException("Já existe um foguete com o nome: " + req.getNome());
        }
        Foguete f = new Foguete(req.getNome(), req.getCombustivel(), req.getCarga(), req.getOrbita());
        return repo.save(f);
    }

    public Foguete atualizar(Long id, FogueteRequest req) {
        Foguete f = buscarPorId(id);
        f.setNome(req.getNome());
        f.setCombustivel(req.getCombustivel());
        f.setCarga(req.getCarga());
        f.setOrbita(req.getOrbita());
        return repo.save(f);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }

    public Foguete abastecer(Long id, float quantidade) {
        Foguete f = buscarPorId(id);
        f.abastecer(quantidade);
        return repo.save(f);
    }
}
