package cl.tasedev.ems.controller;

import cl.tasedev.ems.exception.PersistenceException;
import cl.tasedev.ems.model.Empleado;
import cl.tasedev.ems.service.EmpleadoService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    // Controles de búsqueda y tabla
    @FXML private TextField txtBuscar;
    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, Integer> colId;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colEmail;
    @FXML private TableColumn<Empleado, String> colTelefono;
    @FXML private TableColumn<Empleado, String> colCargo;
    @FXML private TableColumn<Empleado, String> colDepartamento;
    @FXML private TableColumn<Empleado, Double> colSalario;
    @FXML private TableColumn<Empleado, LocalDate> colFecha;

    // Controles del formulario
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCargo;
    @FXML private TextField txtDepartamento;
    @FXML private TextField txtSalario;
    @FXML private DatePicker dpFechaContratacion;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @FXML
    private void initialize() {
        configurarColumnas();
        configurarSeleccionTabla();
        configurarBuscador();
        dpFechaContratacion.setValue(LocalDate.now());
        cargarDatosDesdeBD();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colTelefono.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTelefono()));
        colCargo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCargo()));
        colDepartamento.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDepartamento()));
        colSalario.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getSalario()).asObject());
        colFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaContratacion()));
    }

    private void configurarSeleccionTabla() {
        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                txtId.setText(String.valueOf(seleccionado.getId()));
                txtNombre.setText(seleccionado.getNombre());
                txtEmail.setText(seleccionado.getEmail());
                txtTelefono.setText(seleccionado.getTelefono());
                txtCargo.setText(seleccionado.getCargo());
                txtDepartamento.setText(seleccionado.getDepartamento());
                txtSalario.setText(String.valueOf(seleccionado.getSalario()));
                dpFechaContratacion.setValue(seleccionado.getFechaContratacion());
            }
        });
    }

    private void configurarBuscador() {
        FilteredList<Empleado> datosFiltrados = new FilteredList<>(listaEmpleados, p -> true);

        txtBuscar.textProperty().addListener((obs, viejoValor, nuevoValor) -> {
            datosFiltrados.setPredicate(empleado -> {
                if (nuevoValor == null || nuevoValor.isBlank()) {
                    return true;
                }
                String filtro = nuevoValor.toLowerCase();
                return String.valueOf(empleado.getId()).contains(filtro)
                        || empleado.getNombre().toLowerCase().contains(filtro)
                        || empleado.getEmail().toLowerCase().contains(filtro)
                        || empleado.getCargo().toLowerCase().contains(filtro)
                        || empleado.getDepartamento().toLowerCase().contains(filtro);
            });
        });

        SortedList<Empleado> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tablaEmpleados.comparatorProperty());
        tablaEmpleados.setItems(datosOrdenados);
    }

    @FXML
    private void handleGuardar() {
        try {
            Empleado nuevo = construirEmpleadoDesdeFormulario(false);
            empleadoService.guardar(nuevo);
            cargarDatosDesdeBD();
            limpiarFormulario();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado registrado con ID: " + nuevo.getId());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato inválido", "El salario debe ser un número válido.");
        } catch (IllegalArgumentException | PersistenceException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void handleActualizar() {
        if (txtId.getText().isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona un empleado de la tabla para actualizar.");
            return;
        }
        try {
            Empleado actualizado = construirEmpleadoDesdeFormulario(true);
            empleadoService.actualizar(actualizado);
            cargarDatosDesdeBD();
            limpiarFormulario();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado actualizado correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dato inválido", "Verifica que el ID y el salario sean numéricos.");
        } catch (IllegalArgumentException | PersistenceException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al actualizar", e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        if (txtId.getText().isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona un empleado de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Eliminar permanentemente al empleado ID " + txtId.getText() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                int id = Integer.parseInt(txtId.getText());
                empleadoService.eliminar(id);
                cargarDatosDesdeBD();
                limpiarFormulario();
            } catch (IllegalArgumentException | PersistenceException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
            }
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    @FXML
    private void handleRecargar() {
        cargarDatosDesdeBD();
    }

    private Empleado construirEmpleadoDesdeFormulario(boolean incluirId) {
        int id = incluirId ? Integer.parseInt(txtId.getText().trim()) : 0;
        double salario = Double.parseDouble(txtSalario.getText().trim());
        LocalDate fecha = dpFechaContratacion.getValue();

        return new Empleado(
                id,
                txtNombre.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefono.getText().trim(),
                txtCargo.getText().trim(),
                txtDepartamento.getText().trim(),
                salario,
                fecha
        );
    }

    private void cargarDatosDesdeBD() {
        try {
            listaEmpleados.setAll(empleadoService.listarTodos());
        } catch (PersistenceException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtCargo.clear();
        txtDepartamento.clear();
        txtSalario.clear();
        dpFechaContratacion.setValue(LocalDate.now());
        tablaEmpleados.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}