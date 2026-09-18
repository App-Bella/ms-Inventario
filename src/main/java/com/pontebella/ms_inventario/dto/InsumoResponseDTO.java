package com.pontebella.ms_inventario.dto;

import java.util.UUID;

import com.pontebella.ms_inventario.model.Insumo;

public record InsumoResponseDTO(
        UUID id,
        String nombre,
        String descripcion,
        Insumo.CategoriaInsumo categoria,
        String unidadMedida,
        Integer stockActual,
        Integer stockMinimo,
        Insumo.EstadoInsumo estado,
        boolean stockBajo
) {
    public static InsumoResponseDTO fromEntity(Insumo insumo) {
        return new InsumoResponseDTO(
                insumo.getId(),
                insumo.getNombre(),
                insumo.getDescripcion(),
                insumo.getCategoria(),
                insumo.getUnidadMedida(),
                insumo.getStockActual(),
                insumo.getStockMinimo(),
                insumo.getEstado(),
                insumo.getStockActual() < insumo.getStockMinimo()
        );
    }
}