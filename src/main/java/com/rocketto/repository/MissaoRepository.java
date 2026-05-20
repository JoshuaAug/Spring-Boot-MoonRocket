package com.rocketto.repository;

import com.rocketto.model.Missao;
import com.rocketto.enums.StatusMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {
    List<Missao> findByStatus(StatusMissao status);
}
