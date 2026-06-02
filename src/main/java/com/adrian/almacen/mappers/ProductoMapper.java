package com.adrian.almacen.mappers;
import com.adrian.almacen.enums.Categoria;
import com.adrian.almacen.dto.productos.ProductoRequest;
import com.adrian.almacen.dto.productos.ProductoResponse;
import com.adrian.almacen.entities.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public Producto requestAEntidad(ProductoRequest request, Categoria categoria) {
        if (request == null) return null;

        return Producto.builder()
                .nombre(request.nombre().trim())
                .categoria(categoria)
                .precio(request.precio())
                .cantidad(request.cantidad())
                .build();
    }

    public ProductoResponse entidadAResponse(Producto entidad) {
        if (entidad == null) return null;

        return new ProductoResponse(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getCategoria().getDescripcion(),
                entidad.getPrecio(),
                entidad.getCantidad()
        );
    }
}