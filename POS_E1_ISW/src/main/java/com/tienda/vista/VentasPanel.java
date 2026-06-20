/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tienda.vista;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VentasPanel extends javax.swing.JPanel {
private final double[] precios = {
        15.50, 22.00, 45.00, 28.50, 42.00,
        20.00, 35.00, 18.00, 14.50, 17.00
    };
    
    private double costoTotal = 0.0;
    private JPanel panelListaProductos;
    private Map<String, FilaProducto> productosAgregados = new HashMap<>();

    public VentasPanel() {
        initComponents();
        configurarPanel1();
        cargarBotonesDinamicos();
    }

    private void configurarPanel1() {
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setPreferredSize(new Dimension(160, 360));

        panelListaProductos = new JPanel();
        panelListaProductos.setLayout(new BoxLayout(panelListaProductos, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPanel1 = new JScrollPane(panelListaProductos);
        scrollPanel1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        jPanel1.add(scrollPanel1, BorderLayout.CENTER);
        jPanel1.add(BotonTotal, BorderLayout.SOUTH);

        actualizarBotonTotal();
    }

    private void cargarBotonesDinamicos() {
        String[] productos = {
            "Arroz", "Frijol", "Aceite", "Leche", "Huevo",
            "Azúcar", "Café", "Atún", "Jabón", "Refresco"
        };

        JPanel panelContenedor = new JPanel(new GridLayout(0, 4, 5, 5));

        for (int i = 0; i < productos.length; i++) {
            String nombreProducto = productos[i];
            double precioProducto = precios[i];

            JButton boton = new JButton(nombreProducto);
            Dimension dimensionBoton = new Dimension(90, 90);
            boton.setPreferredSize(dimensionBoton);
            boton.setMinimumSize(dimensionBoton);
            boton.setMaximumSize(dimensionBoton);
            boton.setFont(new Font("Arial", Font.BOLD, 11));
            boton.setMargin(new Insets(2, 2, 2, 2));

            boton.addActionListener(e -> agregarOIncrementarProducto(nombreProducto, precioProducto));

            panelContenedor.add(boton);
        }

        jScrollPane2.setViewportView(panelContenedor);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private void agregarOIncrementarProducto(String nombre, double precio) {
        if (productosAgregados.containsKey(nombre)) {
            FilaProducto fila = productosAgregados.get(nombre);
            int cantidadActual = (int) fila.spinnerCantidad.getValue();
            fila.spinnerCantidad.setValue(cantidadActual + 1);
        } else {
            FilaProducto nuevaFila = new FilaProducto(nombre, precio);
            productosAgregados.put(nombre, nuevaFila);
            panelListaProductos.add(nuevaFila);
            
            costoTotal += precio;
            actualizarBotonTotal();
        }
        
        panelListaProductos.revalidate();
        panelListaProductos.repaint();
    }

    private void actualizarBotonTotal() {
        BotonTotal.setText("TOTAL: $" + String.format("%.2f", costoTotal));
    }

    /**
     * Clase interna rediseñada para centrar elementos y agrandar el Spinner
     */
    private class FilaProducto extends JPanel {
        private JSpinner spinnerCantidad;
        private JLabel lblPrecio;
        private double precioUnitario;
        private String nombreProd;
        private int cantidadAnterior = 1;

        public FilaProducto(String nombre, double precio) {
            this.nombreProd = nombre;
            this.precioUnitario = precio;
            
            // Usamos GridBagLayout para centrar perfectamente todo el renglón
            this.setLayout(new GridBagLayout());
            this.setMaximumSize(new Dimension(160, 40)); 
            this.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2)); // Margen interno de la fila

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 3, 0, 3); // Espaciado horizontal entre componentes
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER; // Clave para centrar

            // 1. Label de Nombre
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
            gbc.gridx = 0;
            this.add(lblNombre, gbc);

            // 2. Configurar Spinner más grande
            SpinnerNumberModel modelo = new SpinnerNumberModel(1, 0, 99, 1);
            spinnerCantidad = new JSpinner(modelo);
            
            // Incrementamos dimensiones para que no se corten los números ni las flechas
            Dimension dimSpinner = new Dimension(55, 26);
            spinnerCantidad.setPreferredSize(dimSpinner);
            spinnerCantidad.setMinimumSize(dimSpinner);
            
            // Agrandar la fuente interna del número en el spinner
            JComponent editor = spinnerCantidad.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                JFormattedTextField txtField = ((JSpinner.DefaultEditor) editor).getTextField();
                txtField.setFont(new Font("Arial", Font.PLAIN, 12));
                txtField.setHorizontalAlignment(JTextField.CENTER); // Centra el número en su cajita
            }

            gbc.gridx = 1;
            this.add(spinnerCantidad, gbc);
            
            // 3. Label de Precio
            lblPrecio = new JLabel("$" + String.format("%.2f", precio));
            lblPrecio.setFont(new Font("Arial", Font.PLAIN, 11));
            gbc.gridx = 2;
            this.add(lblPrecio, gbc);

            // Listener del Spinner
            spinnerCantidad.addChangeListener(e -> {
                int nuevaCantidad = (int) spinnerCantidad.getValue();
                
                if (nuevaCantidad == 0) {
                    costoTotal -= precioUnitario;
                    panelListaProductos.remove(this);
                    productosAgregados.remove(nombreProd);
                    
                    panelListaProductos.revalidate();
                    panelListaProductos.repaint();
                } else {
                    int diferencia = nuevaCantidad - cantidadAnterior;
                    costoTotal += (diferencia * precioUnitario);
                    lblPrecio.setText("$" + String.format("%.2f", precioUnitario * nuevaCantidad));
                    cantidadAnterior = nuevaCantidad;
                }
                actualizarBotonTotal();
            });
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        BotonTotal = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("jButton1");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 30));

        jTextField1.setText("Buscar");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 810, 30));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, -1, -1));

        jPanel1.setBackground(new java.awt.Color(250, 247, 251));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        BotonTotal.setBackground(new java.awt.Color(71, 210, 165));
        BotonTotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BotonTotal.addActionListener(this::BotonTotalActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BotonTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(602, Short.MAX_VALUE)
                .addComponent(BotonTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 60, 160, 660));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane2.setViewportView(jPanel2);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 980, 660));

        jTextField2.setText("jTextField2");
        add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 50, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void BotonTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotonTotalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonTotal;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
