package com.tienda.dao;

import com.tienda.modelo.Reporte;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * RF-12: reporte de ventas diario (ventas brutas + ganancia neta) para el Administrador.
 */
public class ReporteDAO {

    public Reporte generarReporteDiario() {
        String sqlVentas = "SELECT COALESCE(SUM(total), 0) AS total_bruto FROM venta WHERE estado = 1 AND DATE(fecha_hora) = CURDATE()";
        String sqlGanancia = "SELECT COALESCE(SUM((dv.precio_unitario - p.precio_compra) * dv.cantidad), 0) AS ganancia "
                + "FROM detalle_venta dv "
                + "JOIN venta v ON v.id_venta = dv.id_venta "
                + "JOIN producto p ON p.id_producto = dv.id_producto "
                + "WHERE v.estado = 1 AND DATE(v.fecha_hora) = CURDATE()";
        String sqlInsert = "INSERT INTO reporte (fecha_generacion, total_ventas_brutas, ganancia_neta, tipo_reporte) VALUES (NOW(), ?, ?, ?)";

        Reporte reporte = new Reporte();
        reporte.setTipoReporte("DIARIO");

        try (Connection conn = Conexion.getConexion()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlVentas); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reporte.setTotalVentasBrutas(rs.getDouble("total_bruto"));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlGanancia); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reporte.setGananciaNeta(rs.getDouble("ganancia"));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, reporte.getTotalVentasBrutas());
                ps.setDouble(2, reporte.getGananciaNeta());
                ps.setString(3, reporte.getTipoReporte());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        reporte.setIdReporte(keys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte de ventas: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }

        return reporte;
    }
}
