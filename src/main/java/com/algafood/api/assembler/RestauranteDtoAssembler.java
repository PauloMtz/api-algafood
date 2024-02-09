package com.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algafood.api.model.dto.RestauranteDto;
import com.algafood.api.model.inputDto.CozinhaIdInputDto;
import com.algafood.api.model.inputDto.RestauranteInputDto;
import com.algafood.domain.model.Restaurante;

@Component
public class RestauranteDtoAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public RestauranteDto convertToDto(Restaurante restaurante) {
        
        return modelMapper.map(restaurante, RestauranteDto.class);
    }

    public List<RestauranteDto> convertToCollectionDto(List<Restaurante> restaurantes) {

        return restaurantes.stream()
            .map(restaurante -> convertToDto(restaurante))
            .collect(Collectors.toList());
    }
    
    // método sugerido no fórum da Algaworks para ser utilizado
    // no método de atualizarParcial
    public RestauranteInputDto convertToInputObject(Restaurante restaurante) {
		RestauranteInputDto restauranteInputDto = new RestauranteInputDto();
		restauranteInputDto.setNome(restaurante.getNome());
		restauranteInputDto.setTaxaFrete(restaurante.getTaxaFrete());
		
		CozinhaIdInputDto cozinhaIdInputDto = new CozinhaIdInputDto();
		cozinhaIdInputDto.setId(restaurante.getCozinha().getId());
		
		restauranteInputDto.setCozinha(cozinhaIdInputDto);
		
		return restauranteInputDto;		
	}
}
