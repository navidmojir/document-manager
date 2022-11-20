package ir.mojir.document_manager.exceptions;

import org.springframework.http.HttpStatus;

import ir.mojir.document_manager.enums.Errors;
import ir.mojir.spring_boot_commons.dtos.ErrorDto;
import ir.mojir.spring_boot_commons.exceptions.ServiceException;

@SuppressWarnings("serial")
public class AccessDeniedException extends ServiceException {

	public AccessDeniedException(String message) {
		super(new ErrorDto(message, Errors.ACCESS_DENIED), null);
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.FORBIDDEN;
	}

}
