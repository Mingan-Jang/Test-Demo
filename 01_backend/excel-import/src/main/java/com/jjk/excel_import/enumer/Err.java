package com.jjk.excel_import.enumer;

public enum Err {
	BAD_REQUEST("400"),
	NOT_FOUND("404"),
	INTERNAL_ERROR("500"),
	UNAUTHORIZED("401");

    private final String code;

    Err(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
