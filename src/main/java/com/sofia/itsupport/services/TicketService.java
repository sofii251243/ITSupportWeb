package com.sofia.itsupport.services;


import com.sofia.itsupport.dto.request.CrearTicketRequest;
import com.sofia.itsupport.dto.request.AsignarTicketRequest;
import com.sofia.itsupport.dto.response.TicketResponseDTO;
import com.sofia.itsupport.entities.*;
import com.sofia.itsupport.enums.*;
import com.sofia.itsupport.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    // ===========================================
    // CREAR TICKET (el más importante)
    // ===========================================
    @Transactional
    public TicketResponseDTO crearTicket(CrearTicketRequest request) {
        // PASO 1: Validar que el encargado existe
        Usuario encargado = usuarioRepository.findById(request.getEncargadoId())
                .orElseThrow(() -> new RuntimeException("Encargado no encontrado con ID: " + request.getEncargadoId()));

        // PASO 2: Validar que sea realmente un encargado
        if (encargado.getRol() != RolUsuario.encargado) {
            throw new RuntimeException("El usuario con ID " + request.getEncargadoId() + " no es un encargado");
        }

        // PASO 3: Validar que el área existe
        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + request.getAreaId()));

        // PASO 4: Validar que el encargado pertenece a esa área
        if (!area.getId().equals(encargado.getArea().getId())) {
            throw new RuntimeException("El encargado no pertenece al área especificada");
        }

        // PASO 5: Crear el ticket
        Ticket ticket = new Ticket();
        ticket.setTitulo(request.getTitulo());
        ticket.setDescripcion(request.getDescripcion());
        ticket.setPrioridad(request.getPrioridad() != null ? request.getPrioridad() : PrioridadTicket.media);
        ticket.setEncargado(encargado);
        ticket.setArea(area);
        ticket.setEstado(EstadoTicket.abierto);

        // PASO 6: Guardar en BD
        ticket = ticketRepository.save(ticket);

        TicketResponseDTO dto = convertirADTO(ticket);   // ← AHORA dto SÍ EXISTE

// PASO 8: Enviar notificación WebSocket
        messagingTemplate.convertAndSend("/topic/tickets/nuevo", dto);

        // PASO 7: Convertir a DTO y retornar
        return dto;
    }

    // ===========================================
    // OBTENER TICKET POR ID
    // ===========================================
    @Transactional(readOnly = true)
    public TicketResponseDTO obtenerTicketPorId(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
        return convertirADTO(ticket);
    }

    // ===========================================
    // LISTAR TODOS LOS TICKETS
    // ===========================================
    @Transactional(readOnly = true)
    public List<TicketResponseDTO> listarTodos() {
        return ticketRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // TICKETS DISPONIBLES PARA TÉCNICO
    // ===========================================
    @Transactional(readOnly = true)
    public List<TicketResponseDTO> ticketsDisponibles() {
        return ticketRepository.findByEstadoAndTecnicoIsNull(EstadoTicket.abierto)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ===========================================
    // ASIGNAR TICKET A TÉCNICO
    // ===========================================
    @Transactional
    public TicketResponseDTO asignarTicket(AsignarTicketRequest request) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        if (ticket.getEstado() != EstadoTicket.abierto) {
            throw new RuntimeException("El ticket no está disponible para asignar");
        }

        Usuario tecnico = usuarioRepository.findById(request.getTecnicoId())
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        if (tecnico.getRol() != RolUsuario.tecnico) {
            throw new RuntimeException("El usuario no es un técnico válido");
        }
        if (tecnico.getEstadoCuenta() != EstadoCuenta.activo) {
            throw new RuntimeException("El técnico no está activo");
        }

        ticket.asignarTecnico(tecnico, TipoAsignacion.manual);
        ticket = ticketRepository.save(ticket);

        Comentario comentario = new Comentario(
                "Ticket asignado manualmente a " + tecnico.getNombreUsuario(),
                ticket,
                tecnico
        );
        comentarioRepository.save(comentario);

        // Convertir a DTO para notificaciones
        TicketResponseDTO dto = convertirADTO(ticket);

        // Notificar al técnico asignado
        messagingTemplate.convertAndSendToUser(
                tecnico.getNombreUsuario(),
                "/queue/asignacion",
                dto
        );

        // Notificar actualización general
        messagingTemplate.convertAndSend("/topic/tickets/actualizacion", dto);

        return dto;
    }

    // ===========================================
    // CERRAR TICKET
    // ===========================================
    @Transactional
    public TicketResponseDTO cerrarTicket(Long ticketId, Long tecnicoId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        if (ticket.getTecnico() == null || !ticket.getTecnico().getId().equals(tecnicoId)) {
            throw new RuntimeException("No tienes permiso para cerrar este ticket");
        }

        ticket.cerrar();
        ticket = ticketRepository.save(ticket);

        TicketResponseDTO dto = convertirADTO(ticket);

        if (ticket.getEncargado() != null) {
            messagingTemplate.convertAndSendToUser(
                    ticket.getEncargado().getNombreUsuario(),
                    "/queue/notificaciones",
                    dto
            );
        }

        messagingTemplate.convertAndSend("/topic/tickets/cerrado", dto);

        return dto;
    }

    // ===========================================
    // ASIGNACIÓN AUTOMÁTICA (SLA 1 HORA)
    // ===========================================
    @Transactional
    public int asignacionAutomatica() {
        LocalDateTime haceUnaHora = LocalDateTime.now().minusHours(1);
        List<Ticket> ticketsVencidos = ticketRepository.findTicketsVencidos(haceUnaHora);

        List<Usuario> tecnicosActivos = usuarioRepository
                .findByRolAndEstadoCuenta(RolUsuario.tecnico, EstadoCuenta.activo);

        if (tecnicosActivos.isEmpty()) {
            throw new RuntimeException("No hay técnicos activos para asignar");
        }

        int asignados = 0;
        for (Ticket ticket : ticketsVencidos) {
            int indice = (int) (Math.random() * tecnicosActivos.size());
            Usuario tecnico = tecnicosActivos.get(indice);

            ticket.asignarTecnico(tecnico, TipoAsignacion.automatica);
            ticketRepository.save(ticket);

            Comentario comentario = new Comentario(
                    "Ticket asignado automáticamente (SLA 1 hora)",
                    ticket,
                    tecnico
            );
            comentarioRepository.save(comentario);

            TicketResponseDTO dto = convertirADTO(ticket);
            messagingTemplate.convertAndSendToUser(
                    tecnico.getNombreUsuario(),
                    "/queue/asignacion",
                    dto
            );

            asignados++;
        }

        return asignados;
    }

    // ===========================================
    // MÉTODO PRIVADO: Convertir Entity → DTO
    // ===========================================
    private TicketResponseDTO convertirADTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();

        // Datos básicos
        dto.setId(ticket.getId());
        dto.setTitulo(ticket.getTitulo());
        dto.setDescripcion(ticket.getDescripcion());
        dto.setPrioridad(ticket.getPrioridad());
        dto.setEstado(ticket.getEstado());
        dto.setTipoAsignacion(ticket.getTipoAsignacion());
        dto.setFechaCreacion(ticket.getFechaCreacion());
        dto.setFechaAsignacion(ticket.getFechaAsignacion());
        dto.setFechaCierre(ticket.getFechaCierre());

        // Encargado
        if (ticket.getEncargado() != null) {
            dto.setEncargadoNombre(ticket.getEncargado().getNombreUsuario());
            dto.setEncargadoEmail(ticket.getEncargado().getEmail());
            dto.setEncargadoId(ticket.getEncargado().getId());
        }

        // Área
        if (ticket.getArea() != null) {
            dto.setAreaNombre(ticket.getArea().getNombre());
            dto.setAreaId(ticket.getArea().getId());

            // Sucursal
            if (ticket.getArea().getSucursal() != null) {
                dto.setSucursalNombre(ticket.getArea().getSucursal().getNombre());
                dto.setSucursalId(ticket.getArea().getSucursal().getId());
            }
        }

        // Técnico
        if (ticket.getTecnico() != null) {
            dto.setTecnicoNombre(ticket.getTecnico().getNombreUsuario());
            dto.setTecnicoId(ticket.getTecnico().getId());
        }

        // Cálculos
        if (ticket.getFechaCreacion() != null) {
            long minutos = ChronoUnit.MINUTES.between(ticket.getFechaCreacion(), LocalDateTime.now());
            dto.setMinutosEsperando((int) minutos);
            dto.setEsVencido(minutos > 60 && ticket.getEstado() == EstadoTicket.abierto);
        }

        // Comentarios
        if (ticket.getComentarios() != null) {
            dto.setNumeroComentarios(ticket.getComentarios().size());
        }

        return dto;
    }

    public List<TicketResponseDTO> getTicketsByTecnico(Usuario tecnico) {
        return ticketRepository.findByTecnico(tecnico).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDTO> getTicketsByEncargado(Usuario encargado) {
        return ticketRepository.findByEncargado(encargado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDTO> getTicketsByEncargadoId(Long encargadoId) {
        Usuario encargado = usuarioRepository.findById(encargadoId)
                .orElseThrow(() -> new RuntimeException("Encargado no encontrado"));
        return ticketRepository.findByEncargado(encargado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDTO> getTicketsByTecnicoId(Long tecnicoId) {
        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        return ticketRepository.findByTecnico(tecnico).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
}

