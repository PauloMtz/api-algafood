package com.algafood.api.model.inputDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioInputDto {
    
    @NotBlank
    private String nome;
    
    @NotBlank
    @Email
    private String email;
}
