package com.sofia.itsupport.repositories;

import com.sofia.itsupport.entities.Comentario;
import com.sofia.itsupport.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Buscar comentarios de un ticket (ordenados por fecha)
    List<Comentario> findByTicketOrderByFechaCreacionAsc(Ticket ticket);

    // Buscar comentarios de un usuario
    List<Comentario> findByUsuarioId(Long usuarioId);
}
