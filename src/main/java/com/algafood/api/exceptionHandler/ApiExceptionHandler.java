package com.algafood.api.exceptionHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> tratarEntidadeNaoEncontradaException(
			EntidadeNaoEncontradaException e) {
		MensagemCustomizada mensagemCustom = MensagemCustomizada.builder()
			.dataHora(LocalDateTime.now())
			.mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(mensagemCustom);
	}

	@ExceptionHandler(EntidadeNaoRemoverException.class)
	public ResponseEntity<?> tratarEntidadeNaoEncontradaException(
			EntidadeNaoRemoverException e) {
		MensagemCustomizada mensagemCustom = MensagemCustomizada.builder()
			.dataHora(LocalDateTime.now())
			.mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(mensagemCustom);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> tratarNegocioException(NegocioException e) {
		MensagemCustomizada mensagemCustom = MensagemCustomizada.builder()
			.dataHora(LocalDateTime.now())
			.mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(mensagemCustom);
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException() {
		MensagemCustomizada mensagemCustom = MensagemCustomizada.builder()
			.dataHora(LocalDateTime.now())
			.mensagem("O tipo de mídia não é suportado.").build();
		
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
			.body(mensagemCustom);
	}
}
