package org.nasa.rocketto.dto;

// DTO de criação de Satélite
public class SateliteRequest {
    private String nome;
    private float massa;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public float getMassa() { return massa; }
    public void setMassa(float massa) { this.massa = massa; }
}
