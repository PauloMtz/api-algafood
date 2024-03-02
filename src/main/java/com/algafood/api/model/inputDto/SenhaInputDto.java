package com.algafood.api.model.inputDto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenhaInputDto {
    
    @NotBlank
    private String senhaAtual;
    
    @NotBlank
    private String novaSenha;
}
