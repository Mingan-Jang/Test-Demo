package com.jjk.apitest.enumeration;

public enum FileActionEnum {
	UPLOAD_FILE("Error occurred during file upload."), UPLOAD_FOLDER("Error occurred during folder upload."),
	READ_LOCAL_FILE("Error occurred while reading local file.");

	private final String errorDesc;

	FileActionEnum(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getErrorDesc() {
		return errorDesc;
	}
}
