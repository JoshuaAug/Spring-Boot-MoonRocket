package com.rocketto.service;

import com.rocketto.dto.MissaoRequest;
import com.rocketto.enums.StatusMissao;
import com.rocketto.model.Foguete;
import com.rocketto.model.Missao;
import com.rocketto.model.Satelite;
import com.rocketto.repository.MissaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MissaoService {

    private final MissaoRepository missaoRepo;
    private final FogueteService fogueteService;
    private final SateliteService sateliteService;

    public MissaoService(MissaoRepository missaoRepo,
                         FogueteService fogueteService,
                         SateliteService sateliteService) {
        this.missaoRepo      = missaoRepo;
        this.fogueteService  = fogueteService;
        this.sateliteService = sateliteService;
    }

    public List<Missao> listar() {
        return missaoRepo.findAll();
    }

    public Missao buscarPorId(Long id) {
        return missaoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Missão não encontrada: " + id));
    }

    public Missao criar(MissaoRequest req) {
        Foguete foguete   = fogueteService.buscarPorId(req.getFogueteId());
        Satelite satelite = sateliteService.buscarPorId(req.getSateliteId());

        // Regra: massa do satélite deve ser menor ou igual à capacidade de carga do foguete
        if (satelite.getMassa() > foguete.getCarga()) {
            throw new RuntimeException(
                    "Satélite muito pesado! Massa: " + satelite.getMassa() +
                            " kg — Capacidade do foguete: " + foguete.getCarga() + " kg"
            );
        }

        Missao missao = new Missao(req.getNome(), req.getObjetivo(), foguete, satelite, req.getOrbita());
        return missaoRepo.save(missao);
    }

    public Missao iniciar(Long id) {
        Missao missao = buscarPorId(id);
        if (missao.getStatus() != StatusMissao.PREPARANDO) {
            throw new RuntimeException("Missão não está em estado PREPARANDO.");
        }
        boolean lancou = missao.iniciar();
        if (!lancou) {
            throw new RuntimeException("Falha no lançamento: combustível insuficiente (mínimo 50 ton).");
        }
        return missaoRepo.save(missao);
    }

    public Missao encerrar(Long id) {
        Missao missao = buscarPorId(id);
        missao.encerrar();
        return missaoRepo.save(missao);
    }

    public Map<String, Object> usarRadar(Long id) {
        Missao missao = buscarPorId(id);
        if (missao.getStatus() != StatusMissao.EM_ANDAMENTO) {
            throw new RuntimeException("Missão não está em andamento.");
        }

        Satelite satelite = missao.getSatelite();
        String resultado  = satelite.usarRadar();

        if (resultado.equals("SEM_ENERGIA")) {
            throw new RuntimeException("Satélite sem energia suficiente (mínimo 20%).");
        }

        int distancia = new java.util.Random().nextInt(10000) + 50;
        String descricao = switch (resultado) {
            case "ESTRANHO"  -> "???? Sinal estranho detectado — origem desconhecida";
            case "ASTEROIDE" -> "Asteroide detectado a " + distancia + " km — Perigo: ALTO";
            case "PEDRAS"    -> "Pequenas pedras espaciais a " + distancia + " km — Perigo: Baixo";
            default          -> "Poeira cósmica a " + distancia + " km — Perigo: Nenhum";
        };

        missao.addDado(descricao);
        missaoRepo.save(missao);

        return Map.of(
                "tipo",            resultado,
                "descricao",       descricao,
                "energiaRestante", satelite.getEnergia()
        );
    }

    public Missao enviarMensagem(Long id, String mensagem) {
        Missao missao = buscarPorId(id);
        if (missao.getStatus() != StatusMissao.EM_ANDAMENTO) {
            throw new RuntimeException("Missão não está em andamento.");
        }
        boolean ok = missao.getSatelite().consumirEnergia(20);
        if (!ok) throw new RuntimeException("Satélite sem energia suficiente.");
        missao.addMensagem(mensagem);
        return missaoRepo.save(missao);
    }

    public Missao ativarPaineis(Long id) {
        Missao missao = buscarPorId(id);
        missao.getSatelite().ativarPaineis();
        return missaoRepo.save(missao);
    }
}