package com.rocketto.model;

import jakarta.persistence.*;
import com.rocketto.enums.StatusSatelite;

@Entity
@Table(name = "satelites")
public class Satelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private float massa; // em kg

    @Column(nullable = false)
    private float energia = 100.0f; // 0 a 100%

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSatelite status = StatusSatelite.EM_SOLO;

    public Satelite() {}

    public Satelite(String nome, float massa) {
        this.nome = nome;
        this.massa = massa;
        this.energia = 100.0f;
        this.status = StatusSatelite.EM_SOLO;
    }

    // Ativar painéis solares — lógica original mantida
    public void ativarPaineis() {
        this.energia = Math.min(100f, this.energia + 25);
    }

    // Consumir energia para operações
    public boolean consumirEnergia(float quantidade) {
        if (this.energia < quantidade) {
            return false; // sem energia suficiente
        }
        this.energia -= quantidade;
        if (this.energia < 20) {
            this.status = StatusSatelite.SEM_ENERGIA;
        }
        return true;
    }

    // Radar — probabilidades do original: 1% estranho, 2% asteroide, 10% pedras, 87% poeira
    public String usarRadar() {
        if (!consumirEnergia(20)) {
            return "SEM_ENERGIA";
        }
        int evento = new java.util.Random().nextInt(100);
        if (evento == 0) return "ESTRANHO";
        if (evento <= 2)  return "ASTEROIDE";
        if (evento <= 12) return "PEDRAS";
        return "POEIRA";
    }

    // Getters e Setters
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public float getMassa() { return massa; }
    public void setMassa(float massa) { this.massa = massa; }

    public float getEnergia() { return energia; }
    public void setEnergia(float energia) { this.energia = energia; }

    public StatusSatelite getStatus() { return status; }
    public void setStatus(StatusSatelite status) { this.status = status; }
}
