package com.rocketto.dto;

public class NasaAsteroidDTO {

    private String id;
    private String nome;
    private double diametroMinKm;
    private double diametroMaxKm;
    private boolean potencialmentePerigoso;
    private String dataAproximacao;
    private double velocidadeKmH;
    private double distanciaKm;

    public NasaAsteroidDTO() {}

    public NasaAsteroidDTO(String id, String nome, double diametroMinKm, double diametroMaxKm,
                           boolean potencialmentePerigoso, String dataAproximacao,
                           double velocidadeKmH, double distanciaKm) {
        this.id = id;
        this.nome = nome;
        this.diametroMinKm = diametroMinKm;
        this.diametroMaxKm = diametroMaxKm;
        this.potencialmentePerigoso = potencialmentePerigoso;
        this.dataAproximacao = dataAproximacao;
        this.velocidadeKmH = velocidadeKmH;
        this.distanciaKm = distanciaKm;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getDiametroMinKm() { return diametroMinKm; }
    public void setDiametroMinKm(double diametroMinKm) { this.diametroMinKm = diametroMinKm; }

    public double getDiametroMaxKm() { return diametroMaxKm; }
    public void setDiametroMaxKm(double diametroMaxKm) { this.diametroMaxKm = diametroMaxKm; }

    public boolean isPotencialmentePerigoso() { return potencialmentePerigoso; }
    public void setPotencialmentePerigoso(boolean potencialmentePerigoso) { this.potencialmentePerigoso = potencialmentePerigoso; }

    public String getDataAproximacao() { return dataAproximacao; }
    public void setDataAproximacao(String dataAproximacao) { this.dataAproximacao = dataAproximacao; }

    public double getVelocidadeKmH() { return velocidadeKmH; }
    public void setVelocidadeKmH(double velocidadeKmH) { this.velocidadeKmH = velocidadeKmH; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }
}