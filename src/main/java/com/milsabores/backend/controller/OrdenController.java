package com.milsabores.backend.controller;

import com.milsabores.backend.dto.OrdenRequest;
import com.milsabores.backend.model.Orden;
import com.milsabores.backend.repository.OrdenRepository;
import com.milsabores.backend.service.OrdenService; // Importar Servicio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private OrdenService ordenService; // Inyectar Servicio

    @GetMapping
    public List<Orden> listarOrdenes() {
        return ordenRepository.findAllByOrderByFechaDesc();
    }

    // 🛑 NUEVO ENDPOINT POST
    @PostMapping
    public ResponseEntity<?> crearOrden(@RequestBody OrdenRequest request) {
        try {
            Orden nuevaOrden = ordenService.crearOrden(request);
            return ResponseEntity.ok(nuevaOrden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}