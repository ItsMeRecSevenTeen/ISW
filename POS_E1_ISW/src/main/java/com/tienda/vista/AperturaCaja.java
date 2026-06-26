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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
public class AperturaCaja extends javax.swing.JPanel {
    UsuarioDAO usuarioDao = new UsuarioDAO();
    public AperturaCaja() {
        initComponents();
       // Asociar la tecla enter con envío de credenciales
       this.setOpaque(false);
        jTextField1.addActionListener(e -> jButton2ActionPerformed(null));
        
        jButton1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/gback.svg", 24, 24));
        jLabel1.putClientProperty("FlatLaf.style", "font: 56 'Arial Rounded MT Bold'");
        jLabel1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/userblnco.svg", (float) 4.0));
        // Centrar el contenido (icono + texto) horizontalmente
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // Mover el texto abajo del icono
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setIconTextGap(1);
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
        jTextField1.requestFocus();
    }
    
@Override
    protected void paintComponent(Graphics g) {
        // Convertimos el objeto Graphics a Graphics2D para acceder a funciones avanzadas de renderizado
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Habilitar Antialiasing para que la transición de colores se vea fluida y limpia
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Definir los colores usando tus códigos Hexadecimales
        Color colorInicio = Color.decode("#1B22A6"); // Rosa arriba
        Color colorFin = Color.decode("#FFFCDE");    // Azul abajo
        
        // Crear el degradado vertical: (0, 0) es la esquina superior, (0, getHeight()) es el límite inferior
        GradientPaint degradadoVertical = new GradientPaint(
                0, 0, colorInicio, 
                0, getHeight(), colorFin
        );
        
        // Aplicar el lienzo de pintura y rellenar el rectángulo de este panel
        g2d.setPaint(degradadoVertical);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.dispose(); // Liberar los recursos gráficos inmediatamente
        super.paintComponent(g);
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

        setMinimumSize(new java.awt.Dimension(860, 640));
        setPreferredSize(new java.awt.Dimension(860, 640));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cajero");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 510, 400));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 540, 240, -1));

        jButton1.setBackground(new java.awt.Color(222, 237, 255));
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
    String textoMonto = jTextField1.getText().trim();
        
        // 2. Validar que el usuario haya ingresado un valor
        if (textoMonto.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Por favor, ingrese el monto de efectivo para realizar la apertura.", 
                    "Monto Requerido", 
                    JOptionPane.WARNING_MESSAGE);
            jTextField1.requestFocus();
            return;
        }
        
        try {
            // 3. Convertir el texto a un formato numérico válido (double)
            double monto = Double.parseDouble(textoMonto);
            
            // Validar que no ingresen valores negativos
            if (monto < 0) {
                JOptionPane.showMessageDialog(this, 
                        "El monto de apertura no puede ser un valor negativo.", 
                        "Monto Inválido", 
                        JOptionPane.ERROR_MESSAGE);
                jTextField1.requestFocus();
                return;
            }
            
            // Recuperars el ID del cajero desde la sesión global
            int idLogueado = com.tienda.util.Sesion.getInstancia().getIdCajero();
            System.out.println("DEBUG - ID del cajero enviado: " + idLogueado); //Depuración
            // Instanciado del DAO y se le pasa ambos datos
            TurnoCajaDAO turnoDAO = new TurnoCajaDAO();
            int idTurno = turnoDAO.registrarApertura(monto, idLogueado);
            com.tienda.util.Sesion.getInstancia().setIdTurno(idTurno);

            // Limpiar campo
            jTextField1.setText("");
            
            MainFrame framePrincipal = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            if (framePrincipal != null) {
                framePrincipal.cambiarPanel(new VentasPanel());
            }            
        } catch (NumberFormatException e) {
            // Se activa si el usuario introduce letras o caracteres especiales inválidos
            JOptionPane.showMessageDialog(this, 
                    "El monto ingresado no es válido. Use únicamente números y punto decimal.", 
                    "Error de Formato", 
                    JOptionPane.ERROR_MESSAGE);
            jTextField1.requestFocus();
        }
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
