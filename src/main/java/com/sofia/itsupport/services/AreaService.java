package com.sofia.itsupport.services;

import com.sofia.itsupport.dto.response.AreaResponseDTO;
import com.sofia.itsupport.entities.Area;
import com.sofia.itsupport.entities.Sucursal;
import com.sofia.itsupport.entities.Usuario;
import com.sofia.itsupport.enums.EstadoGeneral;
import com.sofia.itsupport.enums.RolUsuario;
import com.sofia.itsupport.repositories.AreaRepository;
import com.sofia.itsupport.repositories.SucursalRepository;
import com.sofia.itsupport.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ===========================================
    // CREAR ÁREA
    // ===========================================
    @Transactional
    public AreaResponseDTO crearArea(String nombre, Long sucursalId) {
        // Validar que la sucursal existe
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + sucursalId));

        // Validar que no exista un área con el mismo nombre en la misma sucursal
        boolean existe = sucursal.getAreas().stream()
                .anyMatch(a -> a.getNombre().equalsIgnoreCase(nombre));

        if (existe) {
            throw new RuntimeException("Ya existe un área con el nombre '" + nombre + "' en esta sucursal");
        }

        // Crear área
        Area area = new Area();
        area.setNombre(nombre);
        area.setEstado(EstadoGeneral.activo);
        area.setSucursal(sucursal);

        area = areaRepository.save(area);

        return convertirADTO(area);
    }

    // ===========================================
    // OBTENER ÁREA POR ID
    // ===========================================
    @Transactional(readOnly = true)
    public AreaResponseDTO obtenerAreaPorId(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + id));
        return convertirADTO(area);
    }

    // ===========================================
    // LISTAR TODAS LAS ÁREAS
    // ===========================================
    @Transactional(readOnly = true)
    public List<AreaResponseDTO> listarTodas() {
        return areaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // LISTAR ÁREAS POR SUCURSAL
    // ===========================================
    @Transactional(readOnly = true)
    public List<AreaResponseDTO> listarPorSucursal(Long sucursalId) {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        return areaRepository.findBySucursal(sucursal).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // LISTAR ÁREAS ACTIVAS DE UNA SUCURSAL
    // ===========================================
    @Transactional(readOnly = true)
    public List<AreaResponseDTO> listarActivasPorSucursal(Long sucursalId) {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        return areaRepository.findBySucursalAndEstado(sucursal, EstadoGeneral.activo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // ACTUALIZAR ÁREA
    // ===========================================
    @Transactional
    public AreaResponseDTO actualizarArea(Long id, String nuevoNombre) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        area.setNombre(nuevoNombre);
        area = areaRepository.save(area);

        return convertirADTO(area);
    }

    // ===========================================
    // DESACTIVAR ÁREA (soft delete)
    // ===========================================
    @Transactional
    public AreaResponseDTO desactivarArea(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        // Verificar si tiene tickets activos
        boolean tieneTicketsActivos = area.getTickets().stream()
                .anyMatch(t -> t.getEstado().name().equals("abierto") ||
                        t.getEstado().name().equals("en_proceso"));

        if (tieneTicketsActivos) {
            throw new RuntimeException("No se puede desactivar el área porque tiene tickets activos");
        }

        area.setEstado(EstadoGeneral.desactivado);
        area = areaRepository.save(area);

        return convertirADTO(area);
    }

    // ===========================================
    // ACTIVAR ÁREA
    // ===========================================
    @Transactional
    public AreaResponseDTO activarArea(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        area.setEstado(EstadoGeneral.activo);
        area = areaRepository.save(area);

        return convertirADTO(area);
    }

    // ===========================================
    // ASIGNAR ENCARGADO A ÁREA
    // ===========================================
    @Transactional
    public AreaResponseDTO asignarEncargado(Long areaId, Long usuarioId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que el usuario sea encargado
        if (usuario.getRol() != RolUsuario.encargado) {
            throw new RuntimeException("El usuario no es un encargado válido");
        }

        // Validar que el encargado no esté ya asignado a otra área
        if (usuario.getArea() != null && !usuario.getArea().getId().equals(areaId)) {
            throw new RuntimeException("El encargado ya está asignado a otra área");
        }

        usuario.setArea(area);
        usuarioRepository.save(usuario);

        return convertirADTO(area);
    }

    // ===========================================
    // MÉTODO PRIVADO: Convertir Entity → DTO
    // ===========================================
    private AreaResponseDTO convertirADTO(Area area) {
        AreaResponseDTO dto = new AreaResponseDTO();

        dto.setId(area.getId());
        dto.setNombre(area.getNombre());
        dto.setEstado(area.getEstado());

        if (area.getSucursal() != null) {
            dto.setSucursalId(area.getSucursal().getId());
            dto.setSucursalNombre(area.getSucursal().getNombre());
        }

        // Total de tickets del área
        dto.setTotalTickets(area.getTickets().size());

        // Buscar encargado (el usuario con rol encargado que tenga esta área)
        String encargado = area.getUsuarios().stream()
                .filter(u -> u.getRol() == RolUsuario.encargado)
                .findFirst()
                .map(Usuario::getNombreUsuario)
                .orElse(null);

        dto.setEncargadoNombre(encargado);

        return dto;
    }

    @Transactional
    public AreaResponseDTO cambiarSucursal(Long areaId, Long nuevaSucursalId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + areaId));
        Sucursal nuevaSucursal = sucursalRepository.findById(nuevaSucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + nuevaSucursalId));

        area.setSucursal(nuevaSucursal);
        area = areaRepository.save(area);
        return convertirADTO(area);
    }
}