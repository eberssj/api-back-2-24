package com.example.api2024.dto;

public class AcessoDto {
    
    private String token;

    public AcessoDto(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
