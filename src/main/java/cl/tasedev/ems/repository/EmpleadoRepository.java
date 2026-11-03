package cl.tasedev.ems.repository;

import cl.tasedev.ems.dao.EmpleadoDao;
import cl.tasedev.ems.model.Empleado;

import java.util.List;
import java.util.Optional;

public class EmpleadoRepository {

    private final EmpleadoDao empleadoDAO;

    public EmpleadoRepository(EmpleadoDao empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    public void guardar(Empleado empleado) {
        empleadoDAO.guardar(empleado);
    }

    public Optional<Empleado> buscarPorId(int id) {
        return empleadoDAO.buscarPorId(id);
    }

    public List<Empleado> listarTodos() {
        return empleadoDAO.listarTodos();
    }

    public void actualizar(Empleado empleado) {
        empleadoDAO.actualizar(empleado);
    }

    public void eliminar(int id) {
        empleadoDAO.eliminar(id);
    }
}