package com.sofia.itsupport.controllers;

import com.sofia.itsupport.dto.request.CrearAreaRequest;
import com.sofia.itsupport.dto.response.AreaResponseDTO;
import com.sofia.itsupport.services.AreaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/areas")
@CrossOrigin(origins = "*")
public class AreaController {

    @Autowired
    private AreaService areaService;

    // ===========================================
    // 1. CREAR ÁREA (POST /api/areas)
    // ===========================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearArea(@Valid @RequestBody CrearAreaRequest request) {
        try {
            AreaResponseDTO nuevaArea = areaService.crearArea(request.getNombre(), request.getSucursalId());
            return new ResponseEntity<>(nuevaArea, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // ===========================================
    // 2. OBTENER ÁREA POR ID (GET /api/areas/{id})
    // ===========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerArea(@PathVariable Long id) {
        try {
            AreaResponseDTO area = areaService.obtenerAreaPorId(id);
            return ResponseEntity.ok(area);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ===========================================
    // 3. LISTAR TODAS LAS ÁREAS (GET /api/areas)
    // ===========================================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AreaResponseDTO>> listarTodas() {
        List<AreaResponseDTO> areas = areaService.listarTodas();
        return ResponseEntity.ok(areas);
    }

    // ===========================================
    // 4. LISTAR ÁREAS POR SUCURSAL (GET /api/areas/sucursal/{sucursalId})
    // ===========================================
    @GetMapping("/sucursal/{sucursalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AreaResponseDTO>> listarPorSucursal(@PathVariable Long sucursalId) {
        List<AreaResponseDTO> areas = areaService.listarPorSucursal(sucursalId);
        return ResponseEntity.ok(areas);
    }

    // ===========================================
    // 5. LISTAR ÁREAS ACTIVAS POR SUCURSAL (GET /api/areas/sucursal/{sucursalId}/activas)
    // ===========================================
    @GetMapping("/sucursal/{sucursalId}/activas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AreaResponseDTO>> listarActivasPorSucursal(@PathVariable Long sucursalId) {
        List<AreaResponseDTO> areas = areaService.listarActivasPorSucursal(sucursalId);
        return ResponseEntity.ok(areas);
    }

    // ===========================================
    // 6. ACTUALIZAR ÁREA (PUT /api/areas/{id})
    // ===========================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarArea(@PathVariable Long id, @RequestParam String nuevoNombre) {
        try {
            AreaResponseDTO area = areaService.actualizarArea(id, nuevoNombre);
            return ResponseEntity.ok(area);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 7. DESACTIVAR ÁREA (PUT /api/areas/{id}/desactivar)
    // ===========================================
    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desactivarArea(@PathVariable Long id) {
        try {
            AreaResponseDTO area = areaService.desactivarArea(id);
            return ResponseEntity.ok(area);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 8. ACTIVAR ÁREA (PUT /api/areas/{id}/activar)
    // ===========================================
    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activarArea(@PathVariable Long id) {
        try {
            AreaResponseDTO area = areaService.activarArea(id);
            return ResponseEntity.ok(area);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 9. ASIGNAR ENCARGADO (POST /api/areas/{areaId}/asignar-encargado/{usuarioId})
    // ===========================================
    @PostMapping("/{areaId}/asignar-encargado/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> asignarEncargado(@PathVariable Long areaId, @PathVariable Long usuarioId) {
        try {
            AreaResponseDTO area = areaService.asignarEncargado(areaId, usuarioId);
            return ResponseEntity.ok(area);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
