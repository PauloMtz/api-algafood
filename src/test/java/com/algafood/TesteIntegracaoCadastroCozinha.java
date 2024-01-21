package com.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.algafood.domain.model.Cozinha;
import com.algafood.domain.service.CozinhaService;

@SpringBootTest
public class TesteIntegracaoCadastroCozinha {

    @Autowired
    private CozinhaService service;
    
    @Test
    public void testeCadastroCozinhaComSucesso() {
        // 1. cenário - caminho feliz (não dar erro)
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        // 2. ação
        service.salvar(novaCozinha);

        // 3. validação
        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();
    }

    @Test
	public void testeCadastroCozinhaSemNome() {
        // 1. cenário - caminho infeliz (dar erro)
		Cozinha novaCozinha = new Cozinha();

        // 2. ação
		novaCozinha.setNome(null);

        // lança exceção
		ConstraintViolationException erroEsperado =
            Assertions.assertThrows(
                ConstraintViolationException.class, () -> {
					service.salvar(novaCozinha);
		});

        // validação
		assertThat(erroEsperado).isNotNull();
	}
}
