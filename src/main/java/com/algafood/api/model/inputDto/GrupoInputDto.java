package com.algafood.api.model.inputDto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrupoInputDto {
    
    @NotBlank
    private String nome;
}
