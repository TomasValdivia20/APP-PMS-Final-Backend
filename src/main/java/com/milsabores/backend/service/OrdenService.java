package com.milsabores.backend.service;

import com.milsabores.backend.dto.DetalleRequest;
import com.milsabores.backend.dto.OrdenRequest;
import com.milsabores.backend.model.*;
import com.milsabores.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private VarianteProductoRepository varianteRepository;

    // @Transactional asegura que si falla algo (ej: falta stock), no se guarda NADA.
    @Transactional
    public Orden crearOrden(OrdenRequest request) {
        // 1. Obtener Usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear Objeto Orden
        Orden orden = new Orden();
        orden.setFecha(LocalDateTime.now());
        orden.setTotal(request.getTotal());
        orden.setEstado("PAGADO");
        orden.setUsuario(usuario);
        orden.setDetalles(new ArrayList<>());

        // 3. Procesar Detalles y Descontar Stock
        for (DetalleRequest detReq : request.getDetalles()) {
            Producto producto = productoRepository.findById(detReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            VarianteProducto variante = varianteRepository.findById(detReq.getVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

            // 🛑 VALIDACIÓN DE STOCK
            if (variante.getStock() < detReq.getCantidad()) {
                throw new RuntimeException("Sin stock suficiente para: " + producto.getNombre() + " (" + variante.getNombre() + ")");
            }

            // 🛑 DESCUENTO DE STOCK
            variante.setStock(variante.getStock() - detReq.getCantidad());
            varianteRepository.save(variante);

            // Crear Detalle
            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(orden);
            detalle.setProducto(producto);
            detalle.setVariante(variante);
            detalle.setCantidad(detReq.getCantidad());
            detalle.setPrecioUnitario(detReq.getPrecioUnitario());
            detalle.setSubtotal(detReq.getCantidad() * detReq.getPrecioUnitario());

            orden.getDetalles().add(detalle);
        }

        // 4. Guardar Orden (Cascade guardará los detalles)
        return ordenRepository.save(orden);
    }
}