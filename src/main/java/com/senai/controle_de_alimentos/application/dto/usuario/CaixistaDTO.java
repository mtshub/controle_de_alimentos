package com.senai.controle_de_alimentos.application.dto.usuario;

import com.senai.controle_de_alimentos.domain.entity.usuario.Caixista;
import com.senai.controle_de_alimentos.domain.enums.Role;

public class CaixistaDTO {

    public record CaixistaRequestDTO(
           String nome,
           String email,
           Double salario,
           String senha,
           Role role
    ){
        public Caixista toEntity() {
            return Caixista.builder()
                    .nome(nome)
                    .email(email)
                    .salario(salario)
                    .senha(senha)
                    .role(role)
                    .build();
        }
    }

    public record CaixistaResponseDTO(
            Long id,
            String nome,
            String email,
            Double salario
    ){
        public static CaixistaResponseDTO fromEntity(Caixista caixista) {
            return new CaixistaResponseDTO(
                    caixista.getId(),
                    caixista.getNome(),
                    caixista.getEmail(),
                    caixista.getSalario());
        }
    }
}
