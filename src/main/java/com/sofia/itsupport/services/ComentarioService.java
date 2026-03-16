package com.sofia.itsupport.services;

import com.sofia.itsupport.dto.request.CrearComentarioRequest;
import com.sofia.itsupport.dto.response.ComentarioResponseDTO;
import com.sofia.itsupport.entities.Comentario;
import com.sofia.itsupport.entities.Ticket;
import com.sofia.itsupport.entities.Usuario;
import com.sofia.itsupport.repositories.ComentarioRepository;
import com.sofia.itsupport.repositories.TicketRepository;
import com.sofia.itsupport.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ===========================================
    // CREAR COMENTARIO
    // ===========================================
    @Transactional
    public ComentarioResponseDTO crearComentario(CrearComentarioRequest request) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comentario comentario = new Comentario(
                request.getContenido(),
                ticket,
                usuario
        );

        comentario = comentarioRepository.save(comentario);

        // Convertir a DTO
        ComentarioResponseDTO dto = convertirADTO(comentario);

        // Notificar a todos los suscritos al ticket (público)
        messagingTemplate.convertAndSend("/topic/tickets/" + ticket.getId() + "/comentarios", dto);

        // Opcional: Notificar personalmente al encargado y al técnico
        if (ticket.getEncargado() != null) {
            messagingTemplate.convertAndSendToUser(
                    ticket.getEncargado().getNombreUsuario(),
                    "/queue/comentarios",
                    dto
            );
        }
        if (ticket.getTecnico() != null) {
            messagingTemplate.convertAndSendToUser(
                    ticket.getTecnico().getNombreUsuario(),
                    "/queue/comentarios",
                    dto
            );
        }

        return dto;
    }

    // ===========================================
    // OBTENER COMENTARIOS DE UN TICKET
    // ===========================================
    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> obtenerComentariosPorTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        return comentarioRepository.findByTicketOrderByFechaCreacionAsc(ticket)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // MÉTODO PRIVADO: Convertir Entity → DTO
    // ===========================================
    private ComentarioResponseDTO convertirADTO(Comentario comentario) {
        ComentarioResponseDTO dto = new ComentarioResponseDTO();

        dto.setId(comentario.getId());
        dto.setContenido(comentario.getContenido());
        dto.setFechaCreacion(comentario.getFechaCreacion());

        if (comentario.getTicket() != null) {
            dto.setTicketId(comentario.getTicket().getId());
            dto.setTicketTitulo(comentario.getTicket().getTitulo());
        }

        if (comentario.getUsuario() != null) {
            dto.setUsuarioId(comentario.getUsuario().getId());
            dto.setUsuarioNombre(comentario.getUsuario().getNombreUsuario());
            dto.setUsuarioRol(comentario.getUsuario().getRol().name());
        }

        return dto;
    }
}
