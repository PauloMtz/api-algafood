package com.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algafood.api.assembler.UsuarioDtoAssembler;
import com.algafood.api.assembler.UsuarioInputDtoDisassembler;
import com.algafood.api.model.dto.UsuarioDto;
import com.algafood.api.model.inputDto.SenhaInputDto;
import com.algafood.api.model.inputDto.UsuarioInputDto;
import com.algafood.api.model.inputDto.UsuarioSenhaInputDto;
import com.algafood.domain.model.Usuario;
import com.algafood.domain.repository.UsuarioRepository;
import com.algafood.domain.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioDtoAssembler assembler;

    @Autowired
    private UsuarioInputDtoDisassembler disassembler;

    @GetMapping
    public List<UsuarioDto> listar() {

        /*List<Usuario> listaTodos = repository.findAll();
        return assembler.convertToCollectionDto(listaTodos);*/

        // é a mesma coisa do código comentado acima
        return assembler.convertToCollectionDto(repository.findAll());
    }

    @ResponseStatus(HttpStatus.CREATED)
	@PostMapping
    public UsuarioDto adicionar(@RequestBody @Valid UsuarioSenhaInputDto usuarioInputDto) {

		Usuario usuario = disassembler.convertToDomainObject(usuarioInputDto);
		usuario = service.salvar(usuario);

		return assembler.convertToDto(usuario);
    }

    @GetMapping("/{usuarioId}")
	public UsuarioDto buscarPorId(@PathVariable("usuarioId") Long id) {

		Usuario usuario = service.buscar(id);

		return assembler.convertToDto(usuario);
	}

    @PutMapping("/{usuarioId}")
	public UsuarioDto atualizar(@PathVariable Long usuarioId, 
		@RequestBody @Valid UsuarioInputDto usuarioInputDto) {

		Usuario persistido = service.buscar(usuarioId);
			
		disassembler.copyToDomainObject(usuarioInputDto, persistido);

		persistido = service.salvar(persistido);

		return assembler.convertToDto(persistido);
	}

    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alteraSenha(@PathVariable Long usuarioId, 
        @RequestBody @Valid SenhaInputDto senha) {
        
        service.alteraSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
    } 
}
