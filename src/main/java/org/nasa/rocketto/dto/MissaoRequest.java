package org.nasa.rocketto.dto;

// DTO de criação de Missão
public class MissaoRequest {
    private String nome;
    private String objetivo; // "Identificar" ou "Comunicar"
    private Long fogueteId;
    private Long sateliteId;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public Long getFogueteId() { return fogueteId; }
    public void setFogueteId(Long fogueteId) { this.fogueteId = fogueteId; }
    public Long getSateliteId() { return sateliteId; }
    public void setSateliteId(Long sateliteId) { this.sateliteId = sateliteId; }
}
