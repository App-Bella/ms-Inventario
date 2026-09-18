package com.pontebella.ms_inventario.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movimientos_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoMovimiento motivo;

    // Id de la cita o matrícula que originó el movimiento (trazabilidad).
    // Nulo en reabastecimientos o ajustes manuales.
    @Column(name = "referencia_id")
    private UUID referenciaId;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();

    public enum TipoMovimiento {
        ENTRADA, SALIDA
    }

    public enum MotivoMovimiento {
        CITA_CONFIRMADA, MATRICULA_KIT, REABASTECIMIENTO, AJUSTE_MANUAL
    }
}