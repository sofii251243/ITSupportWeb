package com.sofia.itsupport.services;

import com.sofia.itsupport.dto.request.LoginRequest;
import com.sofia.itsupport.dto.response.LoginResponseDTO;
import com.sofia.itsupport.entities.Usuario;
import com.sofia.itsupport.enums.EstadoCuenta;
import com.sofia.itsupport.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ===========================================
    // LOGIN (temporal - sin encriptación)
    // ===========================================
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequest request) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));

        // Validar contraseña (temporal, sin encriptar)
        if (!usuario.getContrasenaHash().equals(request.getContrasena())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        // Validar estado de la cuenta
        if (usuario.getEstadoCuenta() == EstadoCuenta.suspendido) {
            throw new RuntimeException("La cuenta está suspendida. Contacta al administrador.");
        }

        // Crear respuesta
        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(usuario.getId());
        response.setNombreUsuario(usuario.getNombreUsuario());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol().name());
        response.setMensaje("Login exitoso");

        return response;
    }

    // ===========================================
    // CAMBIAR CONTRASEÑA (temporal)
    // ===========================================
    @Transactional
    public String cambiarContrasena(Long usuarioId, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar contraseña actual
        if (!usuario.getContrasenaHash().equals(contrasenaActual)) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        // Actualizar contraseña
        usuario.setContrasenaHash(contrasenaNueva);
        usuarioRepository.save(usuario);

        return "Contraseña actualizada correctamente";
    }
}