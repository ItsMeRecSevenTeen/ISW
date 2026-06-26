/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rec17
 */
public class Venta {

    private int idVenta;
    private int idUsuario;
    private int idTurno;
    private LocalDateTime fechaHora;
    private double totalSinIva;
    private double ivaPorcentaje;
    private double ivaMonto;
    private double total;
    private double montoRecibido;
    private double cambio;
    private boolean completada;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {
    }

    public int getIdVenta() {
        return this.idVenta;
    }
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTurno() {
        return this.idTurno;
    }
    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public LocalDateTime getFechaHora() {
        return this.fechaHora;
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getTotalSinIva() {
        return this.totalSinIva;
    }
    public void setTotalSinIva(double totalSinIva) {
        this.totalSinIva = totalSinIva;
    }

    public double getIvaPorcentaje() {
        return this.ivaPorcentaje;
    }
    public void setIvaPorcentaje(double ivaPorcentaje) {
        this.ivaPorcentaje = ivaPorcentaje;
    }

    public double getIvaMonto() {
        return this.ivaMonto;
    }
    public void setIvaMonto(double ivaMonto) {
        this.ivaMonto = ivaMonto;
    }

    public double getTotal() {
        return this.total;
    }
    public void setTotal(double total) {
        this.total = total;
    }

    public double getMontoRecibido() {
        return this.montoRecibido;
    }
    public void setMontoRecibido(double montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    public double getCambio() {
        return this.cambio;
    }
    public void setCambio(double cambio) {
        this.cambio = cambio;
    }

    public boolean isCompletada() {
        return this.completada;
    }
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public List<DetalleVenta> getDetalles() {
        return this.detalles;
    }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
