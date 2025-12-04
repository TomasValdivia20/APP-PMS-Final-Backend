package com.milsabores.backend.controller;

import com.milsabores.backend.dto.LoginRequest;
import com.milsabores.backend.dto.RegisterRequest;
import com.milsabores.backend.model.Rol;
import com.milsabores.backend.model.Usuario;
import com.milsabores.backend.repository.RolRepository;
import com.milsabores.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> user = usuarioRepository.findByCorreoAndPassword(request.getEmail(), request.getPassword());
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            // Retornamos 401 si falla
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getEmail())) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        // Buscar Rol CLIENTE (Asumimos que DataInitializer lo creó)
        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Error: Rol CLIENTE no encontrado."));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setRut(request.getRut());
        usuario.setRegion(request.getRegion());
        usuario.setComuna(request.getComuna());
        usuario.setDireccion(request.getDireccion());
        usuario.setCorreo(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setRol(rolCliente);

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }
}