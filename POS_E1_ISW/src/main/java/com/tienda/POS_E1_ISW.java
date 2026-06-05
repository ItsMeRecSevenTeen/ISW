/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tienda;

import com.tienda.dao.Conexion;
import java.sql.Connection;
import com.formdev.flatlaf.FlatLightLaf; // Importante importar la librería
import com.tienda.vista.MainFrame;
/**
 *
 * @author Rec17
 */
public class POS_E1_ISW {

    public static void main(String[] args) {
        // Test de conexión instantáneo
        System.out.println("Intentando conectar a MySQL...");
        Connection cn = Conexion.getConexion();
        
        if (cn != null) {
            System.out.println("¡Conexión EXITOSA! El sistema está listo.");
            try { cn.close(); } catch (Exception e) {} // Cerramos la prueba
        } else {
            System.out.println("Error crítico: Revisa tus credenciales o enciende tu servidor.");
        }
        // 1. Activar el Look and Feel antes de abrir cualquier ventana
        FlatLightLaf.setup(); // Esto activa el modo claro moderno
        
        // Si prefieren el modo oscuro, cambien la línea de arriba por:
        com.formdev.flatlaf.FlatDarkLaf.setup();

        // 2. Aquí ya lanzan su primer panel o ventana
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true); // O la ventana que tengan armada
        });
    }
}
