package com.senai.controle_de_alimentos.application.service;

import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import com.senai.controle_de_alimentos.domain.entity.Alimento;
import com.senai.controle_de_alimentos.domain.exception.CodigoDeBarrasExistenteException;
import com.senai.controle_de_alimentos.domain.exception.DataValidadeInvalidaException;
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

    public Alimento cadastrarAlimento(AlimentoDTO dto) {
        validarDados(dto);
        return alimentoRepo.save(dto.fromDTO());
    }

    public List<AlimentoDTO> listarAlimentos() {
        List<Alimento> alimentos = alimentoRepo.findAllByStatusTrue();
        return alimentos.stream()
                .map(AlimentoDTO::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlimentoDTO> buscarPorId(Long id) {
        return alimentoRepo.findById(id).filter(Alimento::isStatus).map(AlimentoDTO::toDTO);
    }

    public Optional<Alimento> atualizarAlimento(Long id, AlimentoDTO dto) {
        return alimentoRepo.findById(id).filter(Alimento::isStatus).map(alimento -> {
            atualizarInfos(alimento, dto);
            alimentoRepo.save(alimento);
            return Optional.of(alimento);
        }).orElse(Optional.empty());
    }

    public Optional<Alimento> deletarAlimento(Long id) {
        Optional<Alimento> alimentoOpt = alimentoRepo.findById(id);
        if(alimentoOpt.isPresent()) {
            Alimento alimento = alimentoOpt.get();
            alimento.setStatus(false);
            alimentoRepo.save(alimento);
            return Optional.of(alimento);
        }
        return Optional.empty();
    }

    public void atualizarInfos(Alimento alimento, AlimentoDTO dto) {
        alimento.setNomeAlimento(dto.nomeAlimento());
        alimento.setPreco(dto.preco());
    }

    public void validarDados(AlimentoDTO dto) {
        boolean codigoBarrasValido = !alimentoService.testarCodigoBarras(dto);
        if (!codigoBarrasValido) {
            throw new CodigoDeBarrasExistenteException("Código de barras já cadastrado em outro produto.");
        }

        boolean dataValidadeValida = alimentoService.dataValidadeValida(dto);
        if (!dataValidadeValida) {
            throw new DataValidadeInvalidaException("Data de validade deve ser em um dia futuro.");
        }
    }
}
