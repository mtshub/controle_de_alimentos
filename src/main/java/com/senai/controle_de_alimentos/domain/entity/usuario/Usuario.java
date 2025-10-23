package com.senai.controle_de_alimentos.domain.entity.usuario;

import com.senai.controle_de_alimentos.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank
    @Column(nullable = false)
    protected String nome;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    protected String email;

    @NotBlank
    @Column(nullable = false)
    protected String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;
}
