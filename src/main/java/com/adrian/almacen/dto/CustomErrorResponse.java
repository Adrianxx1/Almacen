package com.adrian.almacen.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
