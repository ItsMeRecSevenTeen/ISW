/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Rec17
 */
public class ProductoDAO {
    public boolean guardarProducto(String tipo, String nombre, String marca, String tamano, 
                                   double precioCompra, double precioVenta, int stock, int stockMinimo, String sku){
        String sql = "INSERT INTO productos (tipo, nombre, marca, tamano, precio_compra, precio_venta, stock, stock_minimo, sku) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con=Conexion.getConexion();
        PreparedStatement ps= con.prepareStatement(sql)){
           ps.setString(1, tipo);
            ps.setString(2, nombre);
            ps.setString(3, marca);
            ps.setString(4, tamano);
            ps.setDouble(5, precioCompra);
            ps.setDouble(6, precioVenta);
            ps.setInt(7, stock);
            ps.setInt(8, stockMinimo);
            ps.setString(9, sku);
         int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se guardó con éxito
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error SQL al guardar el producto: " + e.getMessage(), 
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error general: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;   
        }
    }
}
