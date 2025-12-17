package com.milsabores.backend.repository;

import com.milsabores.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    // Listar órdenes de la más reciente a la más antigua
    List<Orden> findAllByOrderByFechaDesc();

    // 🛑 QUERY PARA REPORTES: Suma los totales en un rango de fechas
    // COALESCE asegura que si no hay ventas, devuelva 0 en vez de null
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Orden o WHERE o.fecha BETWEEN :inicio AND :fin")
    Integer sumarTotalesPorFecha(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}