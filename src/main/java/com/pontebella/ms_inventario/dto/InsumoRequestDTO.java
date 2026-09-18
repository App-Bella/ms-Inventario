package com.pontebella.ms_inventario.dto;

import com.pontebella.ms_inventario.model.Insumo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InsumoRequestDTO(
        @NotBlank(message = "El nombre del insumo es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "La categoría es obligatoria")
        Insumo.CategoriaInsumo categoria,

        @NotBlank(message = "La unidad de medida es obligatoria")
        String unidadMedida,

        @NotNull(message = "El stock inicial es obligatorio")
        @PositiveOrZero(message = "El stock inicial no puede ser negativo")
        Integer stockActual,

        @NotNull(message = "El stock mínimo es obligatorio")
        @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
        Integer stockMinimo,

        @NotNull(message = "El estado es obligatorio")
        Insumo.EstadoInsumo estado
) {
}