/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

/**
 *
 * @author Rec17
 */
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    public int autenticar(String usuario, String password) {
        String sql = "SELECT rol FROM usuario WHERE nombre_usuario = ? AND contrasena_hash = SHA2(?, 256) AND activo = true";
        //try-with-resources para que Java cierre los recursos automáticamente
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
    public int registrarCajero(String usuario, String password){
        // Definir la query con placeholders (?) para evitar SQL Injection
        //rol 1 = cajero ------------------------------------------------------------------------------v
        //String sql = "INSERT INTO usuario (nombre_usuario, contrasena_hash, rol, activo) VALUES (?, ?, 1, 0)";
        String sql = "INSERT INTO usuario (nombre_usuario, contrasena_hash, rol, activo) VALUES (?, SHA2(?, 256), 1, 1)";
        
        // Usa try-with-resources para que Java cierre los recursos automáticamente
        try (Connection conn = Conexion.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, password);

            int filasAfectadas = ps.executeUpdate(); // Se ejecuta solo una vez
            return filasAfectadas > 0 ? 1 : 0;

        } catch (SQLException e) {
            // Capturar el código de error si es que surge uno
            if (e.getErrorCode() == 1062) {//Código de error cuando hay un usuario con el mismo nombre
                System.out.println("Intento de insertar usuario duplicado: " + usuario);
                return 1062; // Devolución del código de error
            }

            System.out.println("Error general en la DB: " + e.getMessage());
            return 0;
        }
    }
 
    public List<Object[]> obtenerUsuarios() {
        List<Object[]> listaUsuarios = new ArrayList<>();
        // Ajusta los nombres de las columnas 'id' según tu archivo DB_ISW.sql
        String sql = "SELECT id_usuario, nombre_usuario, contrasena_hash, rol FROM usuario";

        try (Connection conn = Conexion.getConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { // executeQuery es para consultas SELECT

            // El ciclo while recorre registro por registro de la DB
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("id_usuario");
                fila[1] = rs.getString("nombre_usuario");

                fila[2] = rs.getString("contrasena_hash");

                fila[3] = rs.getString("rol");
                listaUsuarios.add(fila);
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }

        return listaUsuarios;
    }
    
    public int actualizarPassword(int idUsuario, String nuevoPassword){
        String sql = "UPDATE usuario SET contrasena_hash = SHA2(?, 256) WHERE id_usuario = ?;";
        try(Connection conn = Conexion.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, nuevoPassword);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate();
        }
        catch(SQLException e){
            System.err.println("Error al actualizar la contraseña: " + e.getMessage());
            return 0; // Si hay error regresa 0 filas actualizadas
        }
    }
}
