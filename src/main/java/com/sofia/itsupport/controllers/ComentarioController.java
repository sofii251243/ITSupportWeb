package com.sofia.itsupport.controllers;

import com.sofia.itsupport.dto.request.CrearComentarioRequest;
import com.sofia.itsupport.dto.response.ComentarioResponseDTO;
import com.sofia.itsupport.services.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    // ===========================================
    // 1. CREAR COMENTARIO (POST /api/comentarios)
    // ===========================================
    @PostMapping
    public ResponseEntity<?> crearComentario(@Valid @RequestBody CrearComentarioRequest request) {
        try {
            ComentarioResponseDTO nuevoComentario = comentarioService.crearComentario(request);
            return new ResponseEntity<>(nuevoComentario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 2. OBTENER COMENTARIOS DE UN TICKET (GET /api/comentarios/ticket/{ticketId})
    // ===========================================
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<?> obtenerComentariosPorTicket(@PathVariable Long ticketId) {
        try {
            List<ComentarioResponseDTO> comentarios = comentarioService.obtenerComentariosPorTicket(ticketId);
            return ResponseEntity.ok(comentarios);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
