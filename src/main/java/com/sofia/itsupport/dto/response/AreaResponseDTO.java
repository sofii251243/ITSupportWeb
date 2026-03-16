package com.sofia.itsupport.dto.response;

import com.sofia.itsupport.enums.EstadoGeneral;

public class AreaResponseDTO {
    private Long id;
    private String nombre;
    private EstadoGeneral estado;
    private Long sucursalId;
    private String sucursalNombre;
    private Integer totalTickets;      // Cuántos tickets tiene esta área
    private String encargadoNombre;    // Nombre del encargado (si tiene)

    public AreaResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public EstadoGeneral getEstado() { return estado; }
    public void setEstado(EstadoGeneral estado) { this.estado = estado; }

    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }

    public String getSucursalNombre() { return sucursalNombre; }
    public void setSucursalNombre(String sucursalNombre) { this.sucursalNombre = sucursalNombre; }

    public Integer getTotalTickets() { return totalTickets; }
    public void setTotalTickets(Integer totalTickets) { this.totalTickets = totalTickets; }

    public String getEncargadoNombre() { return encargadoNombre; }
    public void setEncargadoNombre(String encargadoNombre) { this.encargadoNombre = encargadoNombre; }
}
