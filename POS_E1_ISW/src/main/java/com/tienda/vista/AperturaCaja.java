/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tienda.vista;
import com.tienda.dao.TurnoCajaDAO;
import com.tienda.dao.UsuarioDAO;
import com.tienda.util.Sesion;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
public class AperturaCaja extends javax.swing.JPanel {
    UsuarioDAO usuarioDao = new UsuarioDAO();
    public AperturaCaja() {
        initComponents();
       // Asociar la tecla enter con envío de credenciales
        jTextField1.addActionListener(e -> jButton2ActionPerformed(null));
        
        jButton1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/backward.svg", 24, 24));
        jLabel1.putClientProperty("FlatLaf.style", "font: 56 'DearSans-Book'");
        jLabel1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/user.svg", (float) 6.0));
        // Centrar el contenido (icono + texto) horizontalmente
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // Mover el texto abajo del icono
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        // Icono de checkmark para enviar la contraseña
        jButton2.putClientProperty("JButton.buttonType", "roundRect");
        jButton2.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/check.svg", 24, 24));
        //placeholder
        jTextField1.putClientProperty("JTextField.placeholderText", "Ingrese efectivo en caja");
        // Saca el nombre de la sesión actual
        String cajeroActual = Sesion.getInstancia().getNombreUsuario();

        // Escribir el nombre de la sesión actual
        jLabel1.setText(cajeroActual);
        ((AbstractDocument) jTextField1.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nextText = currentText.substring(0, offset) + string + currentText.substring(offset);

                // ER para números a 2 decimales
                if (nextText.matches("^\\d*(\\.\\d{0,2})?$")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nextText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (nextText.matches("^\\d*(\\.\\d{0,2})?$")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
    

    @SuppressWarnings("unchecked")
 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button1 = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        button1.setLabel("button1");

        jScrollPane1.setViewportView(jTextPane1);

        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(500, 500));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cajero");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 510, 400));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 540, 240, -1));

        jButton1.setText("Atrás");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.addActionListener(this::jButton1ActionPerformed);
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 100, 40));

        jButton2.addActionListener(this::jButton2ActionPerformed);
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 540, 40, 40));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Obtener la info del frame principal
        MainFrame framePrincipal = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        if (framePrincipal != null) {
            // Instanciar el nuevo panel del Login de Administrador
            LoginPanel LoginPanel = new LoginPanel(); //Para cada ventana, cambiar su constructor

            // Mandar a hacer el intercambio
            framePrincipal.cambiarPanel(LoginPanel);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
