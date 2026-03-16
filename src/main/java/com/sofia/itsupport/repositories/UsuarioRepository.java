package com.sofia.itsupport.repositories;

import com.sofia.itsupport.entities.Usuario;
import com.sofia.itsupport.enums.RolUsuario;
import com.sofia.itsupport.enums.EstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por email (para login)
    Optional<Usuario> findByEmail(String email);

    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Buscar todos los técnicos activos (para asignación automática)
    List<Usuario> findByRolAndEstadoCuenta(RolUsuario rol, EstadoCuenta estado);

    // Buscar encargados de un área específica
    List<Usuario> findByRolAndAreaId(RolUsuario rol, Long areaId);

    // Verificar si existe un email
    boolean existsByEmail(String email);

    // Verificar si existe un nombre de usuario
    boolean existsByNombreUsuario(String nombreUsuario);
}