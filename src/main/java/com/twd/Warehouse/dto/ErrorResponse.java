package com.twd.Warehouse.dto;


import lombok.Data;

@Data
public class ErrorResponse {
    private String errorCode;
    private String message;
    private String barcode;

    public ErrorResponse(String errorCode, String message, String barcode) {
        this.errorCode = errorCode;
        this.message = message;
        this.barcode = barcode;
    }

    // Getters and setters
}
