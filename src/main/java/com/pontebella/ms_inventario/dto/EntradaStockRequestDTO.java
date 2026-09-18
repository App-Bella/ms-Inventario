package com.pontebella.ms_inventario.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EntradaStockRequestDTO(
        @NotNull(message = "Debe indicar el insumo")
        UUID insumoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a 0")
        Integer cantidad
) {
}