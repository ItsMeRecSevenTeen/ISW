package com.tienda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Parámetros de MySQL para hacer la conexión
    private static final String DATABASE = "db_isw"; //Nombre de la base de datos, No de la conexión
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";//URL de conexión 
    private static final String USER = "root"; // Usuario de MySQL
    private static final String PASSWORD = "root_password_isw"; // Contraseña de la conexión

    // Método para la obtención de la conexión
    public static Connection getConexion() {
        Connection conectar = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el Driver de MySQL. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error: Falló la conexión a la base de datos. Verificar si el servidor está encendido. " + e.getMessage());
        }
        return conectar;
    }
}