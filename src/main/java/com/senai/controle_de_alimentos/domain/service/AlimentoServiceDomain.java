package com.senai.controle_de_alimentos.domain.service;

import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import com.senai.controle_de_alimentos.domain.entity.Alimento;
import com.senai.controle_de_alimentos.domain.repository.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AlimentoServiceDomain {
    @Autowired
    private AlimentoRepository alimentoRepo;

    public boolean testarCodigoBarras(AlimentoDTO dto) {
        return
                alimentoRepo.findByCodigoDeBarras(dto.codigoDeBarras()).isPresent();
    }

    public boolean dataValidadeValida(AlimentoDTO dto) {
        return !dto.dataValidade().isBefore(LocalDate.now());
    }
}
