package com.sofia.itsupport.entities;

import com.sofia.itsupport.enums.RolUsuario;
import com.sofia.itsupport.enums.EstadoCuenta;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", length = 50, nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "contrasena_hash", length = 255, nullable = false)
    private String contrasenaHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    private RolUsuario rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", nullable = false)
    private EstadoCuenta estadoCuenta = EstadoCuenta.activo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Relación ManyToOne con Area (solo aplica si es encargado)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id")
    private Area area;

    // Relación OneToMany con Tickets (como encargado - los tickets que creó)
    @OneToMany(mappedBy = "encargado")
    private List<Ticket> ticketsCreados = new ArrayList<>();

    // Relación OneToMany con Tickets (como técnico - los tickets que atiende)
    @OneToMany(mappedBy = "tecnico")
    private List<Ticket> ticketsAsignados = new ArrayList<>();

    // Relación OneToMany con Comentarios (comentarios que escribió)
    @OneToMany(mappedBy = "usuario")
    private List<Comentario> comentarios = new ArrayList<>();

    // Constructores
    public Usuario() {}

    public Usuario(String nombreUsuario, String email, String contrasenaHash, RolUsuario rol) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasenaHash = contrasenaHash;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Ticket> getTicketsCreados() {
        return ticketsCreados;
    }

    public void setTicketsCreados(List<Ticket> ticketsCreados) {
        this.ticketsCreados = ticketsCreados;
    }

    public List<Ticket> getTicketsAsignados() {
        return ticketsAsignados;
    }

    public void setTicketsAsignados(List<Ticket> ticketsAsignados) {
        this.ticketsAsignados = ticketsAsignados;
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

    // Método helper para validar si es encargado
    public boolean isEncargado() {
        return rol == RolUsuario.encargado;
    }

    // Método helper para validar si es tecnico
    public boolean isTecnico() {
        return rol == RolUsuario.tecnico;
    }

    // Método helper para validar si es admin
    public boolean isAdmin() {
        return rol == RolUsuario.admin;
    }
}