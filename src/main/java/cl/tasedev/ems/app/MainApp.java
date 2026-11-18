package cl.tasedev.ems.app;

import cl.tasedev.ems.controller.EmpleadoController;
import cl.tasedev.ems.dao.EmpleadoDao;
import cl.tasedev.ems.daoImplement.EmpleadoDaoImpl;
import cl.tasedev.ems.repository.EmpleadoRepository;
import cl.tasedev.ems.service.EmpleadoService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Instanciar las capas de abajo hacia arriba
        EmpleadoDao empleadoDao = new EmpleadoDaoImpl();
        EmpleadoRepository empleadoRepository = new EmpleadoRepository(empleadoDao);
        EmpleadoService empleadoService = new EmpleadoService(empleadoRepository);

        // 2. Cargar el FXML (verifica que la ruta coincida con tu carpeta resources)
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/cl/tasedev/employeemanagementsystem/view/empleado-view.fxml")
        );

        // 3. Inyectar el servicio en el controlador ANTES de ejecutar initialize()
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == EmpleadoController.class) {
                return new EmpleadoController(empleadoService);
            }
            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(loader.load(), 1000, 650);

        stage.setTitle("Employee Management System");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}