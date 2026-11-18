package cl.tasedev.ems.service;

import cl.tasedev.ems.model.Empleado;
import cl.tasedev.ems.repository.EmpleadoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void guardar(Empleado empleado) {
        validarEmpleado(empleado);
        empleadoRepository.guardar(empleado);
    }

    public Optional<Empleado> buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return empleadoRepository.buscarPorId(id);
    }

    public List<Empleado> listarTodos() {
        return empleadoRepository.listarTodos();
    }

    public void actualizar(Empleado empleado) {
        if (empleado.getId() <= 0) {
            throw new IllegalArgumentException("El ID del empleado a actualizar no es válido.");
        }
        validarEmpleado(empleado);
        empleadoRepository.actualizar(empleado);
    }

    public void eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        empleadoRepository.eliminar(id);
    }

    private void validarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo.");
        }
        if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (empleado.getEmail() == null || !empleado.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email no es válido.");
        }
        if (empleado.getSalario() < 0) {
            throw new IllegalArgumentException("El salario no puede ser negativo.");
        }
        if (empleado.getFechaContratacion() == null) {
            empleado.setFechaContratacion(LocalDate.now());
        }
    }
}