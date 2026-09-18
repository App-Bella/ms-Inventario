package com.pontebella.ms_inventario.dto;

import java.util.UUID;

import com.pontebella.ms_inventario.model.MovimientoStock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Usado tanto por el endpoint manual como por el futuro listener de RabbitMQ
// que consuma "cita.confirmada" o "matricula.creada" (RF-IN-02).
public record DescuentoStockRequestDTO(
        @NotNull(message = "Debe indicar el insumo")
        UUID insumoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a 0")
        Integer cantidad,

        @NotNull(message = "Debe indicar el motivo del descuento")
        MovimientoStock.MotivoMovimiento motivo,

        // Id de la cita o matrícula que origina el descuento (trazabilidad)
        UUID referenciaId
) {
}