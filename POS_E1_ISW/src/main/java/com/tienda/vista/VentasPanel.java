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

    // Variables de navegación de pantallas incorporadas
    private CardLayout navegador;
    private JPanel contenedorTarjetas;
    private JPanel TOTALPanel;
    
    public VentasPanel() {
        initComponents();
        configurarEstructuraCardLayout(); // Inicializa la navegación antes que los paneles internos
        
        // Crea el menú flotante
        JPopupMenu menuUsuario = new JPopupMenu();

        // Crea la opción de cerrar sesión
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar caja");
        
        itemCerrarSesion.addActionListener(e -> {
            MainFrame framePrincipal = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            if (framePrincipal != null) {
                CierreCaja cierreCaja = new CierreCaja();
                framePrincipal.cambiarPanel(cierreCaja);
            }
        });

        // Agregar el evento de clic al ícono de administrador
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuUsuario.show(jLabel1, 0, jLabel1.getHeight());
            }
        });

        menuUsuario.add(itemCerrarSesion);
        
        configurarPanel1();
        jLabel1.putClientProperty("FlatLaf.style", "font: 20 'DearSans-Book'");
        jLabel1.setText("<html><body style='margin-top: 2px;'>" + Sesion.getInstancia().getNombreUsuario() + "</body></html>");
        
        jLabel1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/user.svg", (float) 1.0));
        jTextField1.putClientProperty("JTextField.placeholderText", "Buscar por SKU o Código de Barras");
        cargarBotonesDinamicos();
        configurarAccionBotonTotal(); // Enlaza el evento click del botón total
    }
    
    /**
     * Envuelve los subpaneles en tarjetas intercambiables.
     */
    private void configurarEstructuraCardLayout() {
        navegador = new CardLayout();
        contenedorTarjetas = new JPanel(navegador);

        TOTALPanel = new JPanel();
        configurarTOTALPanelCheckout();

        // Envolvemos el área de venta superior (Buscador, catálogo y carrito lateral)
        JPanel vistaVentasMesa = new JPanel(new BorderLayout());
        
        // Contenedor superior para el buscador y el avatar del cajero
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.add(jTextField1, BorderLayout.CENTER);
        panelCabecera.add(jLabel1, BorderLayout.EAST);

        vistaVentasMesa.add(panelCabecera, BorderLayout.NORTH);
        vistaVentasMesa.add(jScrollPane2, BorderLayout.CENTER);
        vistaVentasMesa.add(jPanel1, BorderLayout.EAST);

        contenedorTarjetas.add(vistaVentasMesa, "PANTALLA_VENTAS");
        contenedorTarjetas.add(TOTALPanel, "PANTALLA_TOTAL");

        this.setLayout(new BorderLayout());
        this.add(contenedorTarjetas, BorderLayout.CENTER);
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

    private void configurarAccionBotonTotal() {
        BotonTotal.addActionListener(e -> {
            if (productosAgregados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El carrito de compras está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            construirDesgloseEnTOTALPanel();
            navegador.show(contenedorTarjetas, "PANTALLA_TOTAL");
        });
    }

    private void configurarTOTALPanelCheckout() {
        TOTALPanel.setLayout(new BorderLayout());
        TOTALPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnRegresar = new JButton("← Volver a Ventas");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegresar.addActionListener(e -> navegador.show(contenedorTarjetas, "PANTALLA_VENTAS"));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(btnRegresar);
        TOTALPanel.add(panelSuperior, BorderLayout.NORTH);
    }

    private void construirDesgloseEnTOTALPanel() {
        BorderLayout layout = (BorderLayout) TOTALPanel.getLayout();
        Component centroAntiguo = layout.getLayoutComponent(BorderLayout.CENTER);
        if (centroAntiguo != null) {
            TOTALPanel.remove(centroAntiguo);
        }

        JPanel panelCentralContenedor = new JPanel();
        panelCentralContenedor.setLayout(new BoxLayout(panelCentralContenedor, BoxLayout.Y_AXIS));

        JPanel panelTicket = new JPanel();
        panelTicket.setLayout(new BoxLayout(panelTicket, BoxLayout.Y_AXIS));
        panelTicket.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        panelTicket.setBackground(Color.WHITE);

        for (FilaProducto fila : productosAgregados.values()) {
            int cantidad = (int) fila.spinnerCantidad.getValue();
            double subtotal = fila.precioUnitario * cantidad;

            JPanel renglon = new JPanel(new BorderLayout());
            renglon.setBackground(Color.WHITE);

            JLabel lblProd = new JLabel(fila.nombreProd + " (x" + cantidad + ")");
            lblProd.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JLabel lblPrecio = new JLabel("$" + String.format("%.2f", subtotal));
            lblPrecio.setFont(new Font("Monospaced", Font.PLAIN, 13));

            renglon.add(lblProd, BorderLayout.WEST);
            renglon.add(lblPrecio, BorderLayout.EAST);
            panelTicket.add(renglon);
            panelTicket.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        JSeparator separador = new JSeparator();
        separador.setForeground(Color.DARK_GRAY);
        panelTicket.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTicket.add(separador);
        panelTicket.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel renglonTotal = new JPanel(new BorderLayout());
        renglonTotal.setBackground(Color.WHITE);
        
        JLabel lblTextoTotal = new JLabel("Total:");
        lblTextoTotal.setFont(new Font("Monospaced", Font.BOLD, 15));
        
        JLabel lblValorTotal = new JLabel("$" + String.format("%.2f", costoTotal));
        lblValorTotal.setFont(new Font("Monospaced", Font.BOLD, 15));

        renglonTotal.add(lblTextoTotal, BorderLayout.WEST);
        renglonTotal.add(lblValorTotal, BorderLayout.EAST);
        panelTicket.add(renglonTotal);

        JScrollPane scrollTicket = new JScrollPane(panelTicket);
        scrollTicket.setPreferredSize(new Dimension(300, 160));
        scrollTicket.setMaximumSize(new Dimension(300, 160));
        scrollTicket.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelAccionPago = new JPanel();
        panelAccionPago.setLayout(new BoxLayout(panelAccionPago, BoxLayout.Y_AXIS));
        panelAccionPago.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelBotonesIniciales = new JPanel(new GridLayout(1, 2, 12, 0));
        panelBotonesIniciales.setMaximumSize(new Dimension(300, 42));

        JButton btnEfectivo = new JButton("Efectivo 💵");
        btnEfectivo.setFont(new Font("Arial", Font.BOLD, 12));

        JButton btnTarjeta = new JButton("Tarjeta 💳");
        btnTarjeta.setFont(new Font("Arial", Font.BOLD, 12));
        btnTarjeta.addActionListener(e -> finalizarTransaccion("Tarjeta"));

        // Lógica de transición de cobro por Efectivo
        btnEfectivo.addActionListener(e -> {
            panelAccionPago.remove(panelBotonesIniciales);

            JPanel panelIngresoEfectivo = new JPanel(new BorderLayout(5, 0));
            panelIngresoEfectivo.setMaximumSize(new Dimension(300, 35));

            JTextField txtEfectivo = new JTextField();
            txtEfectivo.putClientProperty("JTextField.placeholderText", "Efectivo recibido");
            txtEfectivo.setFont(new Font("Arial", Font.PLAIN, 13));

            JButton btnCalcularCambio = new JButton("Aceptar");
            btnCalcularCambio.setFont(new Font("Arial", Font.BOLD, 12));

            panelIngresoEfectivo.add(new JLabel("Paga con: $"), BorderLayout.WEST);
            panelIngresoEfectivo.add(txtEfectivo, BorderLayout.CENTER);
            panelIngresoEfectivo.add(btnCalcularCambio, BorderLayout.EAST);

            panelAccionPago.add(panelIngresoEfectivo);

            btnCalcularCambio.addActionListener(evt -> {
                try {
                    double efectivo = Double.parseDouble(txtEfectivo.getText().trim());
                    if (efectivo < costoTotal) {
                        JOptionPane.showMessageDialog(TOTALPanel, "El efectivo ingresado es insuficiente.", "Dinero insuficiente", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    double cambio = efectivo - costoTotal;

                    txtEfectivo.setEditable(false);
                    btnCalcularCambio.setEnabled(false);

                    JPanel panelCambioResultado = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    JLabel lblCambio = new JLabel("SU CAMBIO: $" + String.format("%.2f", cambio));
                    lblCambio.setFont(new Font("Arial", Font.BOLD, 14));
                    lblCambio.setForeground(new Color(34, 139, 34));
                    panelCambioResultado.add(lblCambio);

                    JButton btnFinalizarEfectivo = new JButton("Finalizar Transacción ✓");
                    btnFinalizarEfectivo.setFont(new Font("Arial", Font.BOLD, 13));
                    btnFinalizarEfectivo.setMaximumSize(new Dimension(300, 40));
                    btnFinalizarEfectivo.setAlignmentX(Component.CENTER_ALIGNMENT);
                    btnFinalizarEfectivo.addActionListener(ev -> finalizarTransaccion("Efectivo"));

                    panelAccionPago.add(Box.createRigidArea(new Dimension(0, 10)));
                    panelAccionPago.add(panelCambioResultado);
                    panelAccionPago.add(Box.createRigidArea(new Dimension(0, 10)));
                    panelAccionPago.add(btnFinalizarEfectivo);

                    TOTALPanel.revalidate();
                    TOTALPanel.repaint();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TOTALPanel, "Por favor, introduzca un valor numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            });

            TOTALPanel.revalidate();
            TOTALPanel.repaint();
        });

        panelBotonesIniciales.add(btnEfectivo);
        panelBotonesIniciales.add(btnTarjeta);
        panelAccionPago.add(panelBotonesIniciales);

        panelCentralContenedor.add(Box.createVerticalGlue());
        panelCentralContenedor.add(scrollTicket);
        panelCentralContenedor.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentralContenedor.add(panelAccionPago);
        panelCentralContenedor.add(Box.createVerticalGlue());

        TOTALPanel.add(panelCentralContenedor, BorderLayout.CENTER);
        TOTALPanel.revalidate();
        TOTALPanel.repaint();
    }

    private void finalizarTransaccion(String metodoPago) {
        com.tienda.dao.ProductoDAO prodDAO = new com.tienda.dao.ProductoDAO();
    boolean todoActualizado = true;

    // 2. Recorrer los productos del carrito y restar el stock en la BD
    for (FilaProducto fila : productosAgregados.values()) {
        String codigoBarras = fila.codigoBarras;
        int cantidadVendida = (int) fila.spinnerCantidad.getValue();

        // Ejecutar la actualización en la base de datos
        boolean exito = prodDAO.restarInventario(codigoBarras, cantidadVendida);
        
        if (!exito) {
            todoActualizado = false; // Si uno falla, registramos el inconveniente
        }
    }

    // 3. Mostrar mensaje de éxito según el resultado del inventario
    if (todoActualizado) {
        JOptionPane.showMessageDialog(this, "Transacción completada y stock actualizado vía: " + metodoPago, "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Venta realizada, pero hubo un problema al actualizar algunos productos en el inventario.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    // 4. Limpiar la mesa de venta y regresar al catálogo
    productosAgregados.clear();
    panelListaProductos.removeAll();
    costoTotal = 0.0;
    actualizarBotonTotal();
    
    // 5. Refrescar los botones del catálogo para que muestren datos reales (Opcional)
    cargarBotonesDinamicos(); 
    
    navegador.show(contenedorTarjetas, "PANTALLA_VENTAS");
}

    private void cargarBotonesDinamicos() {
        JPanel panelContenedor = new JPanel(new GridLayout(0, 4, 5, 5));

        com.tienda.dao.ProductoDAO prodDAO = new com.tienda.dao.ProductoDAO();
        java.util.List<com.tienda.modelo.Producto> catalogo = prodDAO.obtenerCatalogoVentas();
        for (com.tienda.modelo.Producto prod : catalogo) {
            String nombreProducto = prod.getNombre();
            double precioProducto = prod.getPrecioVenta();
            String codigoBarrasProducto = prod.getCodigoBarras();

            JButton boton = new JButton("<html><center>" + nombreProducto + "</center></html>");
            Dimension dimensionBoton = new Dimension(90, 90);
            boton.setPreferredSize(dimensionBoton);
            boton.setMinimumSize(dimensionBoton);
            boton.setMaximumSize(dimensionBoton);
            boton.setFont(new Font("Arial", Font.BOLD, 10));
            boton.setMargin(new Insets(2, 2, 2, 2));

            boton.addActionListener(e -> agregarOIncrementarProducto(nombreProducto, precioProducto, codigoBarrasProducto));

            panelContenedor.add(boton);
        }

        jScrollPane2.setViewportView(panelContenedor);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private void agregarOIncrementarProducto(String nombre, double precio, String codigoBarras) {
        if (productosAgregados.containsKey(nombre)) {
            FilaProducto fila = productosAgregados.get(nombre);
            int cantidadActual = (int) fila.spinnerCantidad.getValue();
            fila.spinnerCantidad.setValue(cantidadActual + 1);
        } else {
            FilaProducto nuevaFila = new FilaProducto(nombre, precio,codigoBarras);
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

        if (productosAgregados.containsKey(nombre)) {
            FilaProducto fila = productosAgregados.get(nombre);
            int valorActual = (int) fila.spinnerCantidad.getValue();
            fila.spinnerCantidad.setValue(valorActual + 1); 
        } else {
            FilaProducto nuevaFila = new FilaProducto(nombre, prod.getPrecioVenta(), prod.getCodigoBarras());
            panelListaProductos.add(nuevaFila);
            productosAgregados.put(nombre, nuevaFila);

            costoTotal += prod.getPrecioVenta();
            actualizarBotonTotal();

            panelListaProductos.revalidate();
            panelListaProductos.repaint();
        }
    }

    private void actualizarBotonTotal() {
        BotonTotal.setText("TOTAL: $" + String.format("%.2f", costoTotal));
    }

    private class FilaProducto extends JPanel {
        private JSpinner spinnerCantidad;
        private JLabel lblPrecio;
        private double precioUnitario;
        private String nombreProd;
        private int cantidadAnterior = 1;
        public String codigoBarras;


        public FilaProducto(String nombre, double precio, String codigoBarras) {
            this.nombreProd = nombre;
            this.precioUnitario = precio;
            this.codigoBarras = codigoBarras;

            this.setLayout(new GridBagLayout());
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            this.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 4, 0, 4);

            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
            gbc.gridx = 0;
            gbc.weightx = 1.0; 
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST; 
            this.add(lblNombre, gbc);

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
            gbc.weightx = 0.0; 
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            this.add(spinnerCantidad, gbc);

            lblPrecio = new JLabel("$" + String.format("%.2f", precio));
            lblPrecio.setFont(new Font("Arial", Font.PLAIN, 11));
            gbc.gridx = 2;
            gbc.weightx = 0.0; 
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST; 
            this.add(lblPrecio, gbc);

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
