package com.pontebella.ms_inventario.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaInsumo categoria;

    // Ej: "unidad", "ml", "gr"
    @Column(name = "unidad_medida", nullable = false)
    private String unidadMedida;

    @Column(name = "stock_actual", nullable = false)
    @Builder.Default
    private Integer stockActual = 0;

    // RF-IN-03: si stockActual < stockMinimo, se dispara alerta de reabastecimiento
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInsumo estado;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimientoStock> movimientos;

    public enum CategoriaInsumo {
        INSUMO_CABINA, COSMETICO, KIT_ACADEMICO
    }

    public enum EstadoInsumo {
        ACTIVO, INACTIVO
    }
}