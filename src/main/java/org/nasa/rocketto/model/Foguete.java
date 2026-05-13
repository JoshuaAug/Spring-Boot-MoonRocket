package org.nasa.rocketto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.nasa.rocketto.enums.Orbita;
import org.nasa.rocketto.enums.StatusFoguete;

@Entity
@Table(name = "foguetes")
public class Foguete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private float combustivel;

    @Column(nullable = false)
    private float carga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Orbita orbita = Orbita.SOLO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFoguete status = StatusFoguete.EM_SOLO;

    // Evita loop infinito na serialização JSON
    @JsonIgnoreProperties({"foguete", "missoes"})
    @OneToOne
    @JoinColumn(name = "satelite_id")
    private Satelite sateliteCarregado;

    @Column
    private float distanciaMaxima;

    public Foguete() {}

    public Foguete(String nome, float combustivel, float carga, Orbita orbita) {
        this.nome             = nome;
        this.combustivel      = combustivel;
        this.carga            = carga;
        this.orbita           = orbita;
        this.status           = StatusFoguete.EM_SOLO;
        this.distanciaMaxima  = calcularDistancia(combustivel);
    }

    private float calcularDistancia(float combustivel) {
        return (combustivel - 30) * 400;
    }

    public boolean lancar() {
        if (this.combustivel > 50) {
            this.status = StatusFoguete.EM_ORBITA;
            return true;
        }
        this.status = StatusFoguete.FALHA;
        return false;
    }

    public void abastecer(float quantidade) {
        this.combustivel += quantidade;
        this.distanciaMaxima = calcularDistancia(this.combustivel);
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public float getCombustivel() { return combustivel; }
    public void setCombustivel(float combustivel) {
        this.combustivel = combustivel;
        this.distanciaMaxima = calcularDistancia(combustivel);
    }

    public float getCarga() { return carga; }
    public void setCarga(float carga) { this.carga = carga; }

    public Orbita getOrbita() { return orbita; }
    public void setOrbita(Orbita orbita) { this.orbita = orbita; }

    public StatusFoguete getStatus() { return status; }
    public void setStatus(StatusFoguete status) { this.status = status; }

    public Satelite getSateliteCarregado() { return sateliteCarregado; }
    public void setSateliteCarregado(Satelite s) { this.sateliteCarregado = s; }

    public float getDistanciaMaxima() { return distanciaMaxima; }
}