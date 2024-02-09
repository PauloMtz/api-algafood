package com.algafood.core.jackson;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class JacksonMixinModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

    // construtor padrão
    // informa que a classe domain tem uma classe "mix in"
	public JacksonMixinModule() {
		// se quiser adicionar outras classes
		// setMixInAnnotation(ClasseDomain.class, ClasseMixIn.class);
	}
}
