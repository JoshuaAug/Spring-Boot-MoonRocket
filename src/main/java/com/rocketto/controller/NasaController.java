package com.rocketto.controller;

import com.rocketto.dto.NasaAsteroidDTO;
import com.rocketto.service.NasaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/nasa")
@CrossOrigin(origins = "*")
public class NasaController {

    private final NasaService service;

    public NasaController(NasaService service) {
        this.service = service;
    }

    // GET /api/nasa/asteroides?dataInicio=2024-01-01&dataFim=2024-01-07
    @GetMapping("/asteroides")
    public List<NasaAsteroidDTO> buscar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        // Se dataFim não for informada, usa 7 dias após dataInicio (limite da API da NASA)
        if (dataFim == null) dataFim = dataInicio.plusDays(7);

        return service.buscarAsteroides(dataInicio.toString(), dataFim.toString());
    }
}