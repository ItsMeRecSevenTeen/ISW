/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tienda;

//import com.formdev.flatlaf.FlatLightLaf;
//import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import com.tienda.dao.Conexion;
import java.sql.Connection;
import com.tienda.vista.MainFrame;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
/**
 *
 * @author Rec17
 */
public class POS_E1_ISW {

    public static void main(String[] args) throws SQLException {
        //Modo claro para todos los frames
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception ex) {
            System.err.println("Error al iniciar FlatLaf");
        }
        // Modo oscuro para todos los frames
        //try {
        //    UIManager.setLookAndFeel( new FlatMacDarkLaf() );
        //} catch( Exception ex ) {
        //    System.err.println( "Error al iniciar FlatLaf" );
        //}
        
        // Carga de fuentes
        try {
            // La raíz es src -------------------------------------v
            InputStream is = POS_E1_ISW.class.getResourceAsStream("/fonts/DearSans-Book.ttf");

            if (is != null) {
                Font fuenteCustom = Font.createFont(Font.TRUETYPE_FONT, is);

                // Registrar en el sistema operativo virtual de Java
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(fuenteCustom);

                // Obtener el nombre exacto de la fuente
                System.out.println("El nombre real de la fuente es: " + fuenteCustom.getName());

            } else {
                System.err.println("ERROR: No se encontro el archivo TTF, Revisa que este dentro de src/main/resources/fonts/");
            }
        } catch (Exception e) {
            System.err.println("Error critico al registrar la fuente: " + e.getMessage());
        }

        try {
            // Intento de conexión a Docker
            Connection conn = Conexion.getConexion();
            
            if (conn != null && !conn.isClosed()) {
                conn.close(); // Cierre inmediato, solo para obtener info
            }

            // Invocando a ventana main
            java.awt.EventQueue.invokeLater(() -> {
                new MainFrame().setVisible(true);
            });

        } catch (SQLException e) {
            // Manejo de error cuando Docker no ejecuta
            System.err.println("Base de datos no disponible: " + e.getMessage());

            java.awt.EventQueue.invokeLater(() -> {
                // Abriendo ventana de error
                JOptionPane.showMessageDialog(new JOptionPane(), "No se pudo conectar a la base de datos, Revisa la conexión", "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
    }
}
