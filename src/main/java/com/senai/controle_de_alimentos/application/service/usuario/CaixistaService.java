package com.senai.controle_de_alimentos.application.service.usuario;

import com.senai.controle_de_alimentos.application.dto.usuario.CaixistaDTO;
import com.senai.controle_de_alimentos.domain.repository.usuario.CaixistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CaixistaService {
    @Autowired
    private CaixistaRepository caixistaRepo;

    public CaixistaDTO.CaixistaResponseDTO cadastrarCaixista(CaixistaDTO.CaixistaRequestDTO caixaDTO) {
        return CaixistaDTO.CaixistaResponseDTO.fromEntity(caixistaRepo.save(caixaDTO.toEntity()));
    }

    public List<CaixistaDTO.CaixistaResponseDTO> listarCaixistas() {
        return caixistaRepo.findAll().stream().map(CaixistaDTO.CaixistaResponseDTO::fromEntity).toList();
    }
}
