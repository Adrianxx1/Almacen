package com.adrian.almacen.repositories;


import com.adrian.almacen.entities.Venta;
import com.adrian.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findAllByEstadoVenta(EstadoVenta estado);

    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estado);

    boolean existsBySucursalIdAndEstadoVenta(Long idSucursal, EstadoVenta estado);

    boolean existsByDetalleVentaProductoIdAndEstadoVenta(Long idProducto, EstadoVenta estado);
}