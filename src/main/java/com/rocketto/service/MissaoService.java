package com.rocketto.service;

import com.rocketto.dto.MissaoRequest;
import com.rocketto.enums.CargoAgente;
import com.rocketto.enums.StatusAgente;
import com.rocketto.enums.StatusMissao;
import com.rocketto.model.Agente;
import com.rocketto.model.Foguete;
import com.rocketto.model.Missao;
import com.rocketto.model.Satelite;
import com.rocketto.repository.MissaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MissaoService {

    private final MissaoRepository missaoRepo;
    private final FogueteService fogueteService;
    private final SateliteService sateliteService;
    private final AgenteService agenteService;

    public MissaoService(MissaoRepository missaoRepo,
                         FogueteService fogueteService,
                         SateliteService sateliteService,
                         AgenteService agenteService) {
        this.missaoRepo      = missaoRepo;
        this.fogueteService  = fogueteService;
        this.sateliteService = sateliteService;
        this.agenteService   = agenteService;
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
                            " kg — Capacidade do foguete: " + foguete.getCarga() + " kg");
        }

        // Busca e valida os agentes
        List<Agente> agentes = req.getAgenteIds().stream()
                .map(agenteService::buscarPorId)
                .collect(Collectors.toList());

        validarAgentes(agentes);

        Missao missao = new Missao(req.getNome(), req.getObjetivo(), foguete, satelite, req.getOrbita());
        missao.setAgentes(agentes);
        return missaoRepo.save(missao);
    }

    private void validarAgentes(List<Agente> agentes) {
        if (agentes.size() < 4) {
            throw new RuntimeException("A missão requer no mínimo 4 agentes.");
        }

        // Conta por cargo
        Map<CargoAgente, Long> contagem = agentes.stream()
                .collect(Collectors.groupingBy(Agente::getCargo, Collectors.counting()));

        long comandantes = contagem.getOrDefault(CargoAgente.COMANDANTE, 0L);
        long pilotos     = contagem.getOrDefault(CargoAgente.PILOTO, 0L);
        long outros      = agentes.stream()
                .filter(a -> a.getCargo() != CargoAgente.COMANDANTE && a.getCargo() != CargoAgente.PILOTO)
                .count();

        if (comandantes != 1) {
            throw new RuntimeException("A missão deve ter exatamente 1 Comandante.");
        }
        if (pilotos < 1) {
            throw new RuntimeException("A missão deve ter ao menos 1 Piloto.");
        }
        if (outros < 2) {
            throw new RuntimeException("A missão deve ter ao menos 2 agentes de outras funções.");
        }

        // Verifica se algum agente já está em missão
        agentes.forEach(a -> {
            if (a.getStatus() == StatusAgente.EM_MISSAO) {
                throw new RuntimeException("O agente '" + a.getNome() + "' já está em outra missão.");
            }
        });
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