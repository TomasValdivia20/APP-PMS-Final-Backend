package com.milsabores.backend.controller;

import com.milsabores.backend.model.Usuario;
import com.milsabores.backend.model.Rol;
import com.milsabores.backend.repository.UsuarioRepository;
import com.milsabores.backend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Crear Usuario para admin
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return ResponseEntity.badRequest().body("El correo ya existe");
        }
        // Asegurar que el rol existe (enviado por ID o nombre)
        if (usuario.getRol() != null && usuario.getRol().getId() != null) {
            Optional<Rol> rolOpt = rolRepository.findById(usuario.getRol().getId());
            if (rolOpt.isPresent()) usuario.setRol(rolOpt.get());
        }
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // Actualizar Usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(user -> {
            user.setNombre(usuarioActualizado.getNombre());
            user.setApellido(usuarioActualizado.getApellido());
            user.setCorreo(usuarioActualizado.getCorreo());
            user.setDireccion(usuarioActualizado.getDireccion());
            user.setRut(usuarioActualizado.getRut());
            user.setRegion(usuarioActualizado.getRegion());
            user.setComuna(usuarioActualizado.getComuna());

            // Actualizar password solo si viene nueva
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                user.setPassword(usuarioActualizado.getPassword());
            }

            // Actualizar rol
            if (usuarioActualizado.getRol() != null && usuarioActualizado.getRol().getId() != null) {
                rolRepository.findById(usuarioActualizado.getRol().getId()).ifPresent(user::setRol);
            }

            return ResponseEntity.ok(usuarioRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar Usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        if (id == 1L) { // Asumimos que ID 1 es el Super Admin inicial
            return ResponseEntity.badRequest().body("No se puede eliminar al Administrador Principal.");
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}