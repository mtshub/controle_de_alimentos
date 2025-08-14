package com.senai.controle_de_alimentos.application.service;

import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import com.senai.controle_de_alimentos.domain.entity.Alimento;
import com.senai.controle_de_alimentos.domain.repository.AlimentoRepository;
import com.senai.controle_de_alimentos.domain.service.AlimentoServiceDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlimentoService {
    @Autowired
    private AlimentoRepository alimentoRepo;

    @Autowired
    private AlimentoServiceDomain alimentoService;

    public boolean cadastrarAlimento(AlimentoDTO dto) {

        boolean codigoBarrasValido = !alimentoService.testarCodigoBarras(dto);
        boolean dataValidadeValida = alimentoService.dataValidadeValida(dto);

        if (codigoBarrasValido && dataValidadeValida) {
            alimentoRepo.save(dto.fromDTO());
            return true;
        }
        return false;
    }

    public List<AlimentoDTO> listarAlimentos() {
        return alimentoRepo.findAllByStatusTrue().stream()
                .map(AlimentoDTO::toDTO).collect(Collectors.toList());
    }

    public Optional<AlimentoDTO> buscarPorId(Long id) {
        return alimentoRepo.findById(id).filter(Alimento::isStatus).map(AlimentoDTO::toDTO);
    }

    public boolean atualizarAlimento(Long id, AlimentoDTO dto) {
        return alimentoRepo.findById(id).filter(Alimento::isStatus).map(alimento -> {
            atualizarInfos(alimento, dto);
            alimentoRepo.save(alimento);
            return true;
        }).orElse(false);
    }

    public boolean deletarAlimento(Long id) {
        return alimentoRepo.findById(id).map(alimento -> {
            alimento.setStatus(false);
            alimentoRepo.save(alimento);
            return true;
        }).orElse(false);
    }

    public void atualizarInfos(Alimento alimento, AlimentoDTO dto) {
        alimento.setNomeAlimento(dto.nomeAlimento());
        alimento.setPreco(dto.preco());
    }
}
