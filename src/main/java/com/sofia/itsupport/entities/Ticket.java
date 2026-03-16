package com.sofia.itsupport.entities;

import com.sofia.itsupport.enums.PrioridadTicket;
import com.sofia.itsupport.enums.EstadoTicket;
import com.sofia.itsupport.enums.TipoAsignacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", length = 200, nullable = false)
    private String titulo;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false)
    private PrioridadTicket prioridad = PrioridadTicket.media;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTicket estado = EstadoTicket.abierto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_asignacion")
    private TipoAsignacion tipoAsignacion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    // Relación ManyToOne con Usuario (el encargado que creó el ticket)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "encargado_id", nullable = false)
    private Usuario encargado;

    // Relación ManyToOne con Usuario (el técnico que atiende el ticket)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

    // Relación ManyToOne con Area (el área que reporta)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    // Relación OneToMany con Comentarios (un ticket tiene muchos comentarios)
    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();

    // Constructores
    public Ticket() {}

    public Ticket(String titulo, String descripcion, Usuario encargado, Area area) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.encargado = encargado;
        this.area = area;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public PrioridadTicket getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadTicket prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }

    public TipoAsignacion getTipoAsignacion() {
        return tipoAsignacion;
    }

    public void setTipoAsignacion(TipoAsignacion tipoAsignacion) {
        this.tipoAsignacion = tipoAsignacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Usuario getEncargado() {
        return encargado;
    }

    public void setEncargado(Usuario encargado) {
        this.encargado = encargado;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    // Métodos de negocio
    public void asignarTecnico(Usuario tecnico, TipoAsignacion tipo) {
        this.tecnico = tecnico;
        this.tipoAsignacion = tipo;
        this.fechaAsignacion = LocalDateTime.now();
        this.estado = EstadoTicket.en_proceso;
    }

    public void cerrar() {
        this.estado = EstadoTicket.cerrado;
        this.fechaCierre = LocalDateTime.now();
    }

    public void anular() {
        this.estado = EstadoTicket.anulado;
        this.fechaCierre = LocalDateTime.now();
    }

    public boolean estaVencidoSLA() {
        if (this.estado != EstadoTicket.abierto) return false;
        return fechaCreacion.plusHours(1).isBefore(LocalDateTime.now());
    }
}