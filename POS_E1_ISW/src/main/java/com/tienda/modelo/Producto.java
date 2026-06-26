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
    private int idProducto;
    private String nombre;
    private String sku;
    private boolean esGranel;
    private double precioPorKg;
    private double precioVenta;
    private double precioCompra;
    private double stockActual;
    private double stockMinimo;
    private String codigoBarras;
    private String contenidoNeto;
    private String marca;
    private boolean activo;

    // Constructor vacío obligatorio
    public Producto() {
    }
    // GETTERS Y SETTERS
    public int getIdProducto() {
        return this.idProducto;
    }
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return this.nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSku() {
        return this.sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isEsGranel() {
        return esGranel;
    }
    public void setEsGranel(boolean esGranel) {
        this.esGranel = esGranel;
    }

    public String getCodigoBarras() {
        return this.codigoBarras;
    }
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public double getPrecioPorKg() {
        return this.precioPorKg;
    }
    public void setPrecioPorKg(double precioPorKg) {
        this.precioPorKg = precioPorKg;
    }

    public double getPrecioVenta() {
        return this.precioVenta;
    }
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioCompra() {
        return this.precioCompra;
    }
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getStockActual() {
        return this.stockActual;
    }
    public void setStockActual(double stockActual) {
        this.stockActual = stockActual;
    }

    public double getStockMinimo() {
        return this.stockMinimo;
    }
    public void setStockMinimo(double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getContenidoNeto() {
        return this.contenidoNeto;
    }
    public void setContenidoNeto(String contenidoNeto) {
        this.contenidoNeto = contenidoNeto;
    }

    public String getMarca() {
        return this.marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }

    public boolean isActivo() {
        return this.activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
