package com.rocketto.repository;

import com.rocketto.model.Satelite;
import com.rocketto.enums.StatusSatelite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SateliteRepository extends JpaRepository<Satelite, Long> {
    Optional<Satelite> findByNome(String nome);
    List<Satelite> findByStatus(StatusSatelite status);
    boolean existsByNome(String nome);
}
