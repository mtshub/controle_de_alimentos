package com.senai.controle_de_alimentos.interface_ui.controller;

import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import com.senai.controle_de_alimentos.application.service.AlimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alimentos")
public class AlimentoController {
    @Autowired
    AlimentoService alimentoService;

    @PostMapping
    public ResponseEntity<Void> cadastrarAlimento(@RequestBody AlimentoDTO dto) {
        if (alimentoService.cadastrarAlimento(dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<AlimentoDTO>> listarAlimentos() {
        return ResponseEntity.ok(alimentoService.listarAlimentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlimentoDTO> buscarAlimento(@PathVariable Long id) {
        return alimentoService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarAlimento(@PathVariable Long id, @RequestBody AlimentoDTO dto) {
        if (alimentoService.atualizarAlimento(id, dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAlimento(@PathVariable Long id) {
        if (alimentoService.deletarAlimento(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
