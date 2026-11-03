
package cl.tasedev.ems.daoImplement;

import cl.tasedev.ems.dao.EmpleadoDao;
import cl.tasedev.ems.exception.PersistenceException;
import cl.tasedev.ems.model.Empleado;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EmpleadoDaoImpl implements EmpleadoDao {

    // Ruta del archivo JSON
    private final Path RUTA_ARCHIVO =
            Paths.get("src/main/resources/json/empleados.json");

    private final ObjectMapper objectMapper;

    public EmpleadoDaoImpl() {

        // Configuración de Jackson
        this.objectMapper = new ObjectMapper();

        // Soporte para LocalDate
        this.objectMapper.registerModule(new JavaTimeModule());

        // Guarda las fechas como texto ISO-8601
        this.objectMapper.disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

        crearDirectorioYArchivo();
    }

    // Crea el directorio y el archivo JSON si no existen
    private void crearDirectorioYArchivo() {

        try {
            Path directorio = RUTA_ARCHIVO.getParent();

            if (directorio != null) {
                Files.createDirectories(directorio);
            }

            if (Files.notExists(RUTA_ARCHIVO)) {
                Files.writeString(
                        RUTA_ARCHIVO,
                        "[]",
                        StandardCharsets.UTF_8
                );
            }

        } catch (IOException e) {
            throw new PersistenceException(
                    "Error al crear el archivo de empleados.",
                    e
            );
        }
    }

    // Lee el listado completo de empleados
    private List<Empleado> leerListadoEmpleados() {

        if (Files.notExists(RUTA_ARCHIVO)) {
            crearDirectorioYArchivo();
        }

        try {
            if (Files.size(RUTA_ARCHIVO) == 0) {
                return new ArrayList<>();
            }

            List<Empleado> empleados = objectMapper.readValue(
                    RUTA_ARCHIVO.toFile(),
                    new TypeReference<List<Empleado>>() {}
            );

            return empleados != null
                    ? empleados
                    : new ArrayList<>();

        } catch (IOException e) {
            throw new PersistenceException(
                    "Error al leer el archivo JSON de empleados.",
                    e
            );
        }
    }

    // Escribe el listado completo de empleados
    private void escribirListadoEmpleados(List<Empleado> empleados) {

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(RUTA_ARCHIVO.toFile(), empleados);

        } catch (IOException e) {
            throw new PersistenceException(
                    "Error al guardar el listado de empleados.",
                    e
            );
        }
    }

    // CREATE
    @Override
    public void guardar(Empleado empleado) {

        List<Empleado> empleados = leerListadoEmpleados();

        boolean existe = empleados.stream()
                .anyMatch(e -> e.getId() == empleado.getId());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un empleado con ID: " + empleado.getId()
            );
        }

        empleados.add(empleado);

        escribirListadoEmpleados(empleados);
    }

    // READ: buscar por ID
    @Override
    public Optional<Empleado> buscarPorId(int id) {

        return leerListadoEmpleados()
                .stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }

    // READ: obtener todos los empleados
    @Override
    public List<Empleado> listarTodos() {

        return leerListadoEmpleados();
    }

    // UPDATE
    @Override
    public void actualizar(Empleado empleado) {

        List<Empleado> empleados = leerListadoEmpleados();

        for (int i = 0; i < empleados.size(); i++) {

            if (empleados.get(i).getId() == empleado.getId()) {

                empleados.set(i, empleado);

                escribirListadoEmpleados(empleados);
                return;
            }
        }

        throw new NoSuchElementException(
                "No existe un empleado con ID: " + empleado.getId()
        );
    }

    // DELETE
    @Override
    public void eliminar(int id) {

        List<Empleado> empleados = leerListadoEmpleados();

        boolean eliminado = empleados.removeIf(
                empleado -> empleado.getId() == id
        );

        if (!eliminado) {
            throw new NoSuchElementException(
                    "No existe un empleado con ID: " + id
            );
        }

        escribirListadoEmpleados(empleados);
    }
}
