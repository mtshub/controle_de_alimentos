package com.senai.controle_de_alimentos.interface_ui.controller;

import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import com.senai.controle_de_alimentos.application.service.AlimentoService;
import com.senai.controle_de_alimentos.domain.entity.Alimento;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alimentos")
public class AlimentoController {
    @Autowired
    AlimentoService alimentoService;

    @Operation(
            summary = "Cadastrar um novo alimento",
            description = "Adiciona um novo alimento à base de dados após validações de código de barras e validade",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AlimentoDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "codigoDeBarras": 98765432,
                                        "nomeAlimento": "Suco de Laranja Natural 1L",
                                        "preco": 12.5,
                                        "dataFabricacao":"2025-08-10",
                                        "dataValidade": "2025-09-01"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Alimento cadastrado com sucesso!"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Violação de regras de negócio. Verifique os dados e tente novamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Código de barras existente", value = "Código de barras já cadastrado em outro produto."),
                                            @ExampleObject(name = "Data de validade inválida", value = "Data de validade deve ser em um dia futuro.")
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<AlimentoDTO> cadastrarAlimento(@Valid @org.springframework.web.bind.annotation.RequestBody AlimentoDTO dto) {
        return ResponseEntity.status(201).body(AlimentoDTO.toDTO(alimentoService.cadastrarAlimento(dto)));
    }

    @Operation(
            summary = "Listar todos os alimentos",
            description = "Retorna todos os alimentos cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de alimentos retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<AlimentoDTO>> listarAlimentos() {
        return ResponseEntity.ok(alimentoService.listarAlimentos());
    }

    @Operation(
            summary = "Buscar alimento pelo ID",
            description = "Retorna um alimento existente a partir do seu ID",
            parameters = {
                    @Parameter(name = "id", description = "ID do alimento a ser buscado", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Alimento encontrado"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Alimento não encontrado",
                            content = @Content(
                                mediaType = "application/json",
                                examples = @ExampleObject(value = "Alimento com o id fornecido não encontrado")
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AlimentoDTO> buscarAlimento(@PathVariable Long id) {
        return alimentoService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar um alimento",
            description = "Atualiza os dados de um alimento existente com novas informações",
            parameters = {
                    @Parameter(name = "id", description = "ID do alimento a ser atualizado")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AlimentoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                        "nomeAlimento": "Batata frita",
                                        "preco": 16.0
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Alimento atualizado com sucesso!"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Alimento não encontrado.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "Alimento com o ID fornecido não encontrado.")
                    ))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarAlimento(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody AlimentoDTO dto) {
        if (alimentoService.atualizarAlimento(id, dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Deletar um alimento",
            description = "Remove um alimento da base de dados a partir do seu ID",
            parameters = {
                    @Parameter(name = "id", description = "ID do alimento a ser deletado", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Alimento removido com sucesso!"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Alimento não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Alimento com o id fornecido não encontrado.")
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAlimento(@PathVariable Long id) {
        alimentoService.deletarAlimento(id);
        return ResponseEntity.noContent().build();
    }
}
