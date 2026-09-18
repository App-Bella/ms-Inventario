package com.pontebella.ms_inventario.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pontebella.ms_inventario.dto.DescuentoStockRequestDTO;
import com.pontebella.ms_inventario.dto.EntradaStockRequestDTO;
import com.pontebella.ms_inventario.dto.MovimientoStockResponseDTO;
import com.pontebella.ms_inventario.exception.BusinessException;
import com.pontebella.ms_inventario.model.Insumo;
import com.pontebella.ms_inventario.model.MovimientoStock;
import com.pontebella.ms_inventario.repository.MovimientoStockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientoStockService {

    private final MovimientoStockRepository movimientoRepository;
    private final InsumoService insumoService;

    // Núcleo de RF-IN-02. Se llama tanto desde el endpoint manual como,
    // más adelante, desde el listener de RabbitMQ que consuma
    // "cita.confirmada" (MS-Citas) o "matricula.creada" (MS-Academia).
    //
    // TODO: cuando se defina el contrato de eventos, si tras el descuento
    // el insumo queda con stockBajo=true, publicar "stock.bajo" por
    // RabbitMQ para que MS-Notificaciones alerte al administrador (RF-IN-03).
    public MovimientoStockResponseDTO descontar(DescuentoStockRequestDTO dto) {
        Insumo insumo = insumoService.buscarEntidad(dto.insumoId());

        if (insumo.getEstado() != Insumo.EstadoInsumo.ACTIVO) {
            throw new BusinessException("No se puede descontar stock de un insumo inactivo");
        }

        if (insumo.getStockActual() < dto.cantidad()) {
            throw new BusinessException(
                    "Stock insuficiente para '%s': disponible %d, solicitado %d"
                            .formatted(insumo.getNombre(), insumo.getStockActual(), dto.cantidad()));
        }

        insumo.setStockActual(insumo.getStockActual() - dto.cantidad());

        MovimientoStock movimiento = MovimientoStock.builder()
                .insumo(insumo)
                .tipo(MovimientoStock.TipoMovimiento.SALIDA)
                .cantidad(dto.cantidad())
                .motivo(dto.motivo())
                .referenciaId(dto.referenciaId())
                .build();

        MovimientoStock guardado = movimientoRepository.save(movimiento);
        // El save de insumo ocurre por dirty-checking de JPA al hacer commit
        // (misma transacción), pero lo guardamos explícito por claridad:
        insumoService.guardarSinValidar(insumo);

        return MovimientoStockResponseDTO.fromEntity(guardado);
    }

    // RF-IN-03 (reabastecimiento manual del administrador)
    public MovimientoStockResponseDTO registrarEntrada(EntradaStockRequestDTO dto) {
        Insumo insumo = insumoService.buscarEntidad(dto.insumoId());

        insumo.setStockActual(insumo.getStockActual() + dto.cantidad());

        MovimientoStock movimiento = MovimientoStock.builder()
                .insumo(insumo)
                .tipo(MovimientoStock.TipoMovimiento.ENTRADA)
                .cantidad(dto.cantidad())
                .motivo(MovimientoStock.MotivoMovimiento.REABASTECIMIENTO)
                .build();

        MovimientoStock guardado = movimientoRepository.save(movimiento);
        insumoService.guardarSinValidar(insumo);

        return MovimientoStockResponseDTO.fromEntity(guardado);
    }

    @Transactional(readOnly = true)
    public List<MovimientoStockResponseDTO> historialPorInsumo(UUID insumoId) {
        return movimientoRepository.findByInsumo_IdOrderByFechaDesc(insumoId).stream()
                .map(MovimientoStockResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimientoStockResponseDTO> historialPorReferencia(UUID referenciaId) {
        return movimientoRepository.findByReferenciaId(referenciaId).stream()
                .map(MovimientoStockResponseDTO::fromEntity)
                .toList();
    }
}