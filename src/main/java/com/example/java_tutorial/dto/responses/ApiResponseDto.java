package com.example.java_tutorial.dto.responses;

public class ApiResponseDto<T> {
    private boolean ok;
    private String message;
    private T data;

    public ApiResponseDto(boolean ok, String message, T data) {
        this.ok = ok;
        this.message = message;
        this.data = data;
    }

    // Getters (No Setters to keep it immutable)
    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
