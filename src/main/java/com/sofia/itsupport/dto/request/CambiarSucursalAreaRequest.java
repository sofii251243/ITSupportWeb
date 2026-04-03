package com.sofia.itsupport.dto.request;

import jakarta.validation.constraints.NotNull;

public class CambiarSucursalAreaRequest {

    @NotNull(message = "El ID de la nueva sucursal es obligatorio")
    private Long sucursalId;

    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
}
