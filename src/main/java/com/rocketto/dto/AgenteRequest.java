package com.rocketto.dto;

import com.rocketto.enums.CargoAgente;

public class AgenteRequest {

    private String nome;
    private CargoAgente cargo;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public CargoAgente getCargo() { return cargo; }
    public void setCargo(CargoAgente cargo) { this.cargo = cargo; }
}