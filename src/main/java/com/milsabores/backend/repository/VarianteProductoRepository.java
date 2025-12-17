package com.milsabores.backend.repository;

import com.milsabores.backend.model.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Long> {
}