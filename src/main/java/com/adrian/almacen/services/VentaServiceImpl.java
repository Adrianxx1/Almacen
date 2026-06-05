package com.adrian.almacen.services;


import com.adrian.almacen.dto.ventas.VentaRequest;
import com.adrian.almacen.entities.DetalleVenta;
import com.adrian.almacen.entities.Producto;
import com.adrian.almacen.entities.Sucursal;
import com.adrian.almacen.entities.Venta;
import com.adrian.almacen.enums.EstadoVenta;
import com.adrian.almacen.dto.ventas.VentaResponse;
import com.adrian.almacen.exceptions.RecursoNoEncontrado;
import com.adrian.almacen.mappers.VentaMapper;
import com.adrian.almacen.repositories.ProductoRepository;
import com.adrian.almacen.repositories.SucursalRepository;
import com.adrian.almacen.repositories.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;


    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        log.info("Listando ventas registradas");
        return ventaRepository.findAllByEstadoVenta(EstadoVenta.REGISTRADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        log.info("Listando ventas canceladas");
        return ventaRepository.findAllByEstadoVenta(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorId(Long id) {
        return ventaMapper.entidadAResponse(obtenerVentaOException(id));
    }
    private Venta obtenerVentaOException(Long id) {
        log.info("Buscando venta con id: {}", id);
        return ventaRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA).orElseThrow(() ->
                new RecursoNoEncontrado("venta no encontrado con id: " + id));
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
        log.info("Registrando nueva venta...");
        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontrado(
                        "Sucursal no encontrada con id: " + request.idSucursal()));

        Venta venta = ventaMapper.requestAEntidad(request, sucursal);

        request.productos().forEach(detalle -> {


            Producto producto = productoRepository.findById(detalle.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontrado(
                            "Producto no encontrado con id: " + detalle.idProducto()));


            producto.descontarCantidad(detalle.cantidadProducto());
            DetalleVenta detalleVenta = DetalleVenta.builder()
                    .producto(producto)
                    .cantidadProducto(detalle.cantidadProducto())
                    .precioProducto(producto.getPrecio())
                    .build();
            venta.agregarDetalle(detalleVenta);
        });
        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Venta registrada con id: {}", ventaGuardada.getId());
        return ventaMapper.entidadAResponse(ventaGuardada);
    }
    @Override
    public void cancelar(Long id) {
        Venta venta = obtenerVentaOException(id);
        log.info("Cancelando venta con id: {}", id);
        venta.cancel();
        venta.getDetalleVenta().forEach(detalle -> {
            detalle.getProducto().aumentarCantidad(detalle.getCantidadProducto());
        });
        log.info("Venta con id {} cancelada", id);
    }




    }

