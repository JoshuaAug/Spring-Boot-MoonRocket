package org.nasa.rocketto.model;

import jakarta.persistence.*;
import org.nasa.rocketto.enums.StatusMissao;

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
    private String objetivo; // "Identificar" ou "Comunicar"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status = StatusMissao.PREPARANDO;

    @ManyToOne
    @JoinColumn(name = "foguete_id")
    private Foguete foguete;

    @ManyToOne
    @JoinColumn(name = "satelite_id")
    private Satelite satelite;

    // Mensagens e dados coletados — guardados como lista no banco
    @ElementCollection
    @CollectionTable(name = "missao_mensagens", joinColumns = @JoinColumn(name = "missao_id"))
    @Column(name = "mensagem")
    private List<String> mensagens = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "missao_dados", joinColumns = @JoinColumn(name = "missao_id"))
    @Column(name = "dado")
    private List<String> dados = new ArrayList<>();

    public Missao() {}

    public Missao(String nome, String objetivo, Foguete foguete, Satelite satelite) {
        this.nome = nome;
        this.objetivo = objetivo;
        this.foguete = foguete;
        this.satelite = satelite;
        this.status = StatusMissao.PREPARANDO;
    }

    // Iniciar missão — equivalente ao iniciar() original
    public boolean iniciar() {
        boolean lancou = this.foguete.lancar();
        if (!lancou) {
            this.status = StatusMissao.FALHOU;
            return false;
        }
        this.satelite.setStatus(org.nasa.rocketto.enums.StatusSatelite.EM_ORBITA);
        this.foguete.setStatus(org.nasa.rocketto.enums.StatusFoguete.EM_MISSAO);
        this.status = StatusMissao.EM_ANDAMENTO;
        return true;
    }

    // Encerrar missão
    public void encerrar() {
        boolean objetivoCumprido =
            (objetivo.equals("Identificar") && !dados.isEmpty()) ||
            (objetivo.equals("Comunicar")   && !mensagens.isEmpty());

        this.status = objetivoCumprido ? StatusMissao.CONCLUIDA : StatusMissao.FALHOU;
        this.foguete.setStatus(org.nasa.rocketto.enums.StatusFoguete.EM_SOLO);
    }

    // Getters e Setters
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

    public List<String> getMensagens() { return mensagens; }
    public void addMensagem(String mensagem) { this.mensagens.add(mensagem); }

    public List<String> getDados() { return dados; }
    public void addDado(String dado) { this.dados.add(dado); }
}
