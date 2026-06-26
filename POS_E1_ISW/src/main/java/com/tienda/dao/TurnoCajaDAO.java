/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.modelo.TurnoCaja;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
/**
 *
 * @author Rec17
 */
public class TurnoCajaDAO {
    // Regresa el id_turno generado, o -1 si falló
    public int registrarApertura(double monto, int idCajero){
       String sql= "INSERT INTO turno_caja(id_cajero, fondo_inicial, fecha_apertura, estaAbierta) VALUES(?, ?, NOW(), 1 )";
       try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
           pstmt.setInt(1, idCajero);
           pstmt.setDouble(2, monto);
           int filasAfectadas=pstmt.executeUpdate();
           if(filasAfectadas> 0){
               JOptionPane.showMessageDialog(null,"Apertura de caja registrada en la base de datos");
               try (ResultSet keys = pstmt.getGeneratedKeys()) {
                   if (keys.next()) {
                       return keys.getInt(1);
                   }
               }
           }
           return -1;
       }catch (SQLException e){
               JOptionPane.showMessageDialog(null, "Error al registrar en la base de datos: "+e.getMessage(),
                       "Error SQL",JOptionPane.ERROR_MESSAGE);
               return -1;
               }catch (Exception e){
                       JOptionPane.showMessageDialog(null,"Error general: "+e.getMessage(),
                               "Error", JOptionPane.ERROR_MESSAGE);
                       return -1;
                       }
    }

    public void sumarVenta(int idTurno, double montoVenta) {
        String sql = "UPDATE turno_caja SET total_ventas = total_ventas + ? WHERE id_turno = ?";
        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, montoVenta);
            pstmt.setInt(2, idTurno);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al acumular venta en el turno: " + e.getMessage());
        }
    }

    public TurnoCaja obtenerTurno(int idTurno) {
        TurnoCaja turno = null;
        String sql = "SELECT id_turno, id_cajero, fecha_apertura, fecha_cierre, fondo_inicial, total_ventas, diferencia, estaAbierta "
                + "FROM turno_caja WHERE id_turno = ?";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTurno);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    turno = new TurnoCaja();
                    turno.setIdTurno(rs.getInt("id_turno"));
                    turno.setIdCajero(rs.getInt("id_cajero"));

                    Timestamp apertura = rs.getTimestamp("fecha_apertura");
                    turno.setFechaApertura(apertura != null ? apertura.toLocalDateTime() : null);

                    Timestamp cierre = rs.getTimestamp("fecha_cierre");
                    turno.setFechaCierre(cierre != null ? cierre.toLocalDateTime() : null);

                    turno.setFondoInicial(rs.getDouble("fondo_inicial"));
                    turno.setTotalVentas(rs.getDouble("total_ventas"));
                    turno.setDiferencia(rs.getDouble("diferencia"));
                    turno.setAbierta(rs.getBoolean("estaAbierta"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el turno de caja: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return turno;
    }

    // RF-11: cierra el turno calculando faltante/sobrante. diferencia > 0 = sobrante, < 0 = faltante.
    public boolean cerrarTurno(int idTurno, double efectivoContado) {
        TurnoCaja turno = obtenerTurno(idTurno);
        if (turno == null) {
            return false;
        }

        double totalEsperado = turno.getFondoInicial() + turno.getTotalVentas();
        double diferencia = efectivoContado - totalEsperado;

        String sql = "UPDATE turno_caja SET fecha_cierre = NOW(), diferencia = ?, estaAbierta = 0 WHERE id_turno = ?";
        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, diferencia);
            pstmt.setInt(2, idTurno);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar el turno de caja: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
