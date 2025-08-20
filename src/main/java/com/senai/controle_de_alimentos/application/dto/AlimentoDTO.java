package com.senai.controle_de_alimentos.application.dto;

import com.senai.controle_de_alimentos.domain.entity.Alimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.aspectj.bridge.IMessage;

import java.time.LocalDate;

public record AlimentoDTO(
        @Schema(description = "ID do produto", example = "1")
        Long id,

        @NotNull(message = "O código de barras é obrigatório")
        @Schema(description = "Código de barras do alimento", example = "12345678")
        Integer codigoDeBarras,

        @NotBlank(message = "O nome do alimento deve ser preenchido")
        @Schema(description = "Nome do alimento", example = "Picanha")
        String nomeAlimento,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que 0")
        @Schema(description = "Preço do alimento", example = "59.99")
        double preco,


        @PastOrPresent(message = "A data de fabricação não pode ser futura")
        @Schema(description = "Data de fabricação do alimento. Formato: ano-mes-dia (AAAA-MM-DD)", example = "2025-08-10")
        LocalDate dataFabricacao,

        @Future(message = "A data de validade deve ser futura.")
        @Schema(description = "Data de validade do alimento. Formato: ano-mes-dia (AAAA-MM-DD)", example = "2028-08-10")
        LocalDate dataValidade
) {
    public static AlimentoDTO toDTO(Alimento alimento) {
        return new AlimentoDTO(
                alimento.getId(),
                alimento.getCodigoDeBarras(),
                alimento.getNomeAlimento(),
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
                preco,
                dataFabricacao,
                dataValidade,
                true
        );
    }
}
