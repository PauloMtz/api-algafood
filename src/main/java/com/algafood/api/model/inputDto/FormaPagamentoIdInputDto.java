package com.algafood.api.model.inputDto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoIdInputDto {
    
    @NotNull
    private Long id;
}
