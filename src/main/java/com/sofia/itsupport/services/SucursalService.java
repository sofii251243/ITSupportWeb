package com.sofia.itsupport.services;

import com.sofia.itsupport.dto.request.CrearSucursalRequest;
import com.sofia.itsupport.dto.response.SucursalResponseDTO;
import com.sofia.itsupport.entities.Sucursal;
import com.sofia.itsupport.enums.EstadoGeneral;
import com.sofia.itsupport.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    // ===========================================
    // CREAR SUCURSAL
    // ===========================================
    @Transactional
    public SucursalResponseDTO crearSucursal(CrearSucursalRequest request) {
        // Validar que no exista una sucursal con el mismo nombre
        boolean existe = sucursalRepository.findAll().stream()
                .anyMatch(s -> s.getNombre().equalsIgnoreCase(request.getNombre()));

        if (existe) {
            throw new RuntimeException("Ya existe una sucursal con el nombre: " + request.getNombre());
        }

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccionFisica(request.getDireccionFisica());
        sucursal.setTelefono(request.getTelefono());
        sucursal.setHorarioOperacion(request.getHorarioOperacion());
        sucursal.setZona(request.getZona());
        sucursal.setEstado(EstadoGeneral.activo);

        sucursal = sucursalRepository.save(sucursal);

        return convertirADTO(sucursal);
    }

    // ===========================================
    // OBTENER SUCURSAL POR ID
    // ===========================================
    @Transactional(readOnly = true)
    public SucursalResponseDTO obtenerSucursalPorId(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
        return convertirADTO(sucursal);
    }

    // ===========================================
    // LISTAR TODAS LAS SUCURSALES
    // ===========================================
    @Transactional(readOnly = true)
    public List<SucursalResponseDTO> listarTodas() {
        return sucursalRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // LISTAR SUCURSALES ACTIVAS
    // ===========================================
    @Transactional(readOnly = true)
    public List<SucursalResponseDTO> listarActivas() {
        return sucursalRepository.findByEstado(EstadoGeneral.activo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // ACTUALIZAR SUCURSAL (ahora actualiza todos los campos)
    // ===========================================
    @Transactional
    public SucursalResponseDTO actualizarSucursal(Long id, CrearSucursalRequest request) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        // Actualizar todos los campos
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccionFisica(request.getDireccionFisica());
        sucursal.setTelefono(request.getTelefono());
        sucursal.setHorarioOperacion(request.getHorarioOperacion());
        sucursal.setZona(request.getZona());

        sucursal = sucursalRepository.save(sucursal);

        return convertirADTO(sucursal);
    }

    // ===========================================
    // DESACTIVAR SUCURSAL (soft delete)
    // ===========================================
    @Transactional
    public SucursalResponseDTO desactivarSucursal(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        // Verificar si tiene áreas activas
        boolean tieneAreasActivas = sucursal.getAreas().stream()
                .anyMatch(a -> a.getEstado() == EstadoGeneral.activo);

        if (tieneAreasActivas) {
            throw new RuntimeException("No se puede desactivar la sucursal porque tiene áreas activas");
        }

        sucursal.setEstado(EstadoGeneral.desactivado);
        sucursal = sucursalRepository.save(sucursal);

        return convertirADTO(sucursal);
    }

    // ===========================================
    // ACTIVAR SUCURSAL
    // ===========================================
    @Transactional
    public SucursalResponseDTO activarSucursal(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        sucursal.setEstado(EstadoGeneral.activo);
        sucursal = sucursalRepository.save(sucursal);

        return convertirADTO(sucursal);
    }

    // ===========================================
    // MÉTODO PRIVADO: Convertir Entity → DTO
    // ===========================================
    private SucursalResponseDTO convertirADTO(Sucursal sucursal) {
        SucursalResponseDTO dto = new SucursalResponseDTO();

        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccionFisica(sucursal.getDireccionFisica());
        dto.setTelefono(sucursal.getTelefono());
        dto.setHorarioOperacion(sucursal.getHorarioOperacion());
        dto.setZona(sucursal.getZona());
        dto.setEstado(sucursal.getEstado());

        // Total de áreas
        dto.setTotalAreas(sucursal.getAreas().size());

        // Total de tickets (sumando los de cada área)
        int totalTickets = sucursal.getAreas().stream()
                .mapToInt(area -> area.getTickets().size())
                .sum();
        dto.setTotalTickets(totalTickets);

        // Nombres de las áreas (solo los primeros 5 para no saturar)
        List<String> nombresAreas = sucursal.getAreas().stream()
                .limit(5)
                .map(area -> area.getNombre())
                .collect(Collectors.toList());
        dto.setAreasNombres(nombresAreas);

        return dto;
    }
}