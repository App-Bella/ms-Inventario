package com.pontebella.ms_inventario.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pontebella.ms_inventario.dto.DescuentoStockRequestDTO;
import com.pontebella.ms_inventario.dto.EntradaStockRequestDTO;
import com.pontebella.ms_inventario.dto.MovimientoStockResponseDTO;
import com.pontebella.ms_inventario.service.MovimientoStockService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoStockController {

    private final MovimientoStockService movimientoStockService;

    // RF-IN-02: descuento automatizado. Este mismo endpoint es el que,
    // más adelante, invocará el listener de RabbitMQ al recibir
    // "cita.confirmada" o "matricula.creada".
    @PostMapping("/descuento")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoStockResponseDTO descontar(@Valid @RequestBody DescuentoStockRequestDTO dto) {
        return movimientoStockService.descontar(dto);
    }

    // RF-IN-03: reabastecimiento manual por parte del administrador
    @PostMapping("/entrada")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoStockResponseDTO registrarEntrada(@Valid @RequestBody EntradaStockRequestDTO dto) {
        return movimientoStockService.registrarEntrada(dto);
    }

    @GetMapping("/insumo/{insumoId}")
    public List<MovimientoStockResponseDTO> historialPorInsumo(@PathVariable UUID insumoId) {
        return movimientoStockService.historialPorInsumo(insumoId);
    }

    // Trazabilidad: qué movimientos generó una cita o matrícula específica
    @GetMapping("/referencia/{referenciaId}")
    public List<MovimientoStockResponseDTO> historialPorReferencia(@PathVariable UUID referenciaId) {
        return movimientoStockService.historialPorReferencia(referenciaId);
    }
}