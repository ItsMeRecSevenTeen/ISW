/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

/**
 *
 * @author Rec17
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionDAO {
    public boolean modificarIVA(int nuevoIva){
        // La query que modifica el valor existente
        String sql = "UPDATE configuracion SET valor = ? WHERE clave = 'IVA_PORCENTAJE'";
        
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Pasamos el entero como String a la BD
            pstmt.setString(1, String.valueOf(nuevoIva));
            
            // executeUpdate() devuelve cuántas filas se modificaron
            int filasAfectadas = pstmt.executeUpdate();
            
            return filasAfectadas > 0; // Retorna true si se actualizó con éxito
            
        } catch (SQLException e) {
            System.err.println("Error en ConfiguracionDAO.actualizarIva: " + e.getMessage());
            return false;
        }
    }
  
    public int getIVA() {
        int iva = 16; // Valor por default
        String sql = "SELECT valor FROM configuracion WHERE clave = 'IVA_PORCENTAJE'";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                // Se convierte en entero debido a que la columna valor almacena VARCHAR
                iva = Integer.parseInt(rs.getString("valor"));
            }

        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error al obtener el IVA en la DB: " + e.getMessage());
        }
        return iva;
    }
    private String obtenerSubcadena(String texto, int longitud) {
        // Si el texto está vacío, evitamos errores
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        // Si el texto es más corto que el límite, lo devuelve completo
        if (texto.length() < longitud) {
            return texto;
        }
        // Si cumple o es mayor, corta hasta la longitud deseada
        return texto.substring(0, longitud);
    }
    public String generarSKU(String nombre, String marca, String cantidad){
       String parteNombre="";
       String parteMarca="";
       String parteCantidad="";
       //limpiar espacios en blanco
       nombre=nombre.replaceAll("\\s+", "");
       marca=marca.replaceAll("\\s+", "");
       cantidad=cantidad.replaceAll("\\s+", "");
      
           parteNombre=obtenerSubcadena(nombre, 4);
           parteMarca=obtenerSubcadena(marca,3);
           parteCantidad=obtenerSubcadena(cantidad,3);
      
       String sku=parteNombre+parteMarca+parteCantidad;
       return sku.toUpperCase();
       
   }
  
}

