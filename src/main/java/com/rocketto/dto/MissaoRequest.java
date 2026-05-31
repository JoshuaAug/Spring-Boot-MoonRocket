package com.rocketto.dto;

import com.rocketto.enums.Orbita;

import java.util.List;

public class MissaoRequest {
    private String nome;
    private String objetivo;
    private Orbita orbita;
    private Long fogueteId;
    private Long sateliteId;
    private List<Long> agenteIds;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public Orbita getOrbita() { return orbita; }
    public void setOrbita(Orbita orbita) { this.orbita = orbita; }

    public Long getFogueteId() { return fogueteId; }
    public void setFogueteId(Long fogueteId) { this.fogueteId = fogueteId; }

    public Long getSateliteId() { return sateliteId; }
    public void setSateliteId(Long sateliteId) { this.sateliteId = sateliteId; }

    public List<Long> getAgenteIds() { return agenteIds; }
    public void setAgenteIds(List<Long> agenteIds) { this.agenteIds = agenteIds; }
}