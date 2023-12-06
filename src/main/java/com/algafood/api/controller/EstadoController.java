package com.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.model.EstadosXmlWrapper;
import com.algafood.domain.model.Estado;
import com.algafood.domain.repository.EstadoRepository;

@RestController
@RequestMapping(value = "/estados"/*, produces = MediaType.APPLICATION_XML_VALUE*/)
public class EstadoController {
    
    @Autowired
	private EstadoRepository estadoRepository;
	
	public List<Estado> listar() {
		return estadoRepository.listar();
	}

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public EstadosXmlWrapper listaXml() {
		return new EstadosXmlWrapper(estadoRepository.listar());
	}

	@GetMapping("/{estadoId}")
	public Estado buscarPorId(@PathVariable("estadoId") Long id) {
		return estadoRepository.buscar(id);
	}
}
