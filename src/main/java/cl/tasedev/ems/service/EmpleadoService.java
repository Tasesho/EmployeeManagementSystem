package cl.tasedev.ems.service;

import cl.tasedev.ems.model.Empleado;
import cl.tasedev.ems.repository.EmpleadoRepository;

import java.util.List;
import java.util.Optional;

public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void guardar(Empleado empleado) {
        // TODO: Validar y guardar.
    }

    public Optional<Empleado> buscarPorId(int id) {
        return empleadoRepository.buscarPorId(id);
    }

    public List<Empleado> listarTodos() {
        return empleadoRepository.listarTodos();
    }

    public void actualizar(Empleado empleado) {
        // TODO: Validar y actualizar.
    }

    public void eliminar(int id) {
        // TODO: Comprobar y eliminar.
    }
}