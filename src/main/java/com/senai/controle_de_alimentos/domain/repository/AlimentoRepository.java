package com.senai.controle_de_alimentos.domain.repository;

import com.senai.controle_de_alimentos.domain.entity.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {
    List<Alimento> findAllByStatusTrue();
    Optional<Alimento> findByCodigoDeBarras(Integer codigo);
}
