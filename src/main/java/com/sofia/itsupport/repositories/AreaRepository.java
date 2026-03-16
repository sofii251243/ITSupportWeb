package com.sofia.itsupport.repositories;

import com.sofia.itsupport.entities.Area;
import com.sofia.itsupport.entities.Sucursal;
import com.sofia.itsupport.enums.EstadoGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    // Buscar áreas por sucursal
    List<Area> findBySucursal(Sucursal sucursal);

    // Buscar áreas activas de una sucursal
    List<Area> findBySucursalAndEstado(Sucursal sucursal, EstadoGeneral estado);

    // Buscar por nombre (útil para búsquedas)
    List<Area> findByNombreContainingIgnoreCase(String nombre);
}