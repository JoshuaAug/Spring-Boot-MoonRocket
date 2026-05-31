package com.rocketto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rocketto.enums.CargoAgente;
import com.rocketto.enums.StatusAgente;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agentes")
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoAgente cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgente status = StatusAgente.NA_ESTACAO;

    // Evita loop infinito na serialização: Agente → Missao → Agente...
    @JsonIgnoreProperties({"agentes", "foguete", "satelite"})
    @ManyToMany(mappedBy = "agentes")
    private List<Missao> missoes = new ArrayList<>();

    public Agente() {}

    public Agente(String nome, CargoAgente cargo) {
        this.nome   = nome;
        this.cargo  = cargo;
        this.status = StatusAgente.NA_ESTACAO;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public CargoAgente getCargo() { return cargo; }
    public void setCargo(CargoAgente cargo) { this.cargo = cargo; }

    public StatusAgente getStatus() { return status; }
    public void setStatus(StatusAgente status) { this.status = status; }

    public List<Missao> getMissoes() { return missoes; }
}