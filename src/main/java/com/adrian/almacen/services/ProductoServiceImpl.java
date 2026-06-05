package com.adrian.almacen.services;

import com.adrian.almacen.dto.productos.ProductoRequest;
import com.adrian.almacen.dto.productos.ProductoResponse;
import com.adrian.almacen.entities.Producto;
import com.adrian.almacen.enums.Categoria;
import com.adrian.almacen.enums.EstadoVenta;
import com.adrian.almacen.exceptions.OperacionNoPermitida;
import com.adrian.almacen.exceptions.RecursoNoEncontrado;
import com.adrian.almacen.mappers.ProductoMapper;
import com.adrian.almacen.repositories.ProductoRepository;
import com.adrian.almacen.repositories.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax
    ) {
        log.info("Listando todos los productos");
        return productoRepository.findAll().stream()
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoOException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto...");
        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim());
        Producto producto = productoMapper.requestAEntidad(request, categoria);
        Producto productoGuardado = productoRepository.save(producto);
        log.info("Nuevo producto {} registrado", productoGuardado.getNombre());
        return productoMapper.entidadAResponse(productoGuardado);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoOException(id);
        log.info("Actualizando producto con id: {}", id);
        producto.actualizar(
                request.nombre(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria()),
                request.precio(),
                request.cantidad()
        );
        log.info("Producto {} actualizado", producto.getNombre());
        return productoMapper.entidadAResponse(producto);
    }

    private Producto obtenerProductoOException(Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontrado("Producto no encontrado con id: " + id));
    }
    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoOException(id);
        if (ventaRepository.existsByDetalleVentaProductoIdAndEstadoVenta(id, EstadoVenta.REGISTRADA))
            throw new OperacionNoPermitida("No se puede eliminar el producto con id: " + id + " porque tiene ventas registradas");
        log.info("Eliminando producto con id: {}", id);
        productoRepository.delete(producto);
        log.info("Producto con id {} eliminado", id);
    }
}