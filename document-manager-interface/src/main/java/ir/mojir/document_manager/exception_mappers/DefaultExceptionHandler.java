package ir.mojir.document_manager.exception_mappers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import ir.mojir.spring_boot_commons.dtos.ErrorDto;
import ir.mojir.spring_boot_commons.enums.ErrorEnum;
import ir.mojir.spring_boot_commons.exceptions.ServiceException;

@ControllerAdvice
public class DefaultExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);
	
	@ExceptionHandler(value = ServiceException.class)
	public ResponseEntity<Object> exception(ServiceException e)
	{
		logger.error("An exception occured in processing request. Trace: ", e);
		return new ResponseEntity<>(e.getError(), e.getHttpStatus());
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception e)
	{
		logger.error("A general exception occured in processing request. Trace: ", e);
		return new ResponseEntity<>(new ErrorDto("A general error occured. Please contact administrator for furthur details.", ErrorEnum.INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleMsgNotReadableException(Exception e)
	{
		logger.error("Failed to . Trace: ", e);
		return new ResponseEntity<>(new ErrorDto("Message body is not readable. Check the http body is valid.", ErrorEnum.INVALID_INPUT), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(Exception e)
	{
		logger.error("Failed to . Trace: ", e);
		return new ResponseEntity<>(new ErrorDto("Method argument is not valid. Check the path parameters in URL.", ErrorEnum.INVALID_INPUT), HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
		logger.error("Validation error. Trace: ", e);
		Map<String, String> errors = new HashMap<>();
	    e.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return new ResponseEntity<>(new ErrorDto("Input is not valid. " + errors, ErrorEnum.INVALID_INPUT), HttpStatus.BAD_REQUEST);
	}
}
