/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.util;

/**
 *
 * @author Rec17
 */
public class Sesion {
    private static Sesion instancia;
    private int idCajero; // Para guardar el ID de la base de datos
    private String nombreUsuario;
    private int rol; // Por si se necesita que admin o cajero lo use

    // Constructor privado para que no usen el método afuera
    private Sesion() {}

    // Punto de acceso global
    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }
    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int idCajero) { this.idCajero = idCajero; }
    // Getters y Setters
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public int getRol() { return rol; }
    public void setRol(int rol) { this.rol = rol; }

    // Método para que cierre sesión
    public void cerrarSesion() {
        nombreUsuario = null;
        rol = 0;
    }
}