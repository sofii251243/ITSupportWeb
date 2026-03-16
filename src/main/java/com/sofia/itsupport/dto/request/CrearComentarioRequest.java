package com.sofia.itsupport.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearComentarioRequest {

    @NotBlank(message = "El comentario no puede estar vacío")
    private String contenido;

    @NotNull(message = "El ID del ticket es obligatorio")
    private Long ticketId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    public CrearComentarioRequest() {}

    // Getters y Setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}