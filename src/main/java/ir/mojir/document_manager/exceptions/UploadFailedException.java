package ir.mojir.document_manager.exceptions;

import org.springframework.http.HttpStatus;

import ir.mojir.document_manager.enums.Errors;
import ir.mojir.spring_boot_commons.dtos.ErrorDto;
import ir.mojir.spring_boot_commons.exceptions.ServiceException;

@SuppressWarnings("serial")
public class UploadFailedException extends ServiceException {

	public UploadFailedException(String message, Throwable e) {
		super(new ErrorDto(message, Errors.UPLOAD_FAILED), e);
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
