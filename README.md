# Employee Management System (EMS)

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21-FF0000?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)
![MariaDB](https://img.shields.io/badge/Database-MariaDB-003545?logo=mariadb&logoColor=white)
![JDBC](https://img.shields.io/badge/API-JDBC-4479A1?logo=java&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white)

> [!NOTE]
> EMS is an academic desktop application for managing employee records. It is also a learning project for JavaFX, layered architecture, MVC, DAO, Repository, and JDBC.

> [!WARNING]
> This project is intended for learning and academic use. It is not recommended for production or serious business operations.

## Overview

EMS provides CRUD (create, read, update, delete) operations for employee records. The desktop interface is built with Java 21 and JavaFX 21. Employee data is persisted in a MariaDB database through JDBC.

## Technology

| Component | Technology |
| --- | --- |
| Language | Java 21 |
| User interface | JavaFX 21 (FXML and CSS) |
| Build tool | Apache Maven |
| Database | MariaDB |
| Database access | MariaDB JDBC driver |
| Tests | JUnit 5 |

## Architecture

The application separates the user interface, business rules, and persistence responsibilities. Each layer works through the layer below it, so the JavaFX view does not connect directly to the database.

```text
JavaFX view (FXML / CSS)
        │ user actions and displayed employee data
        ▼
EmpleadoController
        │ domain objects
        ▼
EmpleadoService
        │ validation and business rules
        ▼
EmpleadoRepository
        │ persistence operations
        ▼
EmpleadoDao interface ── implemented by ── EmpleadoDaoImpl
        │ SQL queries and result mapping
        ▼
JDBC (java.sql) ── MariaDB JDBC driver
        │ TCP/IP
        ▼
MariaDB database (ems_db)
```

### How the layers fit together

1. **View:** `empleado-view.fxml` defines the JavaFX screen, and `styles.css` contains its visual styling.
2. **Controller:** `EmpleadoController` handles UI events and coordinates the screen with the service.
3. **Service:** `EmpleadoService` holds the application's business rules and validation before persistence operations.
4. **Repository:** `EmpleadoRepository` provides the service with employee persistence operations while delegating database work to the DAO.
5. **DAO:** `EmpleadoDao` defines create, read, update, and delete operations. `EmpleadoDaoImpl` implements them with SQL and maps database results to `Empleado` objects. Lookups use `Optional<Empleado>` to represent an employee that may not exist.
6. **Database connection:** `DatabaseConnection` opens JDBC connections to MariaDB. `PersistenceException` wraps database errors for the application.

### Startup and dependency wiring

`MainApp` creates the DAO, repository, and service in order, then loads the FXML view. A controller factory supplies the service to `EmpleadoController` before the view initializes. This is manual dependency injection: each component receives the dependency it needs instead of constructing the next layer itself.

## Project structure

```text
src/main/java/cl/tasedev/ems/
├── app/              # JavaFX application entry point and startup wiring
├── config/           # Database connection setup
├── controller/       # JavaFX event handling
├── dao/              # Persistence contract
├── daoImplement/     # JDBC-backed DAO implementation
├── exception/        # Persistence exception
├── model/            # Employee domain model
├── repository/       # Persistence operations exposed to the service
└── service/          # Business rules and validation

src/main/resources/cl/tasedev/employeemanagementsystem/
├── css/              # JavaFX stylesheets
└── view/             # FXML screens
```

> The `repository/` package is alongside the other packages under `cl/tasedev/ems/`.

## Database setup

The application expects a MariaDB database named `ems_db`. Configure the connection URL, username, and password in `DatabaseConnection` to match your local database before running the application. The current connection settings are defined in source code; move credentials to environment-based configuration before using the project beyond local learning.

The project includes the MariaDB JDBC driver in `pom.xml`. It does not currently include a database schema or Docker Compose configuration, so the database and employee table must be created separately to match the queries in `EmpleadoDaoImpl`.

## Requirements

- JDK 21 or later
- MariaDB with the `ems_db` database and required employee table
- Maven (or the included Maven wrapper)

## Installation

See [INSTALL.md](INSTALL.md) for setup instructions, MariaDB configuration, and commands to run the application on Windows, macOS, or Linux.

## Run the application

From the project root, run:

```bash
./mvnw clean javafx:run
```

On Windows, use:

```bat
mvnw.cmd clean javafx:run
```

## Project status

EMS is a work in progress and an academic project. Its current persistence layer uses MariaDB through JDBC; the application requires a database instance configured to match the connection settings and SQL used by the project.
