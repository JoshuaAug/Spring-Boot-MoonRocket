package org.nasa.rocketto.service;

import org.nasa.rocketto.dto.MissaoRequest;
import org.nasa.rocketto.enums.StatusMissao;
import org.nasa.rocketto.model.Foguete;
import org.nasa.rocketto.model.Missao;
import org.nasa.rocketto.model.Satelite;
import org.nasa.rocketto.repository.MissaoRepository;
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

    // Criar missão — valida peso antes (lógica original do conferirPeso)
    public Missao criar(MissaoRequest req) {
        Foguete foguete   = fogueteService.buscarPorId(req.getFogueteId());
        Satelite satelite = sateliteService.buscarPorId(req.getSateliteId());

        if (!conferirPeso(foguete, satelite)) {
            throw new RuntimeException("Peso do satélite ultrapassa a capacidade do foguete!");
        }

        Missao missao = new Missao(req.getNome(), req.getObjetivo(), foguete, satelite);
        return missaoRepo.save(missao);
    }

    // Iniciar missão
    public Missao iniciar(Long id) {
        Missao missao = buscarPorId(id);
        if (missao.getStatus() != StatusMissao.PREPARANDO) {
            throw new RuntimeException("Missão não está em estado PREPARANDO.");
        }
        boolean lancou = missao.iniciar();
        if (!lancou) {
            throw new RuntimeException("Falha no lançamento: combustível insuficiente.");
        }
        return missaoRepo.save(missao);
    }

    // Encerrar missão
    public Missao encerrar(Long id) {
        Missao missao = buscarPorId(id);
        missao.encerrar();
        return missaoRepo.save(missao);
    }

    // Usar radar — retorna o resultado + salva dado na missão
    public Map<String, Object> usarRadar(Long id) {
        Missao missao = buscarPorId(id);
        if (missao.getStatus() != StatusMissao.EM_ANDAMENTO) {
            throw new RuntimeException("Missão não está em andamento.");
        }

        Satelite satelite = missao.getSatelite();
        String resultado  = satelite.usarRadar();

        if (resultado.equals("SEM_ENERGIA")) {
            throw new RuntimeException("Satélite sem energia suficiente.");
        }

        // Monta descrição do achado (igual ao original)
        int distancia = new java.util.Random().nextInt(10000) + 50;
        String descricao = switch (resultado) {
            case "ESTRANHO"   -> "???? Sinal estranho detectado — origem desconhecida";
            case "ASTEROIDE"  -> "Asteroide detectado a " + distancia + " km — Perigo: ALTO";
            case "PEDRAS"     -> "Pequenas pedras espaciais a " + distancia + " km — Perigo: Baixo";
            default           -> "Poeira cósmica a " + distancia + " km — Perigo: Nenhum";
        };

        missao.addDado(descricao);
        sateliteService.buscarPorId(satelite.getId()); // força atualização
        missaoRepo.save(missao);

        return Map.of(
            "tipo", resultado,
            "descricao", descricao,
            "energiaRestante", satelite.getEnergia()
        );
    }

    // Enviar mensagem pela missão
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

    // Ativar painéis solares do satélite da missão
    public Missao ativarPaineis(Long id) {
        Missao missao = buscarPorId(id);
        missao.getSatelite().ativarPaineis();
        return missaoRepo.save(missao);
    }

    // Lógica original do conferirPeso da Central
    private boolean conferirPeso(Foguete foguete, Satelite satelite) {
        return foguete.getCarga() <= satelite.getMassa() * 0.04;
    }
}
