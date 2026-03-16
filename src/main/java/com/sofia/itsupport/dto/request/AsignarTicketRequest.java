package com.sofia.itsupport.dto.request;

import jakarta.validation.constraints.NotNull;

public class AsignarTicketRequest {

    @NotNull(message = "El ID del ticket es obligatorio")
    private Long ticketId;

    @NotNull(message = "El ID del técnico es obligatorio")
    private Long tecnicoId;

    // Constructor vacío
    public AsignarTicketRequest() {}

    // Getters y Setters
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }
}