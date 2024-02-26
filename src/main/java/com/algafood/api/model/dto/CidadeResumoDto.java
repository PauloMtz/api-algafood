package com.algafood.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeResumoDto {
    
    private Long id;
    private String nome;

    // alterando para estado, o lombok
    // utiliza o toString() como retorno
    // por isso, adequação em ModelMapperConfig()
    // no pom.xml a versão do model-mapper foi alterada para 3.0.0
    private String estado;
}
