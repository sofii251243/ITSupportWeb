package com.sofia.itsupport.dto.response;

import com.sofia.itsupport.enums.PrioridadTicket;
import com.sofia.itsupport.enums.EstadoTicket;
import com.sofia.itsupport.enums.TipoAsignacion;
import java.time.LocalDateTime;

public class TicketResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private PrioridadTicket prioridad;
    private EstadoTicket estado;
    private TipoAsignacion tipoAsignacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaCierre;

    // Datos del encargado (solo texto)
    private String encargadoNombre;
    private String encargadoEmail;
    private Long encargadoId;

    // Datos del área (solo texto)
    private String areaNombre;
    private Long areaId;

    // Datos de la sucursal (solo texto)
    private String sucursalNombre;
    private Long sucursalId;

    // Datos del técnico (puede ser null)
    private String tecnicoNombre;
    private Long tecnicoId;

    // Datos calculados
    private Integer minutosEsperando;
    private Integer numeroComentarios;
    private Boolean esVencido; // true si > 1 hora sin asignar

    // Constructor vacío
    public TicketResponseDTO() {}

    // Getters y Setters (generarlos con el IDE o manualmente)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public PrioridadTicket getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadTicket prioridad) { this.prioridad = prioridad; }

    public EstadoTicket getEstado() { return estado; }
    public void setEstado(EstadoTicket estado) { this.estado = estado; }

    public TipoAsignacion getTipoAsignacion() { return tipoAsignacion; }
    public void setTipoAsignacion(TipoAsignacion tipoAsignacion) { this.tipoAsignacion = tipoAsignacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public String getEncargadoNombre() { return encargadoNombre; }
    public void setEncargadoNombre(String encargadoNombre) { this.encargadoNombre = encargadoNombre; }

    public String getEncargadoEmail() { return encargadoEmail; }
    public void setEncargadoEmail(String encargadoEmail) { this.encargadoEmail = encargadoEmail; }

    public Long getEncargadoId() { return encargadoId; }
    public void setEncargadoId(Long encargadoId) { this.encargadoId = encargadoId; }

    public String getAreaNombre() { return areaNombre; }
    public void setAreaNombre(String areaNombre) { this.areaNombre = areaNombre; }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public String getSucursalNombre() { return sucursalNombre; }
    public void setSucursalNombre(String sucursalNombre) { this.sucursalNombre = sucursalNombre; }

    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }

    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }

    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }

    public Integer getMinutosEsperando() { return minutosEsperando; }
    public void setMinutosEsperando(Integer minutosEsperando) { this.minutosEsperando = minutosEsperando; }

    public Integer getNumeroComentarios() { return numeroComentarios; }
    public void setNumeroComentarios(Integer numeroComentarios) { this.numeroComentarios = numeroComentarios; }

    public Boolean getEsVencido() { return esVencido; }
    public void setEsVencido(Boolean esVencido) { this.esVencido = esVencido; }
}