package com.cunina.backend.service;

import com.cunina.backend.entity.Tarifa;
import com.cunina.backend.repository.TarifaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<Tarifa> listarTodas() {
        return tarifaRepository.findAll();
    }

    public Tarifa guardar(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public Optional<Tarifa> obtenerPorEspecialidad(Long especialidadId) {
        return tarifaRepository.findByEspecialidad_IdEspecialidad(especialidadId);
    }

    public void eliminar(Long id) {
        tarifaRepository.deleteById(id);
    }
}