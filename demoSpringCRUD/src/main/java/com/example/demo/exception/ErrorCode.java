package com.example.demo.exception;

public enum ErrorCode {
    ERROR1O1(1001," ERROR NEW ");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;



    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
