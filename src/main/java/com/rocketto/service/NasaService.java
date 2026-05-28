package com.rocketto.service;

import com.rocketto.dto.NasaAsteroidDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NasaService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${nasa.api.key}")
    private String nasaApiKey;

    private static final String NASA_URL =
            "https://api.nasa.gov/neo/rest/v1/feed?start_date={start}&end_date={end}&api_key={key}";

    public NasaService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<NasaAsteroidDTO> buscarAsteroides(String dataInicio, String dataFim) {
        List<NasaAsteroidDTO> resultado = new ArrayList<>();

        try {
            String json = restTemplate.getForObject(
                    NASA_URL,
                    String.class,
                    dataInicio,
                    dataFim,
                    nasaApiKey
            );

            if (json == null) return resultado;

            JsonNode resposta = objectMapper.readTree(json);
            JsonNode porData  = resposta.get("near_earth_objects");
            if (porData == null) return resultado;

            // Converte para Map para iterar as datas com Java nativo
            Map<String, JsonNode> mapaData = objectMapper.convertValue(
                    porData,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, JsonNode.class)
            );

            for (JsonNode listaAsteroides : mapaData.values()) {
                for (int i = 0; i < listaAsteroides.size(); i++) {
                    JsonNode asteroide = listaAsteroides.get(i);
                    try {
                        String id        = asteroide.get("id").asText();
                        String nome      = asteroide.get("name").asText();
                        boolean perigoso = asteroide.get("is_potentially_hazardous_asteroid").asBoolean();

                        JsonNode diam = asteroide.get("estimated_diameter").get("kilometers");
                        double dMin   = diam.get("estimated_diameter_min").asDouble();
                        double dMax   = diam.get("estimated_diameter_max").asDouble();

                        JsonNode aprox    = asteroide.get("close_approach_data").get(0);
                        String dataAprox  = aprox.get("close_approach_date").asText();
                        double velocidade = aprox.get("relative_velocity").get("kilometers_per_hour").asDouble();
                        double distancia  = aprox.get("miss_distance").get("kilometers").asDouble();

                        resultado.add(new NasaAsteroidDTO(
                                id, nome, dMin, dMax, perigoso, dataAprox, velocidade, distancia));
                    } catch (Exception e) {
                        // Ignora asteroides com dados incompletos
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar NASA: " + e.getMessage());
        }

        return resultado;
    }
}