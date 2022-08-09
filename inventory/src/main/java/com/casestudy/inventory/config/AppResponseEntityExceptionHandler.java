package com.casestudy.inventory.config;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.casestudy.instagrocer.commons.dto.ExceptionResponse;
import com.casestudy.instagrocer.commons.exception.AuthorizationException;
import com.casestudy.instagrocer.commons.exception.InstaGrocerApplicationException;
import com.casestudy.instagrocer.commons.exception.NotFoundException;

@RestController
@ControllerAdvice
public class AppResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setMessage("Invalid feilds provided!!");
		
		BindingResult bindingResult = ex.getBindingResult();
		List<ObjectError> errors = bindingResult.getAllErrors();
		errors.forEach(error -> exceptionResponse.addDetails(error.getDefaultMessage()));
		
		return new ResponseEntity<>(exceptionResponse, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setMessage("Cannot read http message!!");
		exceptionResponse.addDetails(ex.getLocalizedMessage());
		
		return new ResponseEntity<>(exceptionResponse, status);
	}
	
	@ExceptionHandler(DuplicateKeyException.class)
	protected ResponseEntity<ExceptionResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setMessage("Unique key violated on collection, Cannot perform write/update!!");
		
		try {
			// parse Exception to get custom message
			String[] customDuplicateKeyParsedError = parseDuplicateKeyErrorRootCause(ex.getRootCause().getLocalizedMessage());
			exceptionResponse.setMessage(customDuplicateKeyParsedError[0] + " must be unique");
			exceptionResponse.addDetails(customDuplicateKeyParsedError[1]);
		} catch (Exception e) {
			exceptionResponse.addDetails("Unique Key violated.");
		}
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected ResponseEntity<ExceptionResponse> handleAuthorizationException(AuthorizationException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setMessage("UnAuthorized for this operation!!");
		exceptionResponse.addDetails(ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(NotFoundException.class)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setMessage("Resource doesn't exists!!");
		exceptionResponse.addDetails(ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InstaGrocerApplicationException.class)
	protected ResponseEntity<ExceptionResponse> handleUserApplicationException(InstaGrocerApplicationException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setMessage(ex.getMessage());
		exceptionResponse.addDetails(ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	// Re-throw AccessDeniedException & AuthenticationException to get authenticationEntryPoint and accessDeniedHandler to work.
	// Imp: Doesn't solve the problem
	@ExceptionHandler({ AccessDeniedException.class, AuthenticationException.class })
	protected void handleSpringSecurityException(RuntimeException ex) {
		throw ex;
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ExceptionResponse> handleAllException(Exception ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setMessage("Something went wrong!!");
		exceptionResponse.addDetails(ex.getLocalizedMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private String[] parseDuplicateKeyErrorRootCause(String error) {
		// splitting by "Error{" => 2nd element will be returned json object
		// and deleting terminating "}."
		String errorJson = error.split("Error\\{")[1].replace("\\}\\.", "");

		// getting message property and deleting collection name
		String mongoErrorMessage = errorJson.split(",")[1]
				.replace("message=", "")
				.replace("'", "")
				.split("dup key")[1]
				.trim();

		// extracting field
		String field = mongoErrorMessage.split("\\{")[1].split(":")[0].trim();

		// return array => 1st element=field, 2nd element=message
		return new String[] { field, "Duplicate" + mongoErrorMessage };
	}
}
