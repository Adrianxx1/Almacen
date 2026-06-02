package com.adrian.almacen.controllers;

import com.adrian.almacen.dto.sucursales.SucursalRequest;
import com.adrian.almacen.dto.sucursales.SucursalResponse;
import com.adrian.almacen.services.SucursalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
@Validated

public class SucursalController {

    private final SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalResponse>> listar(){
        return ResponseEntity.ok(sucursalService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse>
    obtenerPorId(@PathVariable @Positive(message = "EL ID DEBE SER POSITIVO") Long id){
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SucursalResponse> registrar(@RequestBody SucursalRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse>
    actualizar (@PathVariable @Positive(message = "EL ID DEBE SER POSITIVO") Long id, @Valid @RequestBody SucursalRequest request){
        return ResponseEntity.ok(sucursalService.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message = "EL ID DEBE SER POSITIVO") Long id) {
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
