package com.algafood.api.exceptionHandler;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MensagemCustomizada {
    
    private LocalDateTime dataHora;
	private String mensagem;
}
