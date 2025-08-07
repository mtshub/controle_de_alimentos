package com.senai.controle_de_alimentos.application.dto;

import com.senai.controle_de_alimentos.domain.entity.Alimento;

import java.time.LocalDate;

public record AlimentoDTO(
        Long id,
        Integer codigoDeBarras,
        String nomeAlimento,
        int qtdEstoque,
        float preco,
        LocalDate dataFabricacao,
        LocalDate dataValidade
) {
    public static AlimentoDTO toDTO(Alimento alimento) {
        return new AlimentoDTO(
                alimento.getId(),
                alimento.getCodigoDeBarras(),
                alimento.getNomeAlimento(),
                alimento.getQtdEstoque(),
                alimento.getPreco(),
                alimento.getDataFabricacao(),
                alimento.getDataValidade()
                );
    }

    public Alimento fromDTO() {
        return new Alimento(
                id,
                codigoDeBarras,
                nomeAlimento,
                qtdEstoque,
                preco,
                dataFabricacao,
                dataValidade,
                true
        );
    }
}
