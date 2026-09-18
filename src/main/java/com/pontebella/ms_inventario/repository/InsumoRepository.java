package com.pontebella.ms_inventario.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pontebella.ms_inventario.model.Insumo;

public interface InsumoRepository extends JpaRepository<Insumo, UUID> {

    List<Insumo> findByEstado(Insumo.EstadoInsumo estado);

    List<Insumo> findByCategoria(Insumo.CategoriaInsumo categoria);

    List<Insumo> findByNombreContainingIgnoreCase(String nombre);

    // RF-IN-03: insumos cuyo stock actual ya cayó por debajo del mínimo
    @Query("SELECT i FROM Insumo i WHERE i.stockActual < i.stockMinimo AND i.estado = 'ACTIVO'")
    List<Insumo> findConStockBajoUmbral();
}