package com.sofia.itsupport.dto.response;

import com.sofia.itsupport.enums.RolUsuario;
import com.sofia.itsupport.enums.EstadoCuenta;

public class UsuarioResponseDTO {
    private Long id;
    private String nombreUsuario;
    private String email;
    private RolUsuario rol;
    private EstadoCuenta estadoCuenta;
    private String areaNombre;
    private Long areaId;
    private Integer ticketsActivos;      // Tickets en proceso
    private Integer ticketsCreados;      // Tickets creados (si es encargado)
    private Integer ticketsResueltos;    // Tickets cerrados (si es técnico)

    public UsuarioResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public RolUsuario getRol() { return rol; }
    public void setRol(RolUsuario rol) { this.rol = rol; }

    public EstadoCuenta getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(EstadoCuenta estadoCuenta) { this.estadoCuenta = estadoCuenta; }

    public String getAreaNombre() { return areaNombre; }
    public void setAreaNombre(String areaNombre) { this.areaNombre = areaNombre; }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public Integer getTicketsActivos() { return ticketsActivos; }
    public void setTicketsActivos(Integer ticketsActivos) { this.ticketsActivos = ticketsActivos; }

    public Integer getTicketsCreados() { return ticketsCreados; }
    public void setTicketsCreados(Integer ticketsCreados) { this.ticketsCreados = ticketsCreados; }

    public Integer getTicketsResueltos() { return ticketsResueltos; }
    public void setTicketsResueltos(Integer ticketsResueltos) { this.ticketsResueltos = ticketsResueltos; }
}