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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfiguracionDAO {

    // Clave bajo la que se persiste la lista de tipos de producto del combo de
    // NuevoProductoDialog (un solo valor delimitado por '|'). NO se toca IVA_PORCENTAJE.
    private static final String CLAVE_TIPOS = "TIPOS_PRODUCTO";
    private static final String DELIMITADOR = "|";
    private static final String[] TIPOS_DEFAULT = {"Refrescos", "Frituras", "Lacteos", "Dulces"};

    // Devuelve los tipos persistidos, o los predeterminados si aún no se ha guardado ninguno.
    public List<String> getTiposProducto() {
        String sql = "SELECT valor FROM configuracion WHERE clave = ?";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, CLAVE_TIPOS);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String valor = rs.getString("valor");
                    if (valor != null && !valor.isBlank()) {
                        List<String> tipos = new ArrayList<>();
                        for (String tipo : valor.split("\\|")) {
                            if (!tipo.isBlank()) {
                                tipos.add(tipo.trim());
                            }
                        }
                        if (!tipos.isEmpty()) {
                            return tipos;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los tipos de producto: " + e.getMessage());
        }

        // Sin fila aún (o vacía): se devuelven los predeterminados sin persistir.
        return new ArrayList<>(Arrays.asList(TIPOS_DEFAULT));
    }

    // Agrega un tipo nuevo y persiste la lista completa (incluyendo los predeterminados
    // la primera vez). Devuelve false si ya existía o si falló el guardado.
    public boolean agregarTipoProducto(String nuevoTipo) {
        if (nuevoTipo == null || nuevoTipo.isBlank()) {
            return false;
        }
        String limpio = nuevoTipo.trim();

        List<String> tipos = getTiposProducto();
        for (String tipo : tipos) {
            if (tipo.equalsIgnoreCase(limpio)) {
                return false; // ya existe
            }
        }
        tipos.add(limpio);

        String valor = String.join(DELIMITADOR, tipos);
        // UPSERT: crea la fila TIPOS_PRODUCTO la primera vez o actualiza el valor después.
        String sql = "INSERT INTO configuracion (clave, valor) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE valor = VALUES(valor)";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, CLAVE_TIPOS);
            pstmt.setString(2, valor);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar el tipo de producto: " + e.getMessage());
            return false;
        }
    }

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
    public String SKU(String nombre, String marca, String tamano){
       String parteNombre="";
       String parteMarca="";
       String parteCantidad="";
       //limpiar espacios en blanco
       nombre=nombre.replaceAll("\\s+", "");
       marca=marca.replaceAll("\\s+", "");
       tamano=tamano.replaceAll("\\s+", "");
      
           parteNombre=obtenerSubcadena(nombre, 4);
           parteMarca=obtenerSubcadena(marca,3);
           parteCantidad=obtenerSubcadena(tamano,3);
      
       String sku=parteNombre+parteMarca+parteCantidad;
       return sku.toUpperCase();
       
   }
  
}

