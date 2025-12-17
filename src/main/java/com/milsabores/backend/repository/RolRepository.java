package com.milsabores.backend.repository;

import com.milsabores.backend.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Método útil para buscar roles por nombre (ej: "ADMIN")
    Optional<Rol> findByNombre(String nombre);
}