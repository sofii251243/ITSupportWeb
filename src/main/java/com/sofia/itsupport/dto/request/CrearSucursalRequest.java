package com.sofia.itsupport.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CrearSucursalRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "La dirección física es obligatoria")
    private String direccionFisica;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    private String telefono;

    @NotBlank(message = "El horario de operación es obligatorio")
    private String horarioOperacion;

    @NotBlank(message = "La zona es obligatoria")
    private String zona;

    // Getters y Setters
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
}