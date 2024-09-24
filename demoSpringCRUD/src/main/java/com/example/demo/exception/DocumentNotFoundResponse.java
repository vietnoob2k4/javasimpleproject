package com.example.demo.exception;

public class DocumentNotFoundResponse {
    private String message;

    public DocumentNotFoundResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
