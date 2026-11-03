package cl.tasedev.ems.dao;

import cl.tasedev.ems.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoDao {
    //CREATE
    void guardar(Empleado empleado);
    //READ
    Optional<Empleado> buscarPorId(int id);

    List<Empleado> listarTodos();
    //UPDATE
    void actualizar(Empleado empleado);
    //DELETE
    void eliminar(int id);
}


/* porque usar Optional? porque una busqueda puede simplemente no encontrar ningun empleado, Optional representa
* eso en vez que devuelva simplemente 'null'*/
