package com.sofia.itsupport.entities;

import com.sofia.itsupport.enums.EstadoGeneral;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sucursales")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "direccion_fisica", length = 255, nullable = false)
    private String direccionFisica;

    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @Column(name = "horario_operacion", length = 50, nullable = false)
    private String horarioOperacion; // Ej: "24 horas", "Lun-Vie 9-18"

    @Column(name = "zona", length = 100, nullable = false)
    private String zona;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoGeneral estado = EstadoGeneral.activo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Relación OneToMany con Area
    @OneToMany(mappedBy = "sucursal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Area> areas = new ArrayList<>();

    // Constructores
    public Sucursal() {}

    public Sucursal(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters (incluyendo los nuevos)
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

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<Area> getAreas() { return areas; }
    public void setAreas(List<Area> areas) { this.areas = areas; }

    public void addArea(Area area) {
        areas.add(area);
        area.setSucursal(this);
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}