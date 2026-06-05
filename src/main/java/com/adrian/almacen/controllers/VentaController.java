package com.adrian.almacen.controllers;


import com.adrian.almacen.dto.ventas.VentaRequest;
import com.adrian.almacen.dto.ventas.VentaResponse;
import com.adrian.almacen.services.VentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@AllArgsConstructor
@Validated
public class VentaController {


    private final VentaService ventaService;
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {

        return ResponseEntity.ok(ventaService.listar());
    }
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorId(
            @PathVariable @Positive(message = "EL ID DEBE SER POSITIVO") Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }
    @PostMapping
    public ResponseEntity<VentaResponse> registrar(
            @Valid @RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrar(request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message = "EL ID DEBE SER POSITIVO") Long id) {
        ventaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/canceladas")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }}