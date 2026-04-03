package com.sofia.itsupport.services;

import com.sofia.itsupport.dto.request.AdminUsuarioUpdateRequest;
import com.sofia.itsupport.dto.request.CrearUsuarioRequest;
import com.sofia.itsupport.dto.request.UsuarioUpdateRequest;
import com.sofia.itsupport.dto.response.UsuarioResponseDTO;
import com.sofia.itsupport.entities.Area;
import com.sofia.itsupport.entities.Usuario;
import com.sofia.itsupport.enums.EstadoCuenta;
import com.sofia.itsupport.enums.RolUsuario;
import com.sofia.itsupport.repositories.AreaRepository;
import com.sofia.itsupport.repositories.TicketRepository;
import com.sofia.itsupport.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // ===========================================
    // REGISTRAR USUARIO
    // ===========================================
    @Transactional
    public UsuarioResponseDTO registrarUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        Area area = null;
        if (request.getRol() == RolUsuario.encargado) {
            if (request.getAreaId() == null) {
                throw new RuntimeException("Un encargado debe tener un área asignada");
            }
            area = areaRepository.findById(request.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Área no encontrada"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setEmail(request.getEmail());
        usuario.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(request.getRol());
        usuario.setEstadoCuenta(EstadoCuenta.activo);
        usuario.setArea(area);

        usuario = usuarioRepository.save(usuario);
        return convertirADTO(usuario);
    }

    // ===========================================
    // OBTENER USUARIO POR ID
    // ===========================================
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertirADTO(usuario);
    }

    // ===========================================
    // LISTAR TODOS LOS USUARIOS
    // ===========================================
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // LISTAR TÉCNICOS ACTIVOS
    // ===========================================
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTecnicosActivos() {
        return usuarioRepository.findByRolAndEstadoCuenta(RolUsuario.tecnico, EstadoCuenta.activo)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // SUSPENDER USUARIO
    // ===========================================
    @Transactional
    public UsuarioResponseDTO suspenderUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstadoCuenta(EstadoCuenta.suspendido);
        usuario = usuarioRepository.save(usuario);
        return convertirADTO(usuario);
    }

    // ===========================================
    // ACTIVAR USUARIO
    // ===========================================
    @Transactional
    public UsuarioResponseDTO activarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstadoCuenta(EstadoCuenta.activo);
        usuario = usuarioRepository.save(usuario);
        return convertirADTO(usuario);
    }

    // ===========================================
    // CAMBIAR CONTRASEÑA (propio usuario)
    // ===========================================
    @Transactional
    public String cambiarContrasena(Long usuarioId, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasenaHash())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }
        usuario.setContrasenaHash(passwordEncoder.encode(contrasenaNueva));
        usuarioRepository.save(usuario);
        return "Contraseña actualizada correctamente";
    }

    // ===========================================
    // OBTENER USUARIO POR EMAIL
    // ===========================================
    public UsuarioResponseDTO obtenerUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return convertirADTO(usuario);
    }

    // ===========================================
    // ACTUALIZAR PERFIL PROPIO (usuario autenticado)
    // ===========================================
    @Transactional
    public UsuarioResponseDTO actualizarUsuarioAutenticado(UsuarioUpdateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar nombre de usuario
        if (request.getNombreUsuario() != null && !request.getNombreUsuario().isBlank()) {
            if (!usuario.getNombreUsuario().equals(request.getNombreUsuario()) &&
                    usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
            usuario.setNombreUsuario(request.getNombreUsuario());
        }

        // Actualizar apellido paterno
        if (request.getApellidoPaterno() != null && !request.getApellidoPaterno().isBlank()) {
            usuario.setApellidoPaterno(request.getApellidoPaterno());
        }

        // Actualizar apellido materno
        if (request.getApellidoMaterno() != null && !request.getApellidoMaterno().isBlank()) {
            usuario.setApellidoMaterno(request.getApellidoMaterno());
        }

        usuario = usuarioRepository.save(usuario);
        return convertirADTO(usuario);
    }

    // ===========================================
    // OBTENER ID DEL USUARIO AUTENTICADO
    // ===========================================
    public Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }

    // ===========================================
    // ACTUALIZAR USUARIO POR ADMIN (PUT completo)
    // ===========================================
    @Transactional
    public UsuarioResponseDTO actualizarUsuarioPorAdmin(Long id, AdminUsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar unicidad de email (si cambió)
        if (!usuario.getEmail().equals(request.getEmail()) &&
                usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        // Validar unicidad de nombre de usuario (si cambió)
        if (!usuario.getNombreUsuario().equals(request.getNombreUsuario()) &&
                usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        // Actualizar campos obligatorios
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setEmail(request.getEmail());
        usuario.setRol(request.getRol());

        // Actualizar contraseña solo si se proporciona y no está vacía
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
        }

        usuario = usuarioRepository.save(usuario);
        return convertirADTO(usuario);
    }

    // ===========================================
    // CONVERSIÓN A DTO
    // ===========================================
    private UsuarioResponseDTO convertirADTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setEstadoCuenta(usuario.getEstadoCuenta());

        if (usuario.getArea() != null) {
            dto.setAreaNombre(usuario.getArea().getNombre());
            dto.setAreaId(usuario.getArea().getId());
        }

        // Tickets según el rol
        if (usuario.getRol() == RolUsuario.tecnico) {
            long activos = usuario.getTicketsAsignados().stream()
                    .filter(t -> t.getEstado().name().equals("en_proceso"))
                    .count();
            dto.setTicketsActivos((int) activos);
            long resueltos = usuario.getTicketsAsignados().stream()
                    .filter(t -> t.getEstado().name().equals("cerrado"))
                    .count();
            dto.setTicketsResueltos((int) resueltos);
        } else if (usuario.getRol() == RolUsuario.encargado) {
            dto.setTicketsCreados(usuario.getTicketsCreados().size());
        }

        return dto;
    }
}