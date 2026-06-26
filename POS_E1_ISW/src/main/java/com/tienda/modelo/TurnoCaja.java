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
public class TurnoCaja {

    private int idTurno;
    private int idCajero;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private double fondoInicial;
    private double totalVentas;
    private double diferencia;
    private boolean abierta;

    public TurnoCaja() {
    }

    public int getIdTurno() {
        return this.idTurno;
    }
    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public int getIdCajero() {
        return this.idCajero;
    }
    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public LocalDateTime getFechaApertura() {
        return this.fechaApertura;
    }
    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return this.fechaCierre;
    }
    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public double getFondoInicial() {
        return this.fondoInicial;
    }
    public void setFondoInicial(double fondoInicial) {
        this.fondoInicial = fondoInicial;
    }

    public double getTotalVentas() {
        return this.totalVentas;
    }
    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getDiferencia() {
        return this.diferencia;
    }
    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public boolean isAbierta() {
        return this.abierta;
    }
    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }
}
