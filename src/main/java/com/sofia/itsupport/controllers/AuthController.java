package com.sofia.itsupport.controllers;

import com.sofia.itsupport.dto.request.LoginRequest;
import com.sofia.itsupport.dto.response.LoginResponseDTO;
import com.sofia.itsupport.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    static {
        System.out.println("🔥 AuthController CARGADO");
    }

    @Autowired
    private AuthService authService;

    // ===========================================
    // 1. LOGIN (POST /api/auth/login)
    // ===========================================

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    // ===========================================
    // 2. CAMBIAR CONTRASEÑA (POST /api/auth/cambiar-contrasena)
    // ===========================================
    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(
            @RequestParam Long usuarioId,
            @RequestParam String contrasenaActual,
            @RequestParam String contrasenaNueva) {
        try {
            String mensaje = authService.cambiarContrasena(usuarioId, contrasenaActual, contrasenaNueva);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", mensaje);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}