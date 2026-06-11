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

public class UsuarioDAO {

    public int autenticar(String usuario, String password) {
        String sql = "SELECT rol FROM usuario WHERE nombre_usuario = ? AND contrasena_hash = SHA2(?, 256) AND activo = true";

        try (Connection conn = Conexion.getConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rol"); // Retorna 0 (Admin) o 1 (Cajero)
                }
                return -1; // Credenciales incorrectas o usuario inactivo
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.autenticar: " + e.getMessage());
            return -1; // En caso de error de BD, asumimos que no pasa
        }
    }
    public boolean registrarCajero(){
        String sql = "";
        return false;
    }
}
