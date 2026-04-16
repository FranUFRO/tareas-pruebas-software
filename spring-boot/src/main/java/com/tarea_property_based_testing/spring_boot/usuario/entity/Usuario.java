package com.tarea_property_based_testing.spring_boot.usuario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "contraseña", nullable = false, unique = true, length = 225)
    private String contraseña;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String email, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}