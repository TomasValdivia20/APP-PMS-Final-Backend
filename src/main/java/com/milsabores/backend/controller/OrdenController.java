package com.milsabores.backend.controller;

import com.milsabores.backend.dto.OrdenRequest;
import com.milsabores.backend.model.Orden;
import com.milsabores.backend.repository.OrdenRepository;
import com.milsabores.backend.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired private OrdenRepository ordenRepository;
    @Autowired private OrdenService ordenService;

    @GetMapping
    public List<Orden> listarOrdenes() {
        return ordenRepository.findAllByOrderByFechaDesc();
    }

    @PostMapping
    public ResponseEntity<?> crearOrden(@RequestBody OrdenRequest request) {
        try {
            Orden nuevaOrden = ordenService.crearOrden(request);
            return ResponseEntity.ok(nuevaOrden);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nuevoEstado = payload.get("estado");
        return ordenRepository.findById(id).map(orden -> {
            orden.setEstado(nuevoEstado);
            return ResponseEntity.ok(ordenRepository.save(orden));
        }).orElse(ResponseEntity.notFound().build());
    }
}