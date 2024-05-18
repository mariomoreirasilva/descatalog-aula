package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Recurso não encontrado");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	//tratando a excessao de banco de dados como integridade referencial
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		//variavel só para não repetir nos codigos comentados. O metodo de cima não troquei
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		//err.setStatus(HttpStatus.BAD_REQUEST.value());
		err.setStatus(status.value());
		err.setError("Erro de banco de dados.");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
		return ResponseEntity.status(status).body(err);
	}
	
	

}
