package com.algafood.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeDto {
    
    private Long id;
    private String nome;
    private EstadoDto estado;
}
