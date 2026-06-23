/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import com.tienda.modelo.Producto;
/**
 *
 * @author Rec17
 */
public class ProductoDAO {

    public List<Producto> obtenerCatalogoVentas() {
        List<Producto> listaProductos = new ArrayList<>();
        // Traemos solo lo necesario y ordenamos alfabéticamente
        // Puedes agregar "WHERE stock_actual > 0" si no quieres mostrar los agotados
        String sql = "SELECT id_producto, sku, nombre, precio_venta, contenido_neto FROM producto ORDER BY nombre ASC";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Producto prod = new Producto();
                // 1. Extraemos las partes
            String nombreBase = rs.getString("nombre");
            String contenido = rs.getString("contenido_neto");
            
            // Armando el nombre único para el botón y para el ticket
            if (contenido != null && !contenido.isEmpty()) {
                prod.setNombre(nombreBase + " " + contenido); // Ejemplo: "Agua Bonafont 2Lt"
            } else {
                prod.setNombre(nombreBase);
            }
            
            prod.setPrecioVenta(rs.getDouble("precio_venta"));
            // Aquí va el setting de los atributos
            
            // Agregar a la lista
            listaProductos.add(prod);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar catálogo: " + e.getMessage());
        }
        return listaProductos;
    }
    public boolean guardarProducto(String sku, String nombre, double precioCompra, double precioVenta, double stockActual, double stockMinimo, String codigoBarras, String contenidoNeto, String marca) {

        // Añadimos 'codigo_barras' a la consulta SQL
        String sql = "INSERT INTO producto (sku, nombre, precio_compra, precio_venta, stock_actual, stock_minimo, codigo_barras, es_granel, contenido_neto, marca) VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, ?)";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sku);
            pstmt.setString(2, nombre);
            pstmt.setDouble(3, precioCompra);
            pstmt.setDouble(4, precioVenta);
            pstmt.setDouble(5, stockActual);
            pstmt.setDouble(6, stockMinimo);
            pstmt.setString(7, codigoBarras); 
            pstmt.setString(8, contenidoNeto);
            pstmt.setString(9, marca);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al insertar el producto: " + e.getMessage(),
                    "Error de Base de Datos", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public List<Object[]> obtenerProductos() {
        List<Object[]> listaProductos = new ArrayList<>();
        // Seleccionamos los campos necesarios de la tabla 'producto'
        String sql = "SELECT id_producto, nombre, sku, precio_venta, precio_compra, stock_actual, stock_minimo FROM producto";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Creamos la fila respetando el orden de las columnas del JTable
                Object[] fila = new Object[8]; 
                
                fila[0] = rs.getString("nombre");         // Columna Nombre
                fila[1] = rs.getString("sku");            // Columna SKU
                fila[2] = rs.getDouble("precio_venta");    // Columna Precio
                fila[3] = rs.getDouble("precio_compra");   // Columna Precio de compra
                fila[4] = "";                             // Columna Acciones (botones)
                fila[5] = rs.getDouble("stock_actual");    // Columna stock actual
                fila[6] = rs.getDouble("stock_minimo");    // Columna stock mínimo
                
                //Guardado del id_producto para cuando se necesite saber que producto editar o borrar al dar clic en los botones.
                fila[7] = rs.getInt("id_producto"); 

                listaProductos.add(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar los productos: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        
        return listaProductos;
    }

    public Producto buscarPorSKUOBarra(String criterio) {
        Producto prod = null;
        // Agregamos contenido_neto a la consulta SQL
        String sql = "SELECT nombre, precio_venta, contenido_neto FROM producto WHERE sku = ? OR codigo_barras = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, criterio);
            ps.setString(2, criterio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prod = new Producto();

                    // Combinacion de el Nombre con el Contenido Neto si no es null
                    String nombreBase = rs.getString("nombre");
                    String contenido = rs.getString("contenido_neto");
                    if (contenido != null && !contenido.isEmpty()) {
                        prod.setNombre(nombreBase + " " + contenido); 
                    } else {
                        prod.setNombre(nombreBase);
                    }

                    prod.setPrecioVenta(rs.getDouble("precio_venta"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return prod;
    }
}
