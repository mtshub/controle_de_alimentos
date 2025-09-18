package com.senai.controle_de_alimentos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControleDeAlimentosIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveSalvarAlimentoValido() throws Exception {
        var dto = new AlimentoDTO(
                null,
                12345,
                "Suco de Maracujá",
                12.99,
                LocalDate.now().minusDays(15),
                LocalDate.now().plusDays(15)
        );
        System.out.println(objectMapper.writeValueAsString(dto));

        mockMvc.perform(
                        post("/alimentos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect((jsonPath("$.nomeAlimento").value("Suco de Maracujá")));
    }

    @Test
    void deveRetornarErroSeDataDeValidadeForInvalida() throws Exception {
        var dto = new AlimentoDTO(
                null,
                22345,
                "Suco de Maracujá",
                12.99,
                LocalDate.now().minusDays(15),
                LocalDate.now().minusDays(1)
        );

        mockMvc.perform(
                        post("/alimentos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.erro").value("Data de validade deve ser em um dia futuro.")));
    }

    @Test
    void deveRetornarErroSeCodigoDeBarrasForExistente() throws Exception {
        var dto = new AlimentoDTO(
                null,
                12345,
                "Suco de Maracujá",
                12.99,
                LocalDate.now().minusDays(15),
                LocalDate.now().plusDays(15)
        );
        System.out.println(objectMapper.writeValueAsString(dto));

        mockMvc.perform(
                        post("/alimentos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.erro").value("Código de barras já cadastrado em outro produto.")));
    }

    @Test
    void deveAtualizarAlimento() throws Exception {
        var alimento = mockMvc.perform(
                get("/alimentos/1").contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();
        System.out.println(objectMapper.writeValueAsString(alimento));

        var alimentoSalvo = objectMapper.readValue(alimento, AlimentoDTO.class);

        var dtoAtualizado = new AlimentoDTO(
                null,
                0,
                "Suco de Maracujá - Maritá",
                9.99,
                LocalDate.now(),
                LocalDate.now()
        );


        mockMvc.perform(
                put("/alimentos/" + alimentoSalvo.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado))
        ).andExpect(status().isOk()).andExpect(jsonPath("$.nomeAlimento").value(dtoAtualizado.nomeAlimento()));
    }

    @Test
    void deveDeletarAlimento() throws Exception {
        mockMvc.perform(
                delete("/alimentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }
}
