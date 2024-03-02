package com.algafood.api.model.inputDto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSenhaInputDto extends UsuarioInputDto {
    
    // junta os atributos de usuário mais a senha
    @NotBlank
    private String senha;
}
