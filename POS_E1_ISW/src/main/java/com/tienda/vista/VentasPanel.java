/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tienda.vista;

import com.tienda.dao.ProductoDAO;
import com.tienda.modelo.Producto;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import com.tienda.util.Sesion;
import java.awt.Component;

public class VentasPanel extends javax.swing.JPanel {
    private double costoTotal = 0.0;
    private JPanel panelListaProductos;
    private Map<String, FilaProducto> productosAgregados = new HashMap<>();

    public VentasPanel() {
        initComponents();
        
        // Crea el menú flotante
        JPopupMenu menuUsuario = new JPopupMenu();

        //Crea la opción de cerrar sesión
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar caja");
        
        itemCerrarSesion.addActionListener(e -> {
            MainFrame framePrincipal = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            if (framePrincipal != null) {
                // Instanciar el nuevo panel del Login de Administrador
                CierreCaja cierreCaja = new CierreCaja(); //Para cada ventana, cambiar su constructor

                // Mandar a hacer el intercambio
                framePrincipal.cambiarPanel(cierreCaja);
            }
        });
        // Agregar el evento de clic al ícono de administrador
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // .show(componente_origen, coordenada_X, coordenada_Y)
                // X en 0 y Y justo al final de la altura del ícono para cuadrarlo bien
                menuUsuario.show(jLabel1, 0, jLabel1.getHeight());
            }
        });
        // Meter el ítem al menú
        menuUsuario.add(itemCerrarSesion);
        
        configurarPanel1();
        jLabel1.putClientProperty("FlatLaf.style", "font: 20 'DearSans-Book'");
        jLabel1.setText("<html><body style='margin-top: 2px;'>" + Sesion.getInstancia().getNombreUsuario() + "</body></html>");
        
        jLabel1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/user.svg", (float) 1.0));
        jTextField1.putClientProperty("JTextField.placeholderText", "Buscar por SKU o Código de Barras");
        cargarBotonesDinamicos();
    }
    
    
    private void configurarPanel1() {
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setPreferredSize(new Dimension(240, 360));

        panelListaProductos = new JPanel();
        panelListaProductos.setLayout(new BoxLayout(panelListaProductos, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPanel1 = new JScrollPane(panelListaProductos);
        scrollPanel1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        jPanel1.add(scrollPanel1, BorderLayout.CENTER);
        jPanel1.add(BotonTotal, BorderLayout.SOUTH);

        actualizarBotonTotal();
    }
    

    private void cargarBotonesDinamicos() {
        JPanel panelContenedor = new JPanel(new GridLayout(0, 4, 5, 5));

        // 1. Llamamos a la base de datos mediante el DAO
        com.tienda.dao.ProductoDAO prodDAO = new com.tienda.dao.ProductoDAO();
        java.util.List<com.tienda.modelo.Producto> catalogo = prodDAO.obtenerCatalogoVentas();

        // 2. Iteramos sobre los productos reales
        for (com.tienda.modelo.Producto prod : catalogo) {
            String nombreProducto = prod.getNombre();
            double precioProducto = prod.getPrecioVenta();

            // Diseño del botón
            // Usamos HTML simple para que el nombre se divida en dos líneas si es muy largo
            JButton boton = new JButton("<html><center>" + nombreProducto + "</center></html>");
            Dimension dimensionBoton = new Dimension(90, 90);
            boton.setPreferredSize(dimensionBoton);
            boton.setMinimumSize(dimensionBoton);
            boton.setMaximumSize(dimensionBoton);
            boton.setFont(new Font("Arial", Font.BOLD, 10)); // Bajé un poco la fuente por si hay nombres largos
            boton.setMargin(new Insets(2, 2, 2, 2));

            // Mantenemos tu lógica intacta para agregar al carrito
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

    private void agregarProductoAlTicket(Producto prod) {
    String nombre = prod.getNombre();

    // CASO A: El producto YA está en el mapa (containsKey sustituye a contains)
    if (productosAgregados.containsKey(nombre)) {
        // ¡Magia del Map! Obtenemos la fila directamente sin usar ningún ciclo FOR
        FilaProducto fila = productosAgregados.get(nombre);
        
        int valorActual = (int) fila.spinnerCantidad.getValue();
        fila.spinnerCantidad.setValue(valorActual + 1); 
        // Esto ya dispara el ChangeListener que recalcula el total solo
    } 
    // CASO B: Es la primera vez que se escanea el producto
    else {
        FilaProducto nuevaFila = new FilaProducto(nombre, prod.getPrecioVenta());
        panelListaProductos.add(nuevaFila);

        // Guardamos en el mapa: la llave es el 'nombre' y el valor es la 'nuevaFila'
        // (put sustituye a add)
        productosAgregados.put(nombre, nuevaFila);

        // Sumamos al costo total global
        costoTotal += prod.getPrecioVenta();
        actualizarBotonTotal();

        // Refrescamos la interfaz
        panelListaProductos.revalidate();
        panelListaProductos.repaint();
    }
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

            this.setLayout(new GridBagLayout());

            // SOLUCIÓN AL GAP (Rojo): Permitimos que la fila se expanda al ancho total disponible, manteniendo 42px de alto
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            this.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6)); // Margen interno de la fila

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 4, 0, 4); // Espaciado entre elementos internos

            // 1. Label de Nombre: Se queda con todo el espacio sobrante de la izquierda
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
            gbc.gridx = 0;
            gbc.weightx = 1.0; // <-- CLAVE: Absorbe todo el ancho libre y empuja al resto a la derecha
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST; // Alineado a la izquierda
            this.add(lblNombre, gbc);

            // Configurar Spinner
            SpinnerNumberModel modelo = new SpinnerNumberModel(1, 0, 99, 1);
            spinnerCantidad = new JSpinner(modelo);
            Dimension dimSpinner = new Dimension(52, 26);
            spinnerCantidad.setPreferredSize(dimSpinner);
            spinnerCantidad.setMinimumSize(dimSpinner);
            spinnerCantidad.setMaximumSize(dimSpinner);

            JComponent editor = spinnerCantidad.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                JFormattedTextField txtField = ((JSpinner.DefaultEditor) editor).getTextField();
                txtField.setFont(new Font("Arial", Font.PLAIN, 12));
                txtField.setHorizontalAlignment(JTextField.CENTER);
            }

            gbc.gridx = 1;
            gbc.weightx = 0.0; // Ancho fijo, no se deforma
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            this.add(spinnerCantidad, gbc);

            // Label de Precio SOLUCIÓN AL RECORTE
            lblPrecio = new JLabel("$" + String.format("%.2f", precio));
            lblPrecio.setFont(new Font("Arial", Font.PLAIN, 11));
            gbc.gridx = 2;
            gbc.weightx = 0.0; // Ancho fijo
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST; // Alineado perfectamente a la derecha
            this.add(lblPrecio, gbc);

            // El listener el Spinner se queda exactamente igual abajo...
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

        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        BotonTotal = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 640, 30));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, -1, -1));

        jPanel1.setBackground(new java.awt.Color(250, 247, 251));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BotonTotal.setBackground(new java.awt.Color(71, 210, 165));
        BotonTotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BotonTotal.addActionListener(this::BotonTotalActionPerformed);
        jPanel1.add(BotonTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 220, 48));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, 270, 660));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane2.setViewportView(jPanel2);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 880, 660));

        jLabel1.setText("jLabel1");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // Captura lo que el escáner acaba de leer
    String skuEscaneado = jTextField1.getText().trim();

    if (!skuEscaneado.isEmpty()) {
        // Buscar el producto en la BD
        ProductoDAO dao = new ProductoDAO();
        Producto productoEncontrado = dao.buscarPorSKUOBarra(skuEscaneado);

        if (productoEncontrado != null) {
            //  Si existe, mandarlo al panel
            agregarProductoAlTicket(productoEncontrado);
        } else {
            // El producto no existe o está mal registrado
            JOptionPane.showMessageDialog(this, "Producto no encontrado: " + skuEscaneado, "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        // PREPARACIÓN PARA EL SIGUIENTE ESCANEO (¡Crucial!)
        jTextField1.setText(""); // Limpiamos el texto
        jTextField1.requestFocus(); // Obligamos al cursor a quedarse parpadeando ahí
    }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void BotonTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotonTotalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
