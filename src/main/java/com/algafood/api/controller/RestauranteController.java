package com.algafood.api.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.RestauranteDtoAssembler;
import com.algafood.api.assembler.RestauranteInputDtoDisassembler;
import com.algafood.api.model.dto.RestauranteDto;
import com.algafood.api.model.inputDto.RestauranteInputDto;
import com.algafood.api.model.jsonView.IRestauranteView;
import com.algafood.core.validation.ValidacaoException;
import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.NegocioException;
import com.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algafood.domain.model.Restaurante;
import com.algafood.domain.repository.RestauranteRepository;
import com.algafood.domain.service.RestauranteService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private RestauranteService service;

    @Autowired
    private SmartValidator validator;

    @Autowired
    private RestauranteDtoAssembler assembler;

    @Autowired
    private RestauranteInputDtoDisassembler disassembler;

    /*@GetMapping
    public List<RestauranteDto> listar() {
        return assembler.convertToCollectionDto(repository.findAll());
    }*/

    @GetMapping
    public MappingJacksonValue listar(
        @RequestParam(required = false) String projecao) {

        List<Restaurante> restaurantes = repository.findAll();
        List<RestauranteDto> restauranteDto = assembler.convertToCollectionDto(restaurantes);
        MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restauranteDto);
        
        if ("apenas-nome".equals(projecao)) {
            restaurantesWrapper.setSerializationView(IRestauranteView.ApenasNome.class);
        } else if ("resumo".equals(projecao)) {
            restaurantesWrapper.setSerializationView(IRestauranteView.Resumo.class);
        }

        return restaurantesWrapper;
    }

    /*  URLs:
        http://localhost:8080/restaurantes?projecao=apenas-nome
        http://localhost:8080/restaurantes?projecao=resumo
        http://localhost:8080/restaurantes
    */

    /*@JsonView(IRestauranteView.Resumo.class)
    @GetMapping(params = "projecao=resumo")
    public List<RestauranteDto> listarResumo() {
        return listar();
    }

    @JsonView(IRestauranteView.ApenasNome.class)
    @GetMapping(params = "projecao=apenas-nome")
    public List<RestauranteDto> listarApenasNome() {
        return listar();
    }*/

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RestauranteDto adicionar(@RequestBody 
        @Valid RestauranteInputDto restauranteInputDto) {

        try {
            Restaurante restaurante = disassembler.convertToDomainObject(restauranteInputDto);
            
            // a classe RestauranteService fica isolada das classes Dto
            return assembler.convertToDto(service.salvar(restaurante));
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @GetMapping("/{restauranteId}")
    public RestauranteDto buscarPorId(@PathVariable("restauranteId") Long id) {
        Restaurante restaurante = service.buscar(id);

        RestauranteDto restauranteDto = assembler.convertToDto(restaurante);
        
        return restauranteDto;
    }

    @GetMapping("/taxa-frete")
    public List<RestauranteDto> porTaxaFrete(BigDecimal taxaIni, BigDecimal taxaFim) {
        // url: http://localhost:8080/restaurantes/taxa-frete?taxaIni=10&taxaFim=13
        return assembler.convertToCollectionDto(repository.findByTaxaFreteBetween(taxaIni, taxaFim));
    }

    @PutMapping("/{restauranteId}")
    public RestauranteDto atualizar(@PathVariable Long restauranteId, 
        @RequestBody @Valid RestauranteInputDto restauranteInputDto) {

        try {
            //Restaurante restaurante = disassembler.convertToDomainObject(restauranteInputDto);

            Restaurante persistido = service.buscar(restauranteId);

            disassembler.copyToDomainObject(restauranteInputDto, persistido);
				
            /*BeanUtils.copyProperties(restaurante, persistido,
                "id", "formasPagamento", "endereco", "dataCadastro");*/

            return assembler.convertToDto(service.salvar(persistido));
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Long restauranteId) {
        service.ativar(restauranteId);
    }

    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restaurantesIds) {
        
        try {
            service.ativar(restaurantesIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarMultiplos(@RequestBody List<Long> restaurantesIds) {
        
        try {
            service.desativar(restaurantesIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long restauranteId) {
        
        service.desativar(restauranteId);
    }

    @PutMapping("/{restauranteId}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrir(@PathVariable Long restauranteId) {
        service.abrir(restauranteId);
    }

    @PutMapping("/{restauranteId}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fechar(@PathVariable Long restauranteId) {
        service.fechar(restauranteId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{restauranteId}")
	public void remover(@PathVariable Long restauranteId) {
        service.excluir(restauranteId);
	}

    @PatchMapping("/{restauranteId}")
	public RestauranteDto atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos, HttpServletRequest request) {
        
		Restaurante restauranteAtual = service.buscar(restauranteId);
		
		merge(campos, restauranteAtual, request);

        validate(restauranteAtual, "restaurante");
		
		return atualizar(restauranteId, assembler.convertToInputObject(restauranteAtual));
	}

    private void validate(Restaurante restaurante, String object) {
        BeanPropertyBindingResult bindingResult = 
            new BeanPropertyBindingResult(restaurante, object);
        
        validator.validate(restaurante, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidacaoException(bindingResult);
        }
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino,
			HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
			
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
}
