package com.senai.controle_de_alimentos.domain.repository.usuario;

import com.senai.controle_de_alimentos.domain.entity.usuario.Caixista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixistaRepository extends JpaRepository<Caixista, Long> {
}
