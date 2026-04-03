package com.sofia.itsupport.dto.response;

import com.sofia.itsupport.enums.EstadoGeneral;
import java.util.List;

public class SucursalResponseDTO {
    private Long id;
    private String nombre;
    private String direccionFisica;
    private String telefono;
    private String horarioOperacion;
    private String zona;
    private EstadoGeneral estado;
    private Integer totalAreas;
    private Integer totalTickets;
    private List<String> areasNombres;

    public SucursalResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccionFisica() { return direccionFisica; }
    public void setDireccionFisica(String direccionFisica) { this.direccionFisica = direccionFisica; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getHorarioOperacion() { return horarioOperacion; }
    public void setHorarioOperacion(String horarioOperacion) { this.horarioOperacion = horarioOperacion; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public EstadoGeneral getEstado() { return estado; }
    public void setEstado(EstadoGeneral estado) { this.estado = estado; }

    public Integer getTotalAreas() { return totalAreas; }
    public void setTotalAreas(Integer totalAreas) { this.totalAreas = totalAreas; }

    public Integer getTotalTickets() { return totalTickets; }
    public void setTotalTickets(Integer totalTickets) { this.totalTickets = totalTickets; }

    public List<String> getAreasNombres() { return areasNombres; }
    public void setAreasNombres(List<String> areasNombres) { this.areasNombres = areasNombres; }
}