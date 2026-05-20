package com.rocketto.dto;

import com.rocketto.enums.Orbita;

// DTO de criação/edição (dados que chegam do frontend)
public class FogueteRequest {
    private String nome;
    private float combustivel;
    private float carga;
    private Orbita orbita;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public float getCombustivel() { return combustivel; }
    public void setCombustivel(float combustivel) { this.combustivel = combustivel; }
    public float getCarga() { return carga; }
    public void setCarga(float carga) { this.carga = carga; }
    public Orbita getOrbita() { return orbita; }
    public void setOrbita(Orbita orbita) { this.orbita = orbita; }
}
