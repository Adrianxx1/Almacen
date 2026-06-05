package com.adrian.almacen.exceptions;

public class OperacionNoPermitida extends RuntimeException {
    public OperacionNoPermitida(String mensaje) {
        super(mensaje);
    }
}