package com.algafood.testes;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algafood.ApiAlgafoodApplication;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;

public class ExclusaoCozinhaMain {
    
    public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(ApiAlgafoodApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);
		
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(1L);
		
		cozinhaRepository.remover(cozinha.getId());
	}
}
