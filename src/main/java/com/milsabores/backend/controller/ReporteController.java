package com.milsabores.backend.controller;

import com.milsabores.backend.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private OrdenRepository ordenRepository;

    @GetMapping("/ventas")
    public Map<String, Integer> obtenerReporteVentas() {
        LocalDateTime ahora = LocalDateTime.now();

        // 1. Cálculo Mensual (Mes actual)
        LocalDateTime inicioMes = ahora.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime finMes = ahora.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        Integer totalMensual = ordenRepository.sumarTotalesPorFecha(inicioMes, finMes);

        // 2. Cálculo Semestral (Últimos 6 meses)
        LocalDateTime inicioSemestre = ahora.minusMonths(6).with(LocalTime.MIN);
        Integer totalSemestral = ordenRepository.sumarTotalesPorFecha(inicioSemestre, ahora);

        // 3. Cálculo Anual (Año actual)
        LocalDateTime inicioAnio = ahora.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        LocalDateTime finAnio = ahora.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        Integer totalAnual = ordenRepository.sumarTotalesPorFecha(inicioAnio, finAnio);

        // Respuesta JSON simple
        Map<String, Integer> reporte = new HashMap<>();
        reporte.put("mensual", totalMensual);
        reporte.put("semestral", totalSemestral);
        reporte.put("anual", totalAnual);

        return reporte;
    }
}