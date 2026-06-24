/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.modelo;

/**
 *
 * @author Rec17
 */
public class Producto {
    
    // Declaramos las variables privadas (Variables de la DB)
    private String nombre;
    private double precioVenta;
    private int stockActual;
    private int stockMinimo;
    
    // private String sku; private double stockActual;

    // Constructor vacío obligatorio
    public Producto() {
    }

    // GETTERS Y SETTERS 
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return this.precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    public double getStock(){
        return this.stockActual;
    }
    public void setStock(int stockActual){
        this.stockActual=stockActual;
    }
    public double getStockMinimo(){
        return this.stockMinimo;
    }
    public void setStockMinimo(int stockMinimo){
        this.stockMinimo=stockMinimo;
    }
    
}
