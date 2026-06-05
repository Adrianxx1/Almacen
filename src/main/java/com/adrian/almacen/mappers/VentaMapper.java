package com.adrian.almacen.mappers;

import com.adrian.almacen.dto.ventas.DetalleVentaResponse;
import com.adrian.almacen.dto.ventas.VentaRequest;
import com.adrian.almacen.dto.ventas.VentaResponse;
import com.adrian.almacen.entities.DetalleVenta;
import com.adrian.almacen.entities.Sucursal;
import com.adrian.almacen.entities.Venta;
import com.adrian.almacen.enums.EstadoVenta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class VentaMapper {
    private final SucursalMapper sucursalMapper;
    public Venta requestAEntidad(VentaRequest request, Sucursal sucursal){
        if (request == null) return null;

        return Venta.builder()
                .fecha(LocalDateTime.now())
                .estadoVenta(EstadoVenta.REGISTRADA)
                .sucursal(sucursal)
                .build();

    }

    public DetalleVentaResponse detalleAResponse (DetalleVenta detalle){
        if (detalle ==null) return null;

        return new DetalleVentaResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidadProducto(),
                detalle.getPrecioProducto(),
                detalle.calcularSubtotal()
        );
    }

    public VentaResponse entidadAResponse (Venta entidad){
        if (entidad == null) return null;
        List<DetalleVentaResponse> detalles = entidad.getDetalleVenta()
                .stream()
                .map(this::detalleAResponse)
                .toList();


        return new VentaResponse(
                entidad.getId(),
                entidad.getFecha().toString(),
                entidad.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(entidad.getSucursal()),
                detalles,
                entidad.getTotal()


        );

    }}

