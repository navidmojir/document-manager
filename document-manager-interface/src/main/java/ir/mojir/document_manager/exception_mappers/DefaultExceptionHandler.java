package ir.mojir.document_manager.exception_mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ir.mojir.spring_boot_commons.dtos.ErrorDto;
import ir.mojir.spring_boot_commons.enums.ErrorEnum;
import ir.mojir.spring_boot_commons.exceptions.ServiceException;

@ControllerAdvice
public class DefaultExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);
	
	@ExceptionHandler(value = ServiceException.class)
	public ResponseEntity<Object> serviceException(ServiceException exception) {
		logger.error("A business exception occured in processing request. Trace:", exception);
		return new ResponseEntity<>(exception.getError(), exception.getHttpStatus());
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception exception) {
		logger.error("A general exception occured in processing request. Trace:", exception);
		return new ResponseEntity<>(new ErrorDto(
				"A general error occured. Details: " + exception.getMessage(), ErrorEnum.INTERNAL_ERROR),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
