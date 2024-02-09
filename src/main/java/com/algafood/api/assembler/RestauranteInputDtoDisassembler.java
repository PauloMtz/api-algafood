package com.algafood.api.assembler;

import org.springframework.stereotype.Component;

import com.algafood.api.model.inputDto.RestauranteInputDto;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDtoDisassembler {
    
    public Restaurante convertToDomainObject(RestauranteInputDto restauranteInputDto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(restauranteInputDto.getNome());
        restaurante.setTaxaFrete(restauranteInputDto.getTaxaFrete());

        Cozinha cozinha = new Cozinha();
        cozinha.setId(restauranteInputDto.getCozinha().getId());

        restaurante.setCozinha(cozinha);

        return restaurante;
    }
}
