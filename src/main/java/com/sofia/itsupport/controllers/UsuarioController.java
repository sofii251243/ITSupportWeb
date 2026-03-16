package com.sofia.itsupport.controllers;

import com.sofia.itsupport.dto.request.CrearUsuarioRequest;
import com.sofia.itsupport.dto.response.UsuarioResponseDTO;
import com.sofia.itsupport.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ===========================================
    // 1. REGISTRAR USUARIO (POST /api/usuarios)
    // ===========================================
    @PostMapping
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
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // ===========================================
    // 4. LISTAR TÉCNICOS ACTIVOS (GET /api/usuarios/tecnicos-activos)
    // ===========================================
    @GetMapping("/tecnicos-activos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTecnicosActivos() {
        List<UsuarioResponseDTO> tecnicos = usuarioService.listarTecnicosActivos();
        return ResponseEntity.ok(tecnicos);
    }

    // ===========================================
    // 5. SUSPENDER USUARIO (PUT /api/usuarios/{id}/suspender)
    // ===========================================
    @PutMapping("/{id}/suspender")
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
    public ResponseEntity<?> activarUsuario(@PathVariable Long id) {
        try {
            UsuarioResponseDTO usuario = usuarioService.activarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
