package com.rocketto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rocketto.enums.Orbita;
import com.rocketto.enums.StatusAgente;
import com.rocketto.enums.StatusFoguete;
import com.rocketto.enums.StatusSatelite;
import jakarta.persistence.*;
import com.rocketto.enums.StatusMissao;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missoes")
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String objetivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status = StatusMissao.PREPARANDO;

    @JsonIgnoreProperties({"sateliteCarregado", "missoes"})
    @ManyToOne
    @JoinColumn(name = "foguete_id")
    private Foguete foguete;

    @JsonIgnoreProperties({"missoes"})
    @ManyToOne
    @JoinColumn(name = "satelite_id")
    private Satelite satelite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Orbita orbita;

    // Relação com agentes — tabela intermediária missao_agentes
    @JsonIgnoreProperties({"missoes"})
    @ManyToMany
    @JoinTable(
            name = "missao_agentes",
            joinColumns = @JoinColumn(name = "missao_id"),
            inverseJoinColumns = @JoinColumn(name = "agente_id")
    )
    private List<Agente> agentes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "missao_mensagens", joinColumns = @JoinColumn(name = "missao_id"))
    @Column(name = "mensagem")
    private List<String> mensagens = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "missao_dados", joinColumns = @JoinColumn(name = "missao_id"))
    @Column(name = "dado")
    private List<String> dados = new ArrayList<>();

    public Missao() {}

    public Missao(String nome, String objetivo, Foguete foguete, Satelite satelite, Orbita orbita) {
        this.nome     = nome;
        this.objetivo = objetivo;
        this.foguete  = foguete;
        this.satelite = satelite;
        this.orbita   = orbita;
        this.status   = StatusMissao.PREPARANDO;
    }

    public boolean iniciar() {
        boolean lancou = this.foguete.lancar();
        if (!lancou) {
            this.status = StatusMissao.FALHOU;
            return false;
        }
        this.satelite.setStatus(StatusSatelite.EM_ORBITA);
        this.foguete.setStatus(StatusFoguete.EM_MISSAO);
        // Bloqueia todos os agentes da missão
        this.agentes.forEach(a -> a.setStatus(StatusAgente.EM_MISSAO));
        this.status = StatusMissao.EM_ANDAMENTO;
        return true;
    }

    public void encerrar() {
        boolean objetivoCumprido =
                (objetivo.equals("Identificar") && !dados.isEmpty()) ||
                        (objetivo.equals("Comunicar")   && !mensagens.isEmpty());

        this.status = objetivoCumprido ? StatusMissao.CONCLUIDA : StatusMissao.FALHOU;
        this.foguete.setStatus(StatusFoguete.EM_SOLO);
        // Libera todos os agentes de volta para a estação
        this.agentes.forEach(a -> a.setStatus(StatusAgente.NA_ESTACAO));
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public StatusMissao getStatus() { return status; }
    public void setStatus(StatusMissao status) { this.status = status; }

    public Foguete getFoguete() { return foguete; }
    public void setFoguete(Foguete foguete) { this.foguete = foguete; }

    public Satelite getSatelite() { return satelite; }
    public void setSatelite(Satelite satelite) { this.satelite = satelite; }

    public Orbita getOrbita() { return orbita; }
    public void setOrbita(Orbita orbita) { this.orbita = orbita; }

    public List<Agente> getAgentes() { return agentes; }
    public void setAgentes(List<Agente> agentes) { this.agentes = agentes; }

    public List<String> getMensagens() { return mensagens; }
    public void addMensagem(String m) { this.mensagens.add(m); }

    public List<String> getDados() { return dados; }
    public void addDado(String d) { this.dados.add(d); }
}