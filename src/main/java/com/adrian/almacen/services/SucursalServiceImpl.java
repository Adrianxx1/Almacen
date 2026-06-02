package com.adrian.almacen.services;

import com.adrian.almacen.dto.sucursales.SucursalRequest;
import com.adrian.almacen.dto.sucursales.SucursalResponse;
import com.adrian.almacen.entities.Sucursal;
import com.adrian.almacen.exceptions.RecursoNoEncontrado;
import com.adrian.almacen.mappers.SucursalMapper;
import com.adrian.almacen.repositories.SucursalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@AllArgsConstructor
@Transactional
@Slf4j


public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private  final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
       log.info("Listado todas las sucursales");
       return sucursalRepository.findAll().stream()
               .map(sucursalMapper :: entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalOException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {
        log.info("Registrado nueva sucursal");

        validarDatosUnicos(request);
        Sucursal sucursal = sucursalMapper.requestAEntidad(request);
        sucursalRepository.save(sucursal);
        return null;
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);
        log.info("Actualizando sucursal con id:  {}", id);

        validarCambiosUnicos(request, id);

        sucursal.actualizar(request.nombre(), request.direccion());
        log.info("Sucursal con id {} actualizada correctamente ", id );
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);
        log.info("Eliminando sucursal con id: {}", id);

        sucursalRepository.delete(sucursal);
        log.info("Sucursal con id {}", id);

    }

    private Sucursal obtenerSucursalOException(Long id){
        log.info("Buscando sucursal con id: {}", id);
        return sucursalRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontrado("Sucursal no encontrada" + id));
    }

    private void validarDatosUnicos(SucursalRequest request) {
        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de :" + request.nombre());
    }
    private void validarCambiosUnicos(SucursalRequest request, Long id) {
        log.info("Validando cambio en nombre unico");
        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de :" + request.nombre());
    }
}
