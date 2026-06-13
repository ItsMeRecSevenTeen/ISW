/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.tienda.vista;
import javax.swing.JOptionPane;

public class NuevoProducto extends javax.swing.JInternalFrame {

    public NuevoProducto() {
        initComponents();
        this.setSize(400,400);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipoProducto = new javax.swing.JComboBox<>();
        tamano = new javax.swing.JTextField();
        marca = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        precioCompra = new javax.swing.JTextField();
        precio = new javax.swing.JTextField();
        stock = new javax.swing.JTextField();
        stockMinimo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        aceptar = new javax.swing.JButton();
        agregarTipo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setClosable(true);
        setResizable(true);
        setTitle("Dar de alta nuevo producto");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tipoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Refrescos", "Frituras", "Lacteos", "Harinas", "Dulces", "Alcohol" }));
        tipoProducto.addActionListener(this::tipoProductoActionPerformed);
        getContentPane().add(tipoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 44, 139, -1));

        tamano.setText("Tamaño");
        tamano.addActionListener(this::tamanoActionPerformed);
        getContentPane().add(tamano, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 171, 139, 31));

        marca.setText("Marca");
        marca.addActionListener(this::marcaActionPerformed);
        getContentPane().add(marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 128, 139, 31));

        nombre.setText("Nombre del Producto");
        nombre.addActionListener(this::nombreActionPerformed);
        getContentPane().add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 85, -1, 31));

        precioCompra.setText("Precio de Compra");
        precioCompra.addActionListener(this::precioCompraActionPerformed);
        getContentPane().add(precioCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 214, 139, 31));

        precio.setText("Precio");
        precio.addActionListener(this::precioActionPerformed);
        getContentPane().add(precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 85, 139, 31));

        stock.setText("Stock");
        stock.addActionListener(this::stockActionPerformed);
        getContentPane().add(stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 86, 139, 31));

        stockMinimo.setText("Stock Minimo");
        stockMinimo.addActionListener(this::stockMinimoActionPerformed);
        getContentPane().add(stockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 128, 139, 31));

        jLabel1.setFont(new java.awt.Font("Galvji", 1, 18)); // NOI18N
        jLabel1.setText("código de barras");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 225, -1, -1));

        jLabel2.setText("SKUGENERADO");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 199, 231, -1));

        jLabel3.setFont(new java.awt.Font("Galvji", 1, 18)); // NOI18N
        jLabel3.setText("SKU");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 171, -1, -1));

        aceptar.setText("ACEPTAR");
        aceptar.addActionListener(this::aceptarActionPerformed);
        getContentPane().add(aceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 276, -1, -1));

        agregarTipo.setText("Agregar otro tipo +");
        agregarTipo.addActionListener(this::agregarTipoActionPerformed);
        getContentPane().add(agregarTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 44, -1, -1));

        jLabel4.setText("Tipo de Producto");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(62, 21, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tipoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoProductoActionPerformed
    String productType=tipoProducto.getSelectedItem().toString();

    }//GEN-LAST:event_tipoProductoActionPerformed

    private void tamanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tamanoActionPerformed
    String Tamano=tamano.getText();        // TODO add your handling code here:
    }//GEN-LAST:event_tamanoActionPerformed

    private void marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marcaActionPerformed
    String Marca=marca.getText();        // TODO add your handling code here:
    }//GEN-LAST:event_marcaActionPerformed

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
        String NombreP=nombre.getText();
    }//GEN-LAST:event_nombreActionPerformed

    private void precioCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioCompraActionPerformed
    try{    String PrecioCompra=precioCompra.getText();   
    double preciodouble=Double.parseDouble(PrecioCompra);
    }catch (NumberFormatException e){
        JOptionPane.showMessageDialog(this, "ingresar un número valido");
    }
    }//GEN-LAST:event_precioCompraActionPerformed

    private void precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioActionPerformed
     try{    String Precio=precio.getText();   
    double preciodouble=Double.parseDouble(Precio);
    }catch (NumberFormatException e){
        JOptionPane.showMessageDialog(this, "ingresar un número valido");
    }
    }//GEN-LAST:event_precioActionPerformed

    private void stockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stockActionPerformed

    private void stockMinimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockMinimoActionPerformed
        try{    String Precio=precioCompra.getText();   
    double preciodouble2=Double.parseDouble(Precio);
    }catch (NumberFormatException e){
        JOptionPane.showMessageDialog(this, "ingresar un número valido");
    }
    }//GEN-LAST:event_stockMinimoActionPerformed

    private void agregarTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarTipoActionPerformed
        String tipoProd=JOptionPane.showInputDialog("ingresa un nuevo tipo de producto: ");
        tipoProducto.addItem(tipoProd);
       
    }//GEN-LAST:event_agregarTipoActionPerformed

    private void aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarActionPerformed
      /*  if(tipoProducto.getSelectedItem()==null||tamano.getText().trim().isEmpty()||stock.getText().trim().isEmpty()||stockMinimo.getText().trim().isEmpty()||precio.getText().trim().isEmpty()||precioCompra.getText().trim().isEmpty()||nombre.getText().trim().isEmpty()||marca.getText().trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(dialogoNuevoProducto,
                        "Los campos Nombre, Marca y Tamaño son requeridos para el SKU.", 
                        "Campos faltantes", javax.swing.JOptionPane.WARNING_MESSAGE);
        
        }
dialogoNuevoProducto.setVisible(true);*///falta guardar en base de datos (?    
    }//GEN-LAST:event_aceptarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptar;
    private javax.swing.JTextField agregarTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField marca;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField precio;
    private javax.swing.JTextField precioCompra;
    private javax.swing.JTextField stock;
    private javax.swing.JTextField stockMinimo;
    private javax.swing.JTextField tamano;
    private javax.swing.JComboBox<String> tipoProducto;
    // End of variables declaration//GEN-END:variables
}
