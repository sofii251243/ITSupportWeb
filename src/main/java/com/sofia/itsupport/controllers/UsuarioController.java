package com.sofia.itsupport.controllers;

import com.sofia.itsupport.dto.request.AdminUsuarioUpdateRequest;
import com.sofia.itsupport.dto.request.CrearUsuarioRequest;
import com.sofia.itsupport.dto.request.UsuarioUpdateRequest;
import com.sofia.itsupport.dto.response.UsuarioResponseDTO;
import com.sofia.itsupport.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ===========================================
    // 1. REGISTRAR USUARIO (POST /api/usuarios)
    // ===========================================
    @PostMapping("/registro")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        try {
            UsuarioResponseDTO nuevoUsuario = usuarioService.registrarUsuario(request);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 2. OBTENER USUARIO POR ID (GET /api/usuarios/{id})
    // ===========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        try {
            UsuarioResponseDTO usuario = usuarioService.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ===========================================
    // 3. LISTAR TODOS LOS USUARIOS (GET /api/usuarios)
    // ===========================================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // ===========================================
    // 4. LISTAR TÉCNICOS ACTIVOS (GET /api/usuarios/tecnicos-activos)
    // ===========================================
    @GetMapping("/tecnicos-activos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTecnicosActivos() {
        List<UsuarioResponseDTO> tecnicos = usuarioService.listarTecnicosActivos();
        return ResponseEntity.ok(tecnicos);
    }

    // ===========================================
    // 5. SUSPENDER USUARIO (PUT /api/usuarios/{id}/suspender)
    // ===========================================
    @PutMapping("/{id}/suspender")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> suspenderUsuario(@PathVariable Long id) {
        try {
            UsuarioResponseDTO usuario = usuarioService.suspenderUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 6. ACTIVAR USUARIO (PUT /api/usuarios/{id}/activar)
    // ===========================================
    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activarUsuario(@PathVariable Long id) {
        try {
            UsuarioResponseDTO usuario = usuarioService.activarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/contrasena")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<?> cambiarContrasena(
            @PathVariable Long id,
            @RequestParam String contrasenaActual,
            @RequestParam String contrasenaNueva) {

        try {
            String mensaje = usuarioService.cambiarContrasena(id, contrasenaActual, contrasenaNueva);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", mensaje);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> obtenerMiPerfil() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            UsuarioResponseDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> actualizarMiPerfil(@Valid @RequestBody UsuarioUpdateRequest request) {
        try {
            UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuarioAutenticado(request);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarUsuarioPorAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminUsuarioUpdateRequest request) {
        try {
            UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuarioPorAdmin(id, request);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
