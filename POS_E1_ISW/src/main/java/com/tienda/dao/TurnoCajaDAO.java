/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Rec17
 */
public class TurnoCajaDAO {
    public void registrarApertura(double monto, int idCajero){
       String sql= "INSERT INTO turno_caja(id_cajero, fondo_inicial, fecha_apertura, estaAbierta) VALUES(?, ?, NOW(), 1 )";
       try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
           pstmt.setInt(1, idCajero);
           pstmt.setDouble(2, monto);
           int filasAfectadas=pstmt.executeUpdate();
           if(filasAfectadas> 0){
               JOptionPane.showMessageDialog(null,"Apertura de caja registrada en la base de datos");
           }
       }catch (SQLException e){
               JOptionPane.showMessageDialog(null, "Error al registrar en la base de datos: "+e.getMessage(),
                       "Error SQL",JOptionPane.ERROR_MESSAGE);
               }catch (Exception e){
                       JOptionPane.showMessageDialog(null,"Error general: "+e.getMessage(),
                               "Error", JOptionPane.ERROR_MESSAGE);
                       }
    }
}
