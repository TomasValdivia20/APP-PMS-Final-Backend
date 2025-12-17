package com.milsabores.backend.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrdenRequest {
    private Long usuarioId;
    private Integer total;
    private List<DetalleRequest> detalles;
}