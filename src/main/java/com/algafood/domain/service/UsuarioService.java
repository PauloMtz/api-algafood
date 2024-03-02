package com.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.exception.NegocioException;
import com.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algafood.domain.model.Usuario;
import com.algafood.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    UsuarioRepository repository;

    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }
    
    @Transactional
    public void alteraSenha(Long usuarioId, String senhaAtual, 
        String novaSenha) {
        
        Usuario usuario = buscar(usuarioId);
        
        if (usuario.senhaNaoCoincide(senhaAtual)) {
            throw new NegocioException("Senha incorreta");
        }
        
        usuario.setSenha(novaSenha);
    }

    public Usuario buscar(Long usuarioId) {
        return repository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }
}
