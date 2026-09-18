package com.pontebella.ms_inventario.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pontebella.ms_inventario.dto.InsumoRequestDTO;
import com.pontebella.ms_inventario.dto.InsumoResponseDTO;
import com.pontebella.ms_inventario.model.Insumo;
import com.pontebella.ms_inventario.service.InsumoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InsumoService insumoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsumoResponseDTO crear(@Valid @RequestBody InsumoRequestDTO dto) {
        return insumoService.crear(dto);
    }

    @GetMapping
    public List<InsumoResponseDTO> listar() {
        return insumoService.listar();
    }

    @GetMapping("/categoria/{categoria}")
    public List<InsumoResponseDTO> listarPorCategoria(@PathVariable Insumo.CategoriaInsumo categoria) {
        return insumoService.listarPorCategoria(categoria);
    }

    // RF-IN-03: panel de insumos que necesitan reabastecimiento
    @GetMapping("/stock-bajo")
    public List<InsumoResponseDTO> listarConStockBajo() {
        return insumoService.listarConStockBajo();
    }

    @GetMapping("/{id}")
    public InsumoResponseDTO obtenerPorId(@PathVariable UUID id) {
        return insumoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public InsumoResponseDTO actualizar(@PathVariable UUID id,
                                         @Valid @RequestBody InsumoRequestDTO dto) {
        return insumoService.actualizar(id, dto);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        insumoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}