package org.nasa.rocketto.repository;

import org.nasa.rocketto.model.Foguete;
import org.nasa.rocketto.enums.StatusFoguete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FogueteRepository extends JpaRepository<Foguete, Long> {
    Optional<Foguete> findByNome(String nome);
    List<Foguete> findByStatus(StatusFoguete status);
    boolean existsByNome(String nome);
}
