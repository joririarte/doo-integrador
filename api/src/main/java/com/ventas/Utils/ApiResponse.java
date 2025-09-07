package com.ventas.Utils;

public class ApiResponse<T> {
    private Boolean success;
    private String message;
    private T data;

    public ApiResponse(){
        this.success = false;
        this.message = "Recurso no encontrado";
        this.data = null;
    }

    public ApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // getters y setters
    public Boolean getStatus() { return success; }
    public void setStatus(Boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public void setSuccessResponse(T data){
        this.success = true;
        this.message = "Recurso encontrado";
        this.data = data;
    }
}

