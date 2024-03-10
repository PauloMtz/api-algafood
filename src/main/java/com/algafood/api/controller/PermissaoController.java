package com.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.PermissaoDtoAssembler;
import com.algafood.api.model.dto.PermissaoDto;
import com.algafood.domain.model.Grupo;
import com.algafood.domain.service.GrupoService;

@RestController
@RequestMapping(value = "/grupos/{grupoId}/permissoes")
public class PermissaoController {
    
    @Autowired
	private GrupoService grupoService;
	
	@Autowired
	private PermissaoDtoAssembler assembler;
	
	@GetMapping
	public List<PermissaoDto> listar(@PathVariable Long grupoId) {
		Grupo grupo = grupoService.buscar(grupoId);
		
		return assembler.convertToCollectionDto(grupo.getPermissoes());
	}
	
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable Long grupoId, 
        @PathVariable Long permissaoId) {
		
        grupoService.desassociarPermissao(grupoId, permissaoId);
	}
	
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long grupoId, 
        @PathVariable Long permissaoId) {
		
        grupoService.associarPermissao(grupoId, permissaoId);
	}
}
