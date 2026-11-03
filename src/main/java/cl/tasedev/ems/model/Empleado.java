package cl.tasedev.ems.model;

import java.time.LocalDate;

public class Empleado {

    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private String cargo;
    private String departamento;
    private double salario;
    private LocalDate fechaContratacion;

    //quize probar con la herramienta LocalDate de java, ya que te da un timestamp de cuando se crea el objeto, similar a SQL con date en una tabla.

    public Empleado(){
    }

    public Empleado(
            int id,
            String nombre,
            String email,
            String telefono,
            String cargo,
            String departamento,
            double salario,
            LocalDate fechaContratacion
    ) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.cargo = cargo;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id =id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", cargo='" + cargo + '\'' +
                ", departamento='" + departamento + '\'' +
                ", salario=" + salario +
                ", fechaContratacion=" + fechaContratacion +
                '}';
    }
}
