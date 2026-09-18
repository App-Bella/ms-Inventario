package com.pontebella.ms_inventario.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pontebella.ms_inventario.model.MovimientoStock;

public record MovimientoStockResponseDTO(
        UUID id,
        UUID insumoId,
        String insumoNombre,
        MovimientoStock.TipoMovimiento tipo,
        Integer cantidad,
        MovimientoStock.MotivoMovimiento motivo,
        UUID referenciaId,
        LocalDateTime fecha
) {
    public static MovimientoStockResponseDTO fromEntity(MovimientoStock movimiento) {
        return new MovimientoStockResponseDTO(
                movimiento.getId(),
                movimiento.getInsumo().getId(),
                movimiento.getInsumo().getNombre(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getMotivo(),
                movimiento.getReferenciaId(),
                movimiento.getFecha()
        );
    }
}