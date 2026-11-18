package cl.tasedev.ems.daoImplement;

import cl.tasedev.ems.config.DatabaseConnection;
import cl.tasedev.ems.dao.EmpleadoDao;
import cl.tasedev.ems.exception.PersistenceException;
import cl.tasedev.ems.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoDaoImpl implements EmpleadoDao {

    @Override
    public void guardar(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, email, telefono, cargo, departamento, salario, fecha_contratacion) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getEmail());
            stmt.setString(3, empleado.getTelefono());
            stmt.setString(4, empleado.getCargo());
            stmt.setString(5, empleado.getDepartamento());
            stmt.setDouble(6, empleado.getSalario());
            stmt.setDate(7, Date.valueOf(empleado.getFechaContratacion()));

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenceException("No se pudo guardar el empleado, ninguna fila afectada.");
            }

            // Recuperar el ID autogenerado por MySQL y asignarlo al objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empleado.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar el empleado en MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Empleado> buscarPorId(int id) {
        String sql = "SELECT id, nombre, email, telefono, cargo, departamento, salario, fecha_contratacion FROM empleados WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSetAEmpleado(rs));
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar empleado por ID: " + id, e);
        }
    }

    @Override
    public List<Empleado> listarTodos() {
        String sql = "SELECT id, nombre, email, telefono, cargo, departamento, salario, fecha_contratacion FROM empleados ORDER BY id ASC";
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearResultSetAEmpleado(rs));
            }
            return empleados;

        } catch (SQLException e) {
            throw new PersistenceException("Error al listar todos los empleados", e);
        }
    }

    @Override
    public void actualizar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre = ?, email = ?, telefono = ?, cargo = ?, departamento = ?, salario = ?, fecha_contratacion = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getEmail());
            stmt.setString(3, empleado.getTelefono());
            stmt.setString(4, empleado.getCargo());
            stmt.setString(5, empleado.getDepartamento());
            stmt.setDouble(6, empleado.getSalario());
            stmt.setDate(7, Date.valueOf(empleado.getFechaContratacion()));
            stmt.setInt(8, empleado.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenceException("No se encontró el empleado con ID " + empleado.getId() + " para actualizar.");
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar el empleado con ID: " + empleado.getId(), e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenceException("No se encontró el empleado con ID " + id + " para eliminar.");
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar el empleado con ID: " + id, e);
        }
    }

    // Método auxiliar para transformar una fila de ResultSet en un objeto Empleado
    private Empleado mapearResultSetAEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("cargo"),
                rs.getString("departamento"),
                rs.getDouble("salario"),
                rs.getDate("fecha_contratacion").toLocalDate()
        );
    }
}