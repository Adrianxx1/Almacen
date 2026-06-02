package com.adrian.almacen.services;

import com.adrian.almacen.dto.productos.ProductoRequest;
import com.adrian.almacen.dto.productos.ProductoResponse;
import com.adrian.almacen.entities.Producto;
import com.adrian.almacen.enums.Categoria;
import com.adrian.almacen.exceptions.RecursoNoEncontrado;
import com.adrian.almacen.mappers.ProductoMapper;
import com.adrian.almacen.repositories.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        log.info("Listando todos los productos");
        return repository.findAll()
                .stream().map(mapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return mapper.entidadAResponse(obtenerProductoOException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto...");
        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim());
        Producto producto = mapper.requestAEntidad(request, categoria);
        Producto guardado = repository.save(producto);
        log.info("Nuevo producto {} registrado", guardado.getNombre());
        return mapper.entidadAResponse(guardado);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    private Producto obtenerProductoOException(Long id) {
        log.info("Buscando producto con id: {}", id);
        return repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontrado("Producto no encontrado con id: " + id));
    }
}