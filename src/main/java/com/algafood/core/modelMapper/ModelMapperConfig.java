package com.algafood.core.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algafood.api.model.dto.EnderecoDto;
import com.algafood.api.model.inputDto.ItemPedidoInputDto;
import com.algafood.domain.model.Endereco;
import com.algafood.domain.model.ItemPedido;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {

        var modelMapper = new ModelMapper();

        modelMapper.createTypeMap(ItemPedidoInputDto.class, ItemPedido.class)
            .addMappings(mapper -> mapper.skip(ItemPedido::setId));

        var enderecoDtoTypeMap = modelMapper.createTypeMap(
            Endereco.class, EnderecoDto.class);

        enderecoDtoTypeMap.<String>addMapping(
            enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),
            (enderecoDtoDest, value) -> enderecoDtoDest.getCidade().setEstado(value));

        return modelMapper;
    }
}
