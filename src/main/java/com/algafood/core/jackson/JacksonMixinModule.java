package com.algafood.core.jackson;

import org.springframework.stereotype.Component;

import com.algafood.api.model.mixin.RestauranteMixIn;
import com.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class JacksonMixinModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

    // construtor padrão
    // informa que a classe domain tem uma classe "mix in"
	public JacksonMixinModule() {
		setMixInAnnotation(Restaurante.class, RestauranteMixIn.class);

		// se quiser adicionar outras classes
		// setMixInAnnotation(ClasseDomain.class, ClasseMixIn.class);
	}
}
