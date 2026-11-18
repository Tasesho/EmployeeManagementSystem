package cl.tasedev.ems.config;

import cl.tasedev.ems.exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mariadb://192.168.1.9:3306/ems_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new PersistenceException("Error al conectar con MariaDB: " + e.getMessage(), e);
        }
    }
}