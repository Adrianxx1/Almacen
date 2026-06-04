package com.adrian.almacen.services;

import com.adrian.almacen.dto.productos.ProductoResponse;
import com.adrian.almacen.dto.sucursales.SucursalResponse;
import com.adrian.almacen.dto.ventas.DetalleVentaResponse;
import com.adrian.almacen.dto.ventas.VentaRequest;
import com.adrian.almacen.entities.Producto;
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
        return null;
    }
    @Override
    public void cancelar(Long id) {

    }

}