package com.senai.controle_de_alimentos;

import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import com.senai.controle_de_alimentos.application.service.AlimentoService;
import com.senai.controle_de_alimentos.domain.entity.Alimento;
import com.senai.controle_de_alimentos.domain.repository.AlimentoRepository;
import com.senai.controle_de_alimentos.domain.service.AlimentoServiceDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ControleDeAlimentosApplicationTests {
    @Mock
    private AlimentoRepository repository;

    @Mock
    private AlimentoServiceDomain alimentoServiceDomain;

    @InjectMocks
    private AlimentoService service;

    @Test
    void contextLoads() {
    }

    @Test
    void deveSalvarAlimentoValido() {
        AlimentoDTO alimento = new AlimentoDTO(
                null,
                12345,
                "Arroz 1KG",
                12.99,
                LocalDate.now().minusDays(15),
                LocalDate.now().plusDays(15)
        );

        when(alimentoServiceDomain.testarCodigoBarras(alimento)).thenReturn(false);
        when(alimentoServiceDomain.dataValidadeValida(alimento)).thenReturn(true);

        when(repository.save(alimento.fromDTO())).thenReturn(alimento.fromDTO());

        Alimento alimentoSalvo = service.cadastrarAlimento(alimento);

        assertNotNull(alimentoSalvo);

        verify(repository).save(alimento.fromDTO());
    }

}
