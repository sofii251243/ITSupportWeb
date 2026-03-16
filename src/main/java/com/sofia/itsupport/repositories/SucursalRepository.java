package com.sofia.itsupport.repositories;

import com.sofia.itsupport.entities.Sucursal;
import com.sofia.itsupport.enums.EstadoGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    // Buscar sucursales activas
    List<Sucursal> findByEstado(EstadoGeneral estado);

    // Buscar por nombre
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);
}