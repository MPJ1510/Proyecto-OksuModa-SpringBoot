package com.proyecto.Oksumoda.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 15)
    private String telefono;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "contrasena", nullable = false, length = 100)  // ✅ SIN @Transient
    private String contrasena;

    @Column(length = 20)
    private String estado;

    // Constructor vacío
    public Cliente() {
        this.estado = "activo";
    }

    // Constructor con parámetros
    public Cliente(Long idCliente, String nombre, String telefono, String email, String contrasena,
             String estado) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}