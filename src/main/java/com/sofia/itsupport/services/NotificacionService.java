package com.sofia.itsupport.services;


import com.sofia.itsupport.dto.response.TicketResponseDTO;
import com.sofia.itsupport.dto.response.ComentarioResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Notificar a todos los técnicos (suscripción a /topic/tickets)
    public void notificarNuevoTicket(TicketResponseDTO ticket) {
        messagingTemplate.convertAndSend("/topic/tickets/nuevo", ticket);
    }

    // Notificar a un técnico específico (cola privada)
    public void notificarAsignacionTecnico(Long tecnicoId, TicketResponseDTO ticket) {
        messagingTemplate.convertAndSendToUser(
                tecnicoId.toString(),
                "/queue/tickets/asignado",
                ticket
        );
    }

    // Notificar al encargado que creó el ticket
    public void notificarEncargado(Long encargadoId, TicketResponseDTO ticket) {
        messagingTemplate.convertAndSendToUser(
                encargadoId.toString(),
                "/queue/tickets/actualizacion",
                ticket
        );
    }

    // Notificar nuevo comentario a los involucrados
    public void notificarNuevoComentario(Long ticketId, ComentarioResponseDTO comentario) {
        // Enviar al topic del ticket específico
        messagingTemplate.convertAndSend("/topic/tickets/" + ticketId + "/comentarios", comentario);
    }

    // Notificar cambio de estado (cerrado, anulado)
    public void notificarCambioEstado(TicketResponseDTO ticket) {
        // A todos los interesados en ese ticket
        messagingTemplate.convertAndSend("/topic/tickets/" + ticket.getId() + "/estado", ticket);
        // También notificar al técnico y encargado (si quieres por privado)
        if (ticket.getTecnicoId() != null) {
            messagingTemplate.convertAndSendToUser(
                    ticket.getTecnicoId().toString(),
                    "/queue/tickets/estado",
                    ticket
            );
        }
        if (ticket.getEncargadoId() != null) {
            messagingTemplate.convertAndSendToUser(
                    ticket.getEncargadoId().toString(),
                    "/queue/tickets/estado",
                    ticket
            );
        }
    }
}