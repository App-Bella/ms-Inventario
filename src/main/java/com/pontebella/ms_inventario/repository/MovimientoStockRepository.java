package com.pontebella.ms_inventario.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pontebella.ms_inventario.model.MovimientoStock;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, UUID> {

    List<MovimientoStock> findByInsumo_IdOrderByFechaDesc(UUID insumoId);

    List<MovimientoStock> findByReferenciaId(UUID referenciaId);

    List<MovimientoStock> findByMotivo(MovimientoStock.MotivoMovimiento motivo);
}