package com.sofia.itsupport.controllers;

import com.sofia.itsupport.dto.request.CrearSucursalRequest;
import com.sofia.itsupport.dto.response.SucursalResponseDTO;
import com.sofia.itsupport.services.SucursalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sucursales")
@CrossOrigin(origins = "*")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    // ===========================================
    // 1. CREAR SUCURSAL (POST /api/sucursales)
    // ===========================================
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    @PostMapping
    public ResponseEntity<?> crearSucursal(@Valid @RequestBody CrearSucursalRequest request) {
        try {
            SucursalResponseDTO nuevaSucursal = sucursalService.crearSucursal(request.getNombre());
            return new ResponseEntity<>(nuevaSucursal, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 2. OBTENER SUCURSAL POR ID (GET /api/sucursales/{id})
    // ===========================================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSucursal(@PathVariable Long id) {
        try {
            SucursalResponseDTO sucursal = sucursalService.obtenerSucursalPorId(id);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ===========================================
    // 3. LISTAR TODAS LAS SUCURSALES (GET /api/sucursales)
    // ===========================================
    @GetMapping
    public ResponseEntity<List<SucursalResponseDTO>> listarTodas() {
        List<SucursalResponseDTO> sucursales = sucursalService.listarTodas();
        return ResponseEntity.ok(sucursales);
    }

    // ===========================================
    // 4. LISTAR SUCURSALES ACTIVAS (GET /api/sucursales/activas)
    // ===========================================
    @GetMapping("/activas")
    public ResponseEntity<List<SucursalResponseDTO>> listarActivas() {
        List<SucursalResponseDTO> sucursales = sucursalService.listarActivas();
        return ResponseEntity.ok(sucursales);
    }

    // ===========================================
    // 5. ACTUALIZAR SUCURSAL (PUT /api/sucursales/{id})
    // ===========================================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarSucursal(@PathVariable Long id, @RequestParam String nuevoNombre) {
        try {
            SucursalResponseDTO sucursal = sucursalService.actualizarSucursal(id, nuevoNombre);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 6. DESACTIVAR SUCURSAL (PUT /api/sucursales/{id}/desactivar)
    // ===========================================
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarSucursal(@PathVariable Long id) {
        try {
            SucursalResponseDTO sucursal = sucursalService.desactivarSucursal(id);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 7. ACTIVAR SUCURSAL (PUT /api/sucursales/{id}/activar)
    // ===========================================
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarSucursal(@PathVariable Long id) {
        try {
            SucursalResponseDTO sucursal = sucursalService.activarSucursal(id);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}