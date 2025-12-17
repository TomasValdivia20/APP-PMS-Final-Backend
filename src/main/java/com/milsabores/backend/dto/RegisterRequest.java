package com.milsabores.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String rut;
    private String region;
    private String comuna;
    private String direccion;
    private String email;
    private String password;
}