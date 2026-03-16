package com.sofia.itsupport.dto.response;

import java.time.LocalDateTime;

public class ComentarioResponseDTO {
    private Long id;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private Long ticketId;
    private String ticketTitulo;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioRol;

    public ComentarioResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getTicketTitulo() { return ticketTitulo; }
    public void setTicketTitulo(String ticketTitulo) { this.ticketTitulo = ticketTitulo; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getUsuarioRol() { return usuarioRol; }
    public void setUsuarioRol(String usuarioRol) { this.usuarioRol = usuarioRol; }
}