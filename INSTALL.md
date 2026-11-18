# Instalación y ejecución

## Requisitos

- JDK 21 o superior. Comprueba la versión con `java -version`.
- Git, para clonar el repositorio.
- MariaDB accesible desde el ordenador donde se ejecutará la aplicación.
- Un entorno gráfico de escritorio para JavaFX.
- Conexión a Internet en la primera ejecución para descargar Maven y las dependencias.

No hace falta instalar Maven por separado: el proyecto incluye Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Preparar MariaDB

La aplicación espera una base de datos llamada `ems_db` y una tabla `empleados`. Si aún no existen, puedes crear la base y la tabla con este SQL:

```sql
CREATE DATABASE ems_db;

USE ems_db;

CREATE TABLE empleados (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefono VARCHAR(50),
    cargo VARCHAR(100),
    departamento VARCHAR(100),
    salario DOUBLE NOT NULL,
    fecha_contratacion DATE NOT NULL
);
```

Configura la URL, el usuario y la contraseña de tu servidor en `src/main/java/cl/tasedev/ems/config/DatabaseConnection.java`. La URL debe apuntar a la máquina y puerto donde MariaDB acepta conexiones, por ejemplo `jdbc:mariadb://localhost:3306/ems_db` si MariaDB está en el mismo ordenador. La configuración actual del proyecto apunta a una dirección de red local; cámbiala si tu servidor está en otra dirección.

## Descargar el proyecto

En Windows, PowerShell o Windows Terminal:

```powershell
git clone https://github.com/Tasesho/EmployeeManagementSystem.git
cd EmployeeManagementSystem
```

En macOS o Linux, abre una terminal:

```bash
git clone https://github.com/Tasesho/EmployeeManagementSystem.git
cd EmployeeManagementSystem
```

Si descargaste el proyecto como ZIP, descomprímelo y abre una terminal en la carpeta `EmployeeManagementSystem`.

## Ejecutar la aplicación

En Windows:

```powershell
.\mvnw.cmd clean javafx:run
```

En macOS o Linux:

```bash
chmod +x mvnw
./mvnw clean javafx:run
```

La aplicación se conectará a MariaDB usando la configuración de `DatabaseConnection.java`. El servidor debe estar encendido, aceptar conexiones desde este ordenador y tener creada la base de datos y la tabla indicadas arriba.
