package com.algafood.api.exceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algafood.core.validation.ValidacaoException;
import com.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algafood.domain.exception.EntidadeNaoRemoverException;
import com.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_FINAL_USER_ERROR = "Ocorreu um erro interno "
		+ "na aplicação. Contate o administrador do sistema";

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return ResponseEntity.status(status).headers(headers).build();
	}

	// método sobrescrito - digite handleBindException e o ide irá sugerir opções
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, 
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// getBindingResult() já utilizado em outros métodos abaixo
		// é um método de BindException()
		ex.getBindingResult();
		
		// essa chamada abaixo dará erros, e lançará exceção
		// http://localhost:8080/pedidos?restauranteId=1&clienteId=aa&dataInicio=2019-10-30T12:00:00(Z)&dataFim=2019-10-31T12:00:00(Z)
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}

	@ExceptionHandler({ ValidacaoException.class })
	public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, 
		WebRequest request) {

		return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), 
			HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {

	    return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		TypeMessage typeMessage = TypeMessage.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. "
			+ "Faça o preenchimento correto e tente novamente.";
	    
	    List<CustomMessage.Object> problemObjects = bindingResult.getAllErrors().stream()
	    		.map(objectError -> {
	    			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
	    			
	    			String name = objectError.getObjectName();
	    			
	    			if (objectError instanceof FieldError) {
	    				name = ((FieldError) objectError).getField();
	    			}
	    			
	    			return CustomMessage.Object.builder()
	    				.name(name)
	    				.userMessage(message)
	    				.build();
	    		})
	    		.collect(Collectors.toList());
	    
		CustomMessage customMessage = createCustomMessageBuiler(status, typeMessage, detail)
	        .userMessage(detail)
	        .objects(problemObjects)
	        .build();
	    
	    return handleExceptionInternal(ex, customMessage, headers, status,
			request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, 
		WebRequest request) {
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
		TypeMessage typeMessage = TypeMessage.ERRO_DE_SISTEMA;
		String detail = "Ocorreu um erro interno inesperado no sistema. "
				+ "Tente novamente e se o problema persistir, entre em contato "
				+ "com o administrador do sistema.";
		
		CustomMessage customMessage = createCustomMessageBuiler(status, 
			typeMessage, detail).build();

		return handleExceptionInternal(ex, customMessage, new HttpHeaders(),
			status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, 
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch(
				(MethodArgumentTypeMismatchException) ex, headers, status, 
					request);
		}
	
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		TypeMessage typeMessage = TypeMessage.PARAMETRO_INVALIDO;

		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		CustomMessage customMessage = createCustomMessageBuiler(status, 
			typeMessage, detail)
			.userMessage(MSG_FINAL_USER_ERROR)
			.build();

		return handleExceptionInternal(ex, customMessage, headers, status, request);
	}

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
			status, typeMessage, detail)
			.userMessage(MSG_FINAL_USER_ERROR)
			.build();
		
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
			status, typeMessage, detail)
			.userMessage(MSG_FINAL_USER_ERROR)
			.build();
		
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
			status, typeMessage, detail)
			.userMessage(MSG_FINAL_USER_ERROR)
			.build();

		return handleExceptionInternal(ex, customMessage, headers, 
			status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
		NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, 
		WebRequest request) {
		
		TypeMessage typeMessage = TypeMessage.RECURSO_NAO_ENCONTRADO;
		String detail = String.format(
			"O recurso '%s', que você tentou acessar, é inexistente.", 
			ex.getRequestURL());
		
		CustomMessage customMessage = createCustomMessageBuiler(status, 
			typeMessage, detail)
			.userMessage(MSG_FINAL_USER_ERROR)
			.build();
		
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
				.timestamp(OffsetDateTime.now())
				.title(status.getReasonPhrase())
				.status(status.value())
				.build();
		} else if (body instanceof String) {
			body = CustomMessage.builder()
				.timestamp(OffsetDateTime.now())
				.title((String) body)
				.status(status.value())
				.build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private CustomMessage.CustomMessageBuilder createCustomMessageBuiler(
		HttpStatus status, TypeMessage typeMessage, String detail) {

		return CustomMessage.builder()
			.timestamp(OffsetDateTime.now())
			.status(status.value())
			.type(typeMessage.getUri())
			.title(typeMessage.getTitle())
			.detail(detail);
	}
}
