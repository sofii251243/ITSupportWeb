package com.sofia.itsupport.repositories;

import com.sofia.itsupport.entities.Ticket;
import com.sofia.itsupport.entities.Usuario;
import com.sofia.itsupport.enums.EstadoTicket;
import com.sofia.itsupport.enums.PrioridadTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Buscar tickets por estado
    List<Ticket> findByEstado(EstadoTicket estado);

    // Buscar tickets por técnico asignado
    List<Ticket> findByTecnico(Usuario tecnico);

    // Buscar tickets creados por un encargado
    List<Ticket> findByEncargado(Usuario encargado);

    // Buscar tickets de un área específica
    List<Ticket> findByAreaId(Long areaId);

    // Tickets abiertos (para el técnico)
    List<Ticket> findByEstadoAndTecnicoIsNull(EstadoTicket estado);

    // Tickets de alta prioridad abiertos
    List<Ticket> findByPrioridadAndEstado(PrioridadTicket prioridad, EstadoTicket estado);

    // IMPORTANTE: Tickets vencidos para SLA (más de 1 hora)
    @Query("SELECT t FROM Ticket t WHERE t.estado = 'abierto' AND t.fechaCreacion < :fechaLimite")
    List<Ticket> findTicketsVencidos(@Param("fechaLimite") LocalDateTime fechaLimite);

    // Tickets de un técnico con filtro de estado
    List<Ticket> findByTecnicoAndEstado(Usuario tecnico, EstadoTicket estado);

    // Contar tickets por estado
    long countByEstado(EstadoTicket estado);

    // Contar tickets de un técnico en proceso
    long countByTecnicoAndEstado(Usuario tecnico, EstadoTicket estado);
}