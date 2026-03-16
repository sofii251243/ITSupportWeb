package com.sofia.itsupport.dto.response;

import com.sofia.itsupport.enums.EstadoGeneral;
import java.util.List;

public class SucursalResponseDTO {
    private Long id;
    private String nombre;
    private EstadoGeneral estado;
    private Integer totalAreas;        // Cuántas áreas tiene
    private Integer totalTickets;       // Cuántos tickets en total
    private List<String> areasNombres;  // Solo nombres, no objetos completos

    public SucursalResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public EstadoGeneral getEstado() { return estado; }
    public void setEstado(EstadoGeneral estado) { this.estado = estado; }

    public Integer getTotalAreas() { return totalAreas; }
    public void setTotalAreas(Integer totalAreas) { this.totalAreas = totalAreas; }

    public Integer getTotalTickets() { return totalTickets; }
    public void setTotalTickets(Integer totalTickets) { this.totalTickets = totalTickets; }

    public List<String> getAreasNombres() { return areasNombres; }
    public void setAreasNombres(List<String> areasNombres) { this.areasNombres = areasNombres; }
}