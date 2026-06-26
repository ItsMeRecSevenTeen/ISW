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
        String sql = "SELECT id_producto, sku, nombre, precio_venta, contenido_neto, codigo_barras, es_granel, precio_por_kg FROM producto WHERE activo = 1 ORDER BY nombre ASC";

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

            prod.setIdProducto(rs.getInt("id_producto"));
            prod.setSku(rs.getString("sku"));
            prod.setPrecioVenta(rs.getDouble("precio_venta"));
            prod.setCodigoBarras(rs.getString("codigo_barras"));
            prod.setEsGranel(rs.getBoolean("es_granel"));
            prod.setPrecioPorKg(rs.getDouble("precio_por_kg"));
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
        String sql = "SELECT id_producto, nombre, sku, precio_venta, precio_compra, stock_actual, stock_minimo FROM producto WHERE activo = 1";

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
        // Agregamos contenido_neto, codigo_barras, es_granel y precio_por_kg: sin estos dos últimos
        // la venta a granel y el descuento de inventario no funcionan cuando el producto se busca
        // por escáner/SKU en vez de tomarlo del catálogo de botones.
        String sql = "SELECT id_producto, sku, nombre, precio_venta, contenido_neto, codigo_barras, es_granel, precio_por_kg "
                + "FROM producto WHERE (sku = ? OR codigo_barras = ?) AND activo = 1";

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

                    prod.setIdProducto(rs.getInt("id_producto"));
                    prod.setSku(rs.getString("sku"));
                    prod.setPrecioVenta(rs.getDouble("precio_venta"));
                    prod.setCodigoBarras(rs.getString("codigo_barras"));
                    prod.setEsGranel(rs.getBoolean("es_granel"));
                    prod.setPrecioPorKg(rs.getDouble("precio_por_kg"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return prod;
    }

    public Producto obtenerProductoPorId(int idProducto) {
        Producto prod = null;
        String sql = "SELECT id_producto, sku, nombre, marca, contenido_neto, precio_compra, precio_venta, "
                + "stock_actual, stock_minimo, codigo_barras, es_granel, precio_por_kg, activo "
                + "FROM producto WHERE id_producto = ?";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    prod = new Producto();
                    prod.setIdProducto(rs.getInt("id_producto"));
                    prod.setSku(rs.getString("sku"));
                    prod.setNombre(rs.getString("nombre"));
                    prod.setMarca(rs.getString("marca"));
                    prod.setContenidoNeto(rs.getString("contenido_neto"));
                    prod.setPrecioCompra(rs.getDouble("precio_compra"));
                    prod.setPrecioVenta(rs.getDouble("precio_venta"));
                    prod.setStockActual(rs.getDouble("stock_actual"));
                    prod.setStockMinimo(rs.getDouble("stock_minimo"));
                    prod.setCodigoBarras(rs.getString("codigo_barras"));
                    prod.setEsGranel(rs.getBoolean("es_granel"));
                    prod.setPrecioPorKg(rs.getDouble("precio_por_kg"));
                    prod.setActivo(rs.getBoolean("activo"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recuperar el producto: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return prod;
    }

    public boolean actualizarProducto(int idProducto, String sku, String nombre, double precioCompra, double precioVenta,
            double stockActual, double stockMinimo, String codigoBarras, String contenidoNeto, String marca) {

        String sql = "UPDATE producto SET sku = ?, nombre = ?, precio_compra = ?, precio_venta = ?, "
                + "stock_actual = ?, stock_minimo = ?, codigo_barras = ?, contenido_neto = ?, marca = ? "
                + "WHERE id_producto = ?";

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
            pstmt.setInt(10, idProducto);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage(),
                    "Error de Base de Datos", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean desactivarProducto(int idProducto) {
        String sql = "UPDATE producto SET activo = 0 WHERE id_producto = ?";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al dar de baja el producto: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
  public boolean restarInventario(String codigoBarras, double cantidadVendida) {
        String sql = "UPDATE producto SET stock_actual = stock_actual - ? WHERE codigo_barras = ?";

        try (Connection con = Conexion.getConexion()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDouble(1, cantidadVendida);
                ps.setString(2, codigoBarras);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas == 0) {
                    return false;
                }
            }

            registrarAlertaSiStockCritico(con, codigoBarras);
            return true; // Retorna true si se actualizó el producto

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar inventario del código" + codigoBarras + ": " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // RNF-06: deja trazabilidad en bitacora_logs cuando el stock queda en o por debajo del mínimo
    private void registrarAlertaSiStockCritico(Connection con, String codigoBarras) throws SQLException {
        String sqlConsulta = "SELECT id_producto, nombre, stock_actual, stock_minimo FROM producto WHERE codigo_barras = ?";

        try (PreparedStatement psConsulta = con.prepareStatement(sqlConsulta)) {
            psConsulta.setString(1, codigoBarras);

            try (ResultSet rs = psConsulta.executeQuery()) {
                if (!rs.next()) {
                    return;
                }
                double stockActual = rs.getDouble("stock_actual");
                double stockMinimo = rs.getDouble("stock_minimo");
                if (stockActual > stockMinimo) {
                    return;
                }

                String sqlBitacora = "INSERT INTO bitacora_logs (id_producto, tipo_evento, descripcion) VALUES (?, 'ALERTA_STOCK', ?)";
                try (PreparedStatement psLog = con.prepareStatement(sqlBitacora)) {
                    psLog.setInt(1, rs.getInt("id_producto"));
                    psLog.setString(2, "Stock crítico de '" + rs.getString("nombre") + "': quedaron "
                            + stockActual + " (mínimo " + stockMinimo + ")");
                    psLog.executeUpdate();
                }
            }
        }
    }
}
