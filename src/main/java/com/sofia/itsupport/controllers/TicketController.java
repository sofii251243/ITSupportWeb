package com.sofia.itsupport.controllers;

import com.sofia.itsupport.dto.request.AsignarTicketRequest;
import com.sofia.itsupport.dto.request.CrearTicketRequest;
import com.sofia.itsupport.dto.response.TicketResponseDTO;
import com.sofia.itsupport.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")  // Permitir peticiones desde cualquier origen (React, Angular, etc.)
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // ===========================================
    // 1. CREAR TICKET (POST /api/tickets)
    // ===========================================
    @PostMapping
    public ResponseEntity<?> crearTicket(@Valid @RequestBody CrearTicketRequest request) {
        try {
            TicketResponseDTO nuevoTicket = ticketService.crearTicket(request);
            return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 2. OBTENER TICKET POR ID (GET /api/tickets/{id})
    // ===========================================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTicket(@PathVariable Long id) {
        try {
            TicketResponseDTO ticket = ticketService.obtenerTicketPorId(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ===========================================
    // 3. LISTAR TODOS LOS TICKETS (GET /api/tickets)
    // ===========================================
    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> listarTodos() {
        List<TicketResponseDTO> tickets = ticketService.listarTodos();
        return ResponseEntity.ok(tickets);
    }

    // ===========================================
    // 4. TICKETS DISPONIBLES PARA TÉCNICOS (GET /api/tickets/disponibles)
    // ===========================================
    @GetMapping("/disponibles")
    public ResponseEntity<List<TicketResponseDTO>> ticketsDisponibles() {
        List<TicketResponseDTO> disponibles = ticketService.ticketsDisponibles();
        return ResponseEntity.ok(disponibles);
    }

    // ===========================================
    // 5. ASIGNAR TICKET A TÉCNICO (POST /api/tickets/asignar)
    // ===========================================
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarTicket(@Valid @RequestBody AsignarTicketRequest request) {
        try {
            TicketResponseDTO ticketAsignado = ticketService.asignarTicket(request);
            return ResponseEntity.ok(ticketAsignado);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 6. CERRAR TICKET (PUT /api/tickets/{id}/cerrar)
    // ===========================================
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarTicket(@PathVariable Long id, @RequestParam Long tecnicoId) {
        try {
            TicketResponseDTO ticketCerrado = ticketService.cerrarTicket(id, tecnicoId);
            return ResponseEntity.ok(ticketCerrado);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ===========================================
    // 7. ASIGNACIÓN AUTOMÁTICA (POST /api/tickets/asignacion-automatica)
    // ===========================================
    @PostMapping("/asignacion-automatica")
    public ResponseEntity<String> asignacionAutomatica() {
        try {
            int asignados = ticketService.asignacionAutomatica();
            return ResponseEntity.ok("Se asignaron " + asignados + " tickets automáticamente");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}