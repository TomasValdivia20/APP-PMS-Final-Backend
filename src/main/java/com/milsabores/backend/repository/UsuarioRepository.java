package com.milsabores.backend.repository;

import com.milsabores.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Para el Login
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);

    // Para validar registro
    boolean existsByCorreo(String correo);
}