package ir.mojir.document_manager.enums;

import ir.mojir.spring_boot_commons.interfaces.IError;

public enum Errors implements IError {
	UPLOAD_FAILED(201);

	private int code;
	
	
	private Errors(int code) {
		this.code = code;
	}

	@Override
	public String getString() {
		return this.name();
	}

	@Override
	public int getCode() {
		return this.code;
	}

}
