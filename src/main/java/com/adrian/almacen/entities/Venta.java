package com.adrian.almacen.entities;

import com.adrian.almacen.enums.EstadoVenta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta; // ← tipo correcto

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalleVenta;

    public List<DetalleVenta> getDetalleVenta() {
        if (this.detalleVenta == null)
            this.detalleVenta = new ArrayList<>();
        return this.detalleVenta;
    }

    public void agregarDetalle(DetalleVenta detalleVenta){
        if (detalleVenta == null)
            throw new IllegalArgumentException("El detalle es requerido");

        this.getDetalleVenta().add(detalleVenta);
        detalleVenta.setVenta(this);
    }

    public void cancel() {
        if (this.estadoVenta == EstadoVenta.CANCELADA)
            throw new IllegalArgumentException("La venta ya está cancelada");
        this.estadoVenta = EstadoVenta.CANCELADA;
    }

    public BigDecimal getTotal() {
        return getDetalleVenta().stream()
                .map(detalle -> detalle.getPrecioProducto().multiply(BigDecimal.valueOf(detalle.getCantidadProducto())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}