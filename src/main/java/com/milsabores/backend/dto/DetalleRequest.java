package com.milsabores.backend.dto;

import lombok.Data;

@Data
public class DetalleRequest {
    private Long productoId;
    private Long varianteId;
    private Integer cantidad;
    private Integer precioUnitario;
}