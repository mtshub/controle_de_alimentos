package com.senai.controle_de_alimentos.interface_ui.controller.usuario;

import com.senai.controle_de_alimentos.application.dto.usuario.CaixistaDTO;
import com.senai.controle_de_alimentos.application.service.usuario.CaixistaService;
import com.senai.controle_de_alimentos.domain.entity.usuario.Caixista;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/caixista")
@RequiredArgsConstructor
public class CaixistaController {
    private final CaixistaService caixaService;

    @PostMapping
    public ResponseEntity<CaixistaDTO.CaixistaResponseDTO> cadastrarCaixista(@RequestBody CaixistaDTO.CaixistaRequestDTO caixaDTO) {
        CaixistaDTO.CaixistaResponseDTO caixaCriado = caixaService.cadastrarCaixista(caixaDTO);

        return ResponseEntity.created(URI.create("/caixista/" + caixaCriado.id())).body(caixaCriado);
    }

    @GetMapping
    public ResponseEntity<List<CaixistaDTO.CaixistaResponseDTO>> listarCaixistas() {
        return ResponseEntity.ok().body(caixaService.listarCaixistas());
    }
}
