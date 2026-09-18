package com.pontebella.ms_inventario.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pontebella.ms_inventario.dto.InsumoRequestDTO;
import com.pontebella.ms_inventario.dto.InsumoResponseDTO;
import com.pontebella.ms_inventario.exception.ResourceNotFoundException;
import com.pontebella.ms_inventario.model.Insumo;
import com.pontebella.ms_inventario.repository.InsumoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoResponseDTO crear(InsumoRequestDTO dto) {
        Insumo insumo = Insumo.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .categoria(dto.categoria())
                .unidadMedida(dto.unidadMedida())
                .stockActual(dto.stockActual())
                .stockMinimo(dto.stockMinimo())
                .estado(dto.estado())
                .build();

        return InsumoResponseDTO.fromEntity(insumoRepository.save(insumo));
    }

    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> listar() {
        return insumoRepository.findAll().stream()
                .map(InsumoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> listarPorCategoria(Insumo.CategoriaInsumo categoria) {
        return insumoRepository.findByCategoria(categoria).stream()
                .map(InsumoResponseDTO::fromEntity)
                .toList();
    }

    // RF-IN-03: insumos que ya están por debajo de su umbral mínimo
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> listarConStockBajo() {
        return insumoRepository.findConStockBajoUmbral().stream()
                .map(InsumoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public InsumoResponseDTO obtenerPorId(UUID id) {
        return InsumoResponseDTO.fromEntity(buscarEntidad(id));
    }

    public InsumoResponseDTO actualizar(UUID id, InsumoRequestDTO dto) {
        Insumo insumo = buscarEntidad(id);
        insumo.setNombre(dto.nombre());
        insumo.setDescripcion(dto.descripcion());
        insumo.setCategoria(dto.categoria());
        insumo.setUnidadMedida(dto.unidadMedida());
        insumo.setStockMinimo(dto.stockMinimo());
        insumo.setEstado(dto.estado());
        // stockActual NO se edita aquí: solo cambia vía movimientos
        // (MovimientoStockService), para mantener la trazabilidad real.

        return InsumoResponseDTO.fromEntity(insumoRepository.save(insumo));
    }

    public void desactivar(UUID id) {
        Insumo insumo = buscarEntidad(id);
        insumo.setEstado(Insumo.EstadoInsumo.INACTIVO);
        insumoRepository.save(insumo);
    }

    protected Insumo buscarEntidad(UUID id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insumo no encontrado con id: " + id));
    }

    // Usado internamente por MovimientoStockService tras ajustar stockActual;
    // no pasa por las validaciones de InsumoRequestDTO porque no viene de un
    // formulario del usuario, sino de un movimiento ya validado.
    protected void guardarSinValidar(Insumo insumo) {
        insumoRepository.save(insumo);
    }
}