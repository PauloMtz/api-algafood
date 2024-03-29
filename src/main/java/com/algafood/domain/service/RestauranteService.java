package com.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algafood.domain.model.Cidade;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.model.FormaPagamento;
import com.algafood.domain.model.Restaurante;
import com.algafood.domain.model.Usuario;
import com.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

    private static final String MSG_REGISTRO_UTILIZADO = 
        "O registro de ID %d está associado a outra entidade e não pode ser removido.";
    
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaService cozinhaService;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private UsuarioService usuarioService;

    /*
     * a anotação transactional serve para manter a consistência das
     * operações no banco de dados. Exemplo: se dentro do método salvar
     * tivesse uma outra operação, e uma delas lançasse uma exceção,
     * isso poderia causar uma inconsistência na operação.
     * Os métodos do spring já são anotados com transactional, mas
     * se houver mais de uma operação no método, é melhor anotar.
     * Inclusive, utilizar o método flush() para descarregar as 
     * alterações pendentes no banco. O método flush() é da JPA.
     */
    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Long cidadeId = restaurante.getEndereco().getCidade().getId();

        Cozinha cozinha = cozinhaService.buscar(cozinhaId);
        Cidade cidade = cidadeService.buscar(cidadeId);

        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);

        return restauranteRepository.save(restaurante);
    }

    /*
     * o método deleteById do spring já é anotado com @Transactional,
     * isso faz com que ele gerencie as exceções da operação.
     * Ao anotar esse método excluir, as exceções aqui não são
     * mais capturadas por este método. Para voltar a capturar, deve-se
     * forçar a JPA a executar as alterações no banco de dados,
     * utilizando o método flush() 
     */
    @SuppressWarnings("null")
    @Transactional
    public void excluir(Long restauranteId) {
        try {
            restauranteRepository.deleteById(restauranteId);
            restauranteRepository.flush();
        } catch (EmptyResultDataAccessException ex) {
            throw new RestauranteNaoEncontradoException(restauranteId);
        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeNaoRemoverException(String.format(
                MSG_REGISTRO_UTILIZADO, restauranteId));
        }
	}

    @SuppressWarnings("null")
    public Restaurante buscar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
			.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}

    @Transactional
    public void ativar(Long restauranteId) {
        Restaurante restaurante = buscar(restauranteId);
        //restaurantePersistido.setAtivo(true);
        restaurante.ativar();
    }

    // foi anotado com transactional porque é um laço em vários elementos
    @Transactional
    public void ativar(List<Long> restaurantesIds) {
        // chama o método ativar para cada elemento da lista
        restaurantesIds.forEach(this::ativar);
    }

    @Transactional
    public void desativar(List<Long> restaurantesIds) {
        
        restaurantesIds.forEach(this::desativar);
    }

    @Transactional
    public void desativar(Long restauranteId) {
        Restaurante restaurante = buscar(restauranteId);
        //restaurante.setAtivo(false);
        restaurante.desativar();
    }

    @Transactional
    public void removerFormaPagamento(Long restauranteId, 
        Long formaPagamentoID) {
        
        Restaurante restaurante = buscar(restauranteId);
        FormaPagamento formaPagamento = formaPagamentoService.buscar(formaPagamentoID);
        
        // não precisa do save (já está em estado gerenciado pela JPA)
        restaurante.removerFormaPagamento(formaPagamento);
    }

    @Transactional
    public void adicionarFormaPagamento(Long restauranteId, 
        Long formaPagamentoID) {
        
        Restaurante restaurante = buscar(restauranteId);
        FormaPagamento formaPagamento = formaPagamentoService.buscar(formaPagamentoID);
        
        // não precisa do save (já está em estado gerenciado pela JPA)
        restaurante.adicionarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void abrir(Long restauranteId) {
        Restaurante persistido = buscar(restauranteId);
        
        persistido.abrir();
    }

    @Transactional
    public void fechar(Long restauranteId) {
        Restaurante persistido = buscar(restauranteId);
        
        persistido.fechar();
    }

    @Transactional
    public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscar(restauranteId);
        Usuario usuario = usuarioService.buscar(usuarioId);
        
        restaurante.removerResponsavel(usuario);
    }

    @Transactional
    public void associarResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscar(restauranteId);
        Usuario usuario = usuarioService.buscar(usuarioId);
        
        restaurante.adicionarResponsavel(usuario);
    }
}
