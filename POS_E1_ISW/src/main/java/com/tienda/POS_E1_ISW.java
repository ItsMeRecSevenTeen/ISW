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
import javax.swing.UIManager;
/**
 *
 * @author Rec17
 */
public class POS_E1_ISW {

    public static void main(String[] args) throws SQLException {
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

        // Test de conexion a la db
        System.out.println("Intentando conectar a MySQL...");
        Connection cn = Conexion.getConexion();

        if (cn != null) {
            System.out.println("Conexion exitosa");
            try {
                cn.close();
            } catch (SQLException e) {
            } // Cerrar la prueba
        } else {
            System.out.println("Error critico, Revisa la conexion del Docker");
        }

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

        // Invocando a ventana main
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
