/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tienda.vista;
import com.tienda.dao.TurnoCajaDAO;
import javax.swing.JOptionPane;

public class AperturaCaja extends javax.swing.JPanel {

    public AperturaCaja() {
        initComponents();
       // jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    }

    @SuppressWarnings("unchecked")
 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button1 = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        efectivoEnCaja = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        atras = new javax.swing.JButton();

        button1.setLabel("button1");

        jScrollPane1.setViewportView(jTextPane1);

        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(500, 500));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cajero");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, -1, -1));

        efectivoEnCaja.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        efectivoEnCaja.setText("ingresar efectivo en caja");
        efectivoEnCaja.addActionListener(this::efectivoEnCajaActionPerformed);
        add(efectivoEnCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 200, 30));

        jButton1.setText("jButton1");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 260, 25, -1));

        atras.setText("Atrás");
        atras.addActionListener(this::atrasActionPerformed);
        add(atras, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void efectivoEnCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efectivoEnCajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efectivoEnCajaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
String textoMonto=efectivoEnCaja.getText().trim();
        if(textoMonto.isEmpty()){
            JOptionPane.showMessageDialog(this, "Ingresar el monto de efectivo para continuar.",
            "Campo requerido",JOptionPane.WARNING_MESSAGE);
            efectivoEnCaja.requestFocus();
            return;
        }
        try{
            double monto=Double.parseDouble(textoMonto);
            if(monto < 0 ){
                JOptionPane.showMessageDialog(this, "El monte inicial no puede ser negativo.",
                        "Monto inválido",JOptionPane.ERROR_MESSAGE);
                efectivoEnCaja.requestFocus();
                return;
            }
            TurnoCajaDAO turnoDAO=new TurnoCajaDAO();
            turnoDAO.registrarApertura(monto);
            efectivoEnCaja.setText("");
            
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Ingresar un monto válido",
            "Error de formato",JOptionPane.ERROR_MESSAGE);
            efectivoEnCaja.requestFocus();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void atrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atrasActionPerformed
        MainFrame framePrincipal=(MainFrame)javax.swing.SwingUtilities.getWindowAncestor(this);
        if (framePrincipal != null){
            CajeroLoginPanel panelCajero=new CajeroLoginPanel();
            framePrincipal.cambiarPanel(panelCajero);
        }
    }//GEN-LAST:event_atrasActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton atras;
    private java.awt.Button button1;
    private javax.swing.JTextField efectivoEnCaja;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
