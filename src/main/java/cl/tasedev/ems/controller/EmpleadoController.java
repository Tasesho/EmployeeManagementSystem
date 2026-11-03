package cl.tasedev.ems.controller;

import cl.tasedev.ems.service.EmpleadoService;
import javafx.fxml.FXML;

public class EmpleadoController {

    private EmpleadoService empleadoService;

    public void setEmpleadoService(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @FXML
    private void initialize() {
        // TODO: Inicializar componentes de la vista.
    }
}