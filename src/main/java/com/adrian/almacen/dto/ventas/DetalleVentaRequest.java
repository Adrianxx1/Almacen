package com.adrian.almacen.dto.ventas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleVentaRequest(
        @NotNull(message = "El ID del producto es requerido")
        @Positive(message = "El OD del prodcuto debe ser positivo" )
        Long idProducto,

        @NotNull(message = "La cantidad del produco es requerida")
        @Positive(message = "La cantidad del proyecto debe ser positiva")
        Integer cantidadProdcuto
) {}
