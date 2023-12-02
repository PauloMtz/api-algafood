package com.algafood.testes;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algafood.ApiAlgafoodApplication;
import com.algafood.domain.model.Cozinha;
import com.algafood.domain.repository.CozinhaRepository;

public class BuscaCozinhaMain {
    
    public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(ApiAlgafoodApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);
		
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		
		Cozinha cozinha = cozinhaRepository.buscar(1L);
		
		System.out.println(">>> Cozinha ID 01: " + cozinha.getNome());
	}
}
