/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.modelo.DetalleVenta;
import com.tienda.modelo.Venta;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Rec17
 */
public class VentaDAO {

    // RNF-07: inserta la venta y su detalle en una sola transacción (todo o nada).
    // Regresa el id_venta generado, o -1 si la operación falló.
    public int registrarVenta(Venta venta) {
        String sqlVenta = "INSERT INTO venta (id_usuario, id_turno, total_sin_iva, iva_porcentaje, iva_monto, total, "
                + "monto_recibido, cambio, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion()) {
            conn.setAutoCommit(false);

            int idVenta;
            try (PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getIdUsuario());
                psVenta.setInt(2, venta.getIdTurno());
                psVenta.setDouble(3, venta.getTotalSinIva());
                psVenta.setDouble(4, venta.getIvaPorcentaje());
                psVenta.setDouble(5, venta.getIvaMonto());
                psVenta.setDouble(6, venta.getTotal());
                psVenta.setDouble(7, venta.getMontoRecibido());
                psVenta.setDouble(8, venta.getCambio());
                psVenta.setInt(9, venta.isCompletada() ? 1 : 0);
                psVenta.executeUpdate();

                try (ResultSet keys = psVenta.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return -1;
                    }
                    idVenta = keys.getInt(1);
                }
            }

            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    psDetalle.setInt(1, idVenta);
                    psDetalle.setInt(2, detalle.getIdProducto());
                    psDetalle.setDouble(3, detalle.getCantidad());
                    psDetalle.setDouble(4, detalle.getPrecioUnitario());
                    psDetalle.setDouble(5, detalle.getSubtotal());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }

            conn.commit();
            return idVenta;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la venta: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
}
