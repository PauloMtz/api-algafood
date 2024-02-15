package com.algafood.api.model.inputDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeInputDto {
    
    @NotBlank
    private String nome;
    
    @Valid
    @NotNull
    private EstadoIdInputDto estado;
}
