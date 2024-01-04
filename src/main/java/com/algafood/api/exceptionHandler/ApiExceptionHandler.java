package com.algafood.api.exceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex, HttpHeaders headers, 
		HttpStatus status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException(
				(InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request); 
		}

		TypeMessage typeMessage = TypeMessage.RECURSO_NAO_IDENTIFICADO;
		String detail = "Corpo da requisição inválido. Verifique a sintaxe.";

		CustomMessage customMessage = createCustomMessageBuiler(
			status, typeMessage, detail).build();
		
		return handleExceptionInternal(ex, customMessage, headers, 
			status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(
		PropertyBindingException ex, HttpHeaders headers, HttpStatus status,
		WebRequest request) {
	
		String path = joinPath(ex.getPath());
		
		TypeMessage typeMessage = TypeMessage.MENSAGEM_INCOMPREENSIVEL;

		String detail = String.format("A propriedade '%s' não existe. "
			+ "Corrija ou remova essa propriedade e tente novamente.", path);

		CustomMessage customMessage = createCustomMessageBuiler(
			status, typeMessage, detail).build();
		
		return handleExceptionInternal(ex, customMessage, headers, status, 
			request);
	}
    
    private String joinPath(List<Reference> references) {
		return references.stream()
			.map(ref -> ref.getFieldName())
			.collect(Collectors.joining("."));
	}

	private ResponseEntity<Object> handleInvalidFormatException(
		InvalidFormatException ex, HttpHeaders headers, HttpStatus status, 
		WebRequest request) {
		
		String path = ex.getPath().stream()
			.map(ref -> ref.getFieldName())
			.collect(Collectors.joining("."));

		TypeMessage typeMessage = TypeMessage.RECURSO_NAO_IDENTIFICADO;
		String detail = String.format("A propriedade '%s' recebeu o valor "
			+ "'%s' que é um tipo inválido. Informe um valor compatível "
			+ "com o tipo %s.", path, ex.getValue(), 
			ex.getTargetType().getSimpleName());
		
		CustomMessage customMessage = createCustomMessageBuiler(
			status, typeMessage, detail).build();

		return handleExceptionInternal(ex, customMessage, headers, 
			status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(
			EntidadeNaoEncontradaException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		TypeMessage typeMessage = TypeMessage.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();

		CustomMessage customMessage = createCustomMessageBuiler(
			status, typeMessage, detail).build();

		return handleExceptionInternal(ex, customMessage, new HttpHeaders(),
			status, request);
	}

	@ExceptionHandler(EntidadeNaoRemoverException.class)
	public ResponseEntity<?> handleEntidadeNaoRemoverException(
			EntidadeNaoRemoverException ex, WebRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;
		TypeMessage typeMessage = TypeMessage.RECURSO_NAO_PODE_SER_REMOVIDO;
		String detail = ex.getMessage();

		CustomMessage customMessage = createCustomMessageBuiler(
			status, typeMessage, detail).build();

		return handleExceptionInternal(ex, customMessage, new HttpHeaders(), 
			status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, 
		WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		TypeMessage typeMessage = TypeMessage.RECURSO_INVALIDO;
		String detail = ex.getMessage();

		CustomMessage customMessage = createCustomMessageBuiler(
			status, typeMessage, detail).build();
			
		return handleExceptionInternal(ex, customMessage, new HttpHeaders(), 
			status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
			org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = CustomMessage.builder()
				.title(status.getReasonPhrase())
				.status(status.value())
				.build();
		} else if (body instanceof String) {
			body = CustomMessage.builder()
				.title((String) body)
				.status(status.value())
				.build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private CustomMessage.CustomMessageBuilder createCustomMessageBuiler(
		HttpStatus status, TypeMessage typeMessage, String detail) {

		return CustomMessage.builder()
			.status(status.value())
			.type(typeMessage.getUri())
			.title(typeMessage.getTitle())
			.detail(detail);
	}
}
