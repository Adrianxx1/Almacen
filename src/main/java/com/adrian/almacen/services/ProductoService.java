package com.adrian.almacen.services;

import com.adrian.almacen.dto.productos.ProductoRequest;
import com.adrian.almacen.dto.productos.ProductoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    List<ProductoResponse> listar(String nombre, String categoria,
                                  BigDecimal precioMoni, BigDecimal precioMax);

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse registrar(ProductoRequest request);

    ProductoResponse actualizar(ProductoRequest request, Long id);

    void eliminar(Long id);
}