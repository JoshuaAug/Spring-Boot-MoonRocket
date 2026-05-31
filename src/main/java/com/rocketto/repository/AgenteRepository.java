package com.rocketto.repository;

import com.rocketto.enums.StatusAgente;
import com.rocketto.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenteRepository extends JpaRepository<Agente, Long> {
    List<Agente> findByStatus(StatusAgente status);
}