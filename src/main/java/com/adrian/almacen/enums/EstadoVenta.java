package com.adrian.almacen.enums;

import com.adrian.almacen.exceptions.RecursoNoEncontrado;
import com.adrian.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoVenta {

    REGISTRADA("Registrada", 1L),
    CANCELADA("Cancelada", 0L);

    private final String descripcion;
    private final Long codigo;

    public static EstadoVenta obtenerCategoriaPorDescripcion(String descripcion) {
        StringCustomUtils.validarNoVacio(descripcion, "La dexcripcion es requerida");
        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion.trim());
        for (EstadoVenta estadoVenta : values()) {
            if (StringCustomUtils.quitarAcentos(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;

        }
        throw  new RecursoNoEncontrado("No existe una categoria con la descripcion:" + descripcion);
    }

    public static EstadoVenta obtenerEstadoVentaPorCodigo(Long codigo) {
        for (EstadoVenta estadoVenta : values()) {
            return estadoVenta;
        }

        throw new RecursoNoEncontrado("No existe un estado de venta con el codigo" +codigo);
    } }
