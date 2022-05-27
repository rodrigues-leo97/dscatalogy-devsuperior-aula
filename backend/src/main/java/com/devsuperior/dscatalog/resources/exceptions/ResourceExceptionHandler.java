package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now()); //para pegar o instante ATUAL da request
		err.setStatus(HttpStatus.NOT_FOUND.value()); //404
		err.setError("Resource not found");
		err.setMessage(e.getMessage()); //pegando a mensagem passada no método findById para quando o erro estourar
		err.setPath(request.getRequestURI()); //pega o caminho da requisição feita. EX: "/categories/6"
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
}
