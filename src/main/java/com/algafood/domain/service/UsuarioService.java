package com.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.exception.NegocioException;
import com.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algafood.domain.model.Grupo;
import com.algafood.domain.model.Usuario;
import com.algafood.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private GrupoService grupoService;

    public Usuario salvar(Usuario usuario) {

        Optional<Usuario> usuarioExistente = repository.findByEmail(usuario.getEmail());

        // verifica se existe o e-mail no banco e seja diferente do atual no caso de atualização
        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
            throw new NegocioException(String.format(
                "O e-mail %s já está cadastrado no sistema", usuario.getEmail()));
        }

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

    @Transactional
    public void desassociarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscar(usuarioId);
        Grupo grupo = grupoService.buscar(grupoId);
        
        usuario.removerGrupo(grupo);
    }

    @Transactional
    public void associarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscar(usuarioId);
        Grupo grupo = grupoService.buscar(grupoId);
        
        usuario.adicionarGrupo(grupo);
    }
}
