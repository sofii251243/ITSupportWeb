package com.sofia.itsupport.dto.response;

public class LoginResponseDTO {
    private Long id;
    private String nombreUsuario;
    private String email;
    private String rol;
    private String token;  // Para JWT (lo veremos después)
    private String mensaje;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
