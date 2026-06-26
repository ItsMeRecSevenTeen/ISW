/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.modelo;

import java.time.LocalDateTime;

/**
 *
 * @author Rec17
 */
public class Reporte {

    private int idReporte;
    private LocalDateTime fechaGeneracion;
    private double totalVentasBrutas;
    private double gananciaNeta;
    private String tipoReporte;

    public Reporte() {
    }

    public int getIdReporte() {
        return this.idReporte;
    }
    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public LocalDateTime getFechaGeneracion() {
        return this.fechaGeneracion;
    }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public double getTotalVentasBrutas() {
        return this.totalVentasBrutas;
    }
    public void setTotalVentasBrutas(double totalVentasBrutas) {
        this.totalVentasBrutas = totalVentasBrutas;
    }

    public double getGananciaNeta() {
        return this.gananciaNeta;
    }
    public void setGananciaNeta(double gananciaNeta) {
        this.gananciaNeta = gananciaNeta;
    }

    public String getTipoReporte() {
        return this.tipoReporte;
    }
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
}
