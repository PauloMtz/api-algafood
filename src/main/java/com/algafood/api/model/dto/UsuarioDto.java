package com.algafood.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {
    
    private Long id;
    private String nome;
    private String email;
}
