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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


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
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        // Si el texto ya tiene 10 caracteres, consume el evento para no escribir el nuevo carácter
        if (jTextField1.getText().length() >= 10) {
            e.consume(); // Detiene la entrada de texto
        }
    }
});
        this.setOpaque(false);
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
        
        jLabel1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/user1.svg", (float) 1.0));
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
        this.setOpaque(false);
        
        // Contenedor superior para el buscador y el avatar del cajero
        JPanel panelCabecera = new JPanel(new BorderLayout());
        this.setOpaque(false);
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
    
    // MODIFICADO: Acción del botón usando ConfirmacionDialog
    btnRegresar.addActionListener(e -> {
        boolean confirmado = ConfirmacionDialog.confirmarAccionDestructiva(
                this, 
                "Cancelar Venta", 
                "¿Estás seguro de querer cancelar la compra actual y volver al inicio?", 
                "Sí, Salir"
        );
        
        // Si el usuario confirma, regresamos a la pantalla de ventas
        if (confirmado) {
            navegador.show(contenedorTarjetas, "PANTALLA_VENTAS");
        }
    });

    JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
    this.setOpaque(false);
    panelSuperior.add(btnRegresar);
    TOTALPanel.add(panelSuperior, BorderLayout.NORTH);
}

    private JPanel crearRenglonTicket(String etiqueta, double monto, int estiloFuente) {
        JPanel renglon = new JPanel(new BorderLayout());
        renglon.setBackground(Color.WHITE);

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Monospaced", estiloFuente, 13));

        JLabel lblMonto = new JLabel("$" + String.format("%.2f", monto));
        lblMonto.setFont(new Font("Monospaced", estiloFuente, 13));

        renglon.add(lblEtiqueta, BorderLayout.WEST);
        renglon.add(lblMonto, BorderLayout.EAST);
        return renglon;
    }

    private void construirDesgloseEnTOTALPanel() {
        BorderLayout layout = (BorderLayout) TOTALPanel.getLayout();
        Component centroAntiguo = layout.getLayoutComponent(BorderLayout.CENTER);
        if (centroAntiguo != null) {
            TOTALPanel.remove(centroAntiguo);
        }

        JPanel panelCentralContenedor = new JPanel();
        this.setOpaque(false);
        panelCentralContenedor.setLayout(new BoxLayout(panelCentralContenedor, BoxLayout.Y_AXIS));

        JPanel panelTicket = new JPanel();
        
        panelTicket.setLayout(new BoxLayout(panelTicket, BoxLayout.Y_AXIS));
        panelTicket.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        panelTicket.setBackground(Color.WHITE);

        for (FilaProducto fila : productosAgregados.values()) {
            double cantidad = ((Number) fila.spinnerCantidad.getValue()).doubleValue();
            double subtotal = fila.precioUnitario * cantidad;

            JPanel renglon = new JPanel(new BorderLayout());
            renglon.setBackground(Color.WHITE);

            String textoCantidad = (cantidad % 1 == 0) ? String.format("%.0f", cantidad) : String.format("%.3f", cantidad);
            
            String nombreTicket = fila.nombreProd.length() > 18 ? fila.nombreProd.substring(0, 17) + "…" : fila.nombreProd;
            JLabel lblProd = new JLabel(nombreTicket + " (x" + textoCantidad + ")");
            lblProd.setToolTipText(fila.nombreProd);
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

        // RNF-04: el total de la venta incluye el desglose del IVA leído desde configuración (RI-05)
        int ivaPorcentaje = new com.tienda.dao.ConfiguracionDAO().getIVA();
        double ivaMonto = costoTotal * ivaPorcentaje / 100.0;
        double totalConIva = costoTotal + ivaMonto;

        panelTicket.add(crearRenglonTicket("Subtotal:", costoTotal, Font.PLAIN));
        panelTicket.add(Box.createRigidArea(new Dimension(0, 3)));
        panelTicket.add(crearRenglonTicket("IVA (" + ivaPorcentaje + "%):", ivaMonto, Font.PLAIN));
        panelTicket.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTicket.add(crearRenglonTicket("Total:", totalConIva, Font.BOLD));

        JScrollPane scrollTicket = new JScrollPane(panelTicket);
        // Más alto y ancho para que el ticket no se vea apretado; sin scroll horizontal
        // (los renglones ya caben a lo ancho) y el vertical solo aparece si hace falta.
        scrollTicket.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTicket.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTicket.setPreferredSize(new Dimension(380, 280));
        scrollTicket.setMaximumSize(new Dimension(380, 280));
        scrollTicket.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelAccionPago = new JPanel();
        panelAccionPago.setLayout(new BoxLayout(panelAccionPago, BoxLayout.Y_AXIS));
        panelAccionPago.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelBotonesIniciales = new JPanel(new GridLayout(1, 2, 12, 0));
        panelBotonesIniciales.setMaximumSize(new Dimension(300, 42));

        JButton btnEfectivo = new JButton("Efectivo 💵");
        btnEfectivo.setFont(new Font("Arial", Font.BOLD, 12));
        btnEfectivo.setForeground(new Color(165,104,188));

        JButton btnTarjeta = new JButton("Tarjeta 💳");
        btnTarjeta.setFont(new Font("Arial", Font.BOLD, 12));
        btnTarjeta.setForeground(new Color(165,104,188));
        btnTarjeta.addActionListener(e -> finalizarTransaccion("Tarjeta", ivaPorcentaje, ivaMonto, totalConIva, totalConIva, 0));

        // Lógica de transición de cobro por Efectivo
        btnEfectivo.addActionListener(e -> {
            panelAccionPago.remove(panelBotonesIniciales);

            JPanel panelIngresoEfectivo = new JPanel(new BorderLayout(5, 0));
            panelIngresoEfectivo.setMaximumSize(new Dimension(300, 35));

            JTextField txtEfectivo = new JTextField();
            txtEfectivo.putClientProperty("JTextField.placeholderText", "Efectivo recibido");
            txtEfectivo.setFont(new Font("Arial", Font.PLAIN, 13));
            com.tienda.util.Sanitizador.limitarTexto(txtEfectivo, "^\\d*(\\.\\d{0,2})?$", 10);

            JButton btnCalcularCambio = new JButton("Aceptar");
            btnCalcularCambio.setFont(new Font("Arial", Font.BOLD, 12));
            btnCalcularCambio.setBackground(new Color(71,210,165));
            btnCalcularCambio.setForeground(new Color(255,255,255));

            panelIngresoEfectivo.add(new JLabel("Paga con: $"), BorderLayout.WEST);
            panelIngresoEfectivo.add(txtEfectivo, BorderLayout.CENTER);
            panelIngresoEfectivo.add(btnCalcularCambio, BorderLayout.EAST);

            panelAccionPago.add(panelIngresoEfectivo);

            btnCalcularCambio.addActionListener(evt -> {
                try {
                    double efectivo = Double.parseDouble(txtEfectivo.getText().trim());
                    if (efectivo < totalConIva) {
                        JOptionPane.showMessageDialog(TOTALPanel, "El efectivo ingresado es insuficiente.", "Dinero insuficiente", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    double cambio = efectivo - totalConIva;

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
                    btnFinalizarEfectivo.setForeground(new Color(255,255,255));
                    btnFinalizarEfectivo.setBackground(new Color(71,210,165));
                    btnFinalizarEfectivo.setAlignmentX(Component.CENTER_ALIGNMENT);
                    btnFinalizarEfectivo.addActionListener(ev -> finalizarTransaccion("Efectivo", ivaPorcentaje, ivaMonto, totalConIva, efectivo, cambio));

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

    private void finalizarTransaccion(String metodoPago, double ivaPorcentaje, double ivaMonto, double totalConIva, double montoRecibido, double cambio) {
        // 1. Armar y registrar la venta + su detalle en una sola transacción (RF-04, RF-07, RNF-07)
        com.tienda.modelo.Venta venta = new com.tienda.modelo.Venta();
        venta.setIdUsuario(com.tienda.util.Sesion.getInstancia().getIdCajero());
        venta.setIdTurno(com.tienda.util.Sesion.getInstancia().getIdTurno());
        venta.setTotalSinIva(costoTotal);
        venta.setIvaPorcentaje(ivaPorcentaje);
        venta.setIvaMonto(ivaMonto);
        venta.setTotal(totalConIva);
        venta.setMontoRecibido(montoRecibido);
        venta.setCambio(cambio);
        venta.setCompletada(true);

        for (FilaProducto fila : productosAgregados.values()) {
            double cantidadVendida = ((Number) fila.spinnerCantidad.getValue()).doubleValue();
            venta.getDetalles().add(new com.tienda.modelo.DetalleVenta(fila.idProducto, cantidadVendida, fila.precioUnitario));
        }

        com.tienda.dao.VentaDAO ventaDAO = new com.tienda.dao.VentaDAO();
        int idVenta = ventaDAO.registrarVenta(venta);

    com.tienda.dao.ProductoDAO prodDAO = new com.tienda.dao.ProductoDAO();
    boolean todoActualizado = true;

    // 2. Recorrer los productos del carrito y restar el stock en la BD
    for (FilaProducto fila : productosAgregados.values()) {
        String codigoBarras = fila.codigoBarras;
        double cantidadVendida = ((Number) fila.spinnerCantidad.getValue()).doubleValue();

        // Ejecutar la actualización en la base de datos
        boolean exito = prodDAO.restarInventario(codigoBarras, cantidadVendida);

        if (!exito) {
            todoActualizado = false; // Si uno falla, registramos el inconveniente
        }
    }

    if (idVenta > 0) {
        new com.tienda.dao.TurnoCajaDAO().sumarVenta(venta.getIdTurno(), totalConIva);
    }

    // 3. Mostrar mensaje de éxito según el resultado del inventario y del registro de la venta
    if (idVenta > 0 && todoActualizado) {
        JOptionPane.showMessageDialog(this, "Transacción completada y stock actualizado vía: " + metodoPago, "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
    } else if (idVenta <= 0) {
        JOptionPane.showMessageDialog(this, "No se pudo registrar la venta en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
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
            int idProductoCatalogo = prod.getIdProducto();

            JButton boton = new JButton("<html><center>" + nombreProducto + "</center></html>");
            Dimension dimensionBoton = new Dimension(90, 90);
            boton.setPreferredSize(dimensionBoton);
            boton.setMinimumSize(dimensionBoton);
            boton.setMaximumSize(dimensionBoton);
            boton.setFont(new Font("Arial", Font.BOLD, 10));
            boton.setMargin(new Insets(2, 2, 2, 2));

           // boton.addActionListener(e -> agregarOIncrementarProducto(nombreProducto, precioProducto, codigoBarrasProducto, idProductoCatalogo));
boton.addActionListener(e -> procesarSeleccionProductoCatalogo(prod));
            panelContenedor.add(boton);
        }

        jScrollPane2.setViewportView(panelContenedor);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

private void agregarOIncrementarProducto(String nombre, double precio, String codigoBarras, int idProducto) {
        if (productosAgregados.containsKey(nombre)) {
            FilaProducto fila = productosAgregados.get(nombre);
            // 1. Extraemos el valor de forma segura como double
            double cantidadActual = ((Number) fila.spinnerCantidad.getValue()).doubleValue();
            // 2. Sumamos 1.0 (en formato decimal)
            fila.spinnerCantidad.setValue(cantidadActual + 1.0);
        } else {
            // 3. Agregamos el 1.0 como parámetro de 'cantidadInicial' al constructor
            FilaProducto nuevaFila = new FilaProducto(nombre, precio, codigoBarras, idProducto, 1.0);
            productosAgregados.put(nombre, nuevaFila);
            panelListaProductos.add(nuevaFila);

            costoTotal += precio;
            actualizarBotonTotal();
        }

        panelListaProductos.revalidate();
        panelListaProductos.repaint();
    }

    private void agregarProductoAlTicket(Producto prod, double cantidad, double precioAplicado) {
        String nombre = prod.getNombre();

        if (productosAgregados.containsKey(nombre)) {
            // Si el producto ya está en el carrito, le sumamos la nueva cantidad
            FilaProducto fila = productosAgregados.get(nombre);
            // Usamos (Number) para evitar errores de casteo entre Integer y Double
            double valorActual = ((Number) fila.spinnerCantidad.getValue()).doubleValue();
            fila.spinnerCantidad.setValue(valorActual + cantidad);
        } else {
            // Si es nuevo, lo mandamos a la fila con su cantidad inicial y su precio aplicado
            FilaProducto nuevaFila = new FilaProducto(nombre, precioAplicado, prod.getCodigoBarras(), prod.getIdProducto(), cantidad);
            panelListaProductos.add(nuevaFila);
            productosAgregados.put(nombre, nuevaFila);

            // Sumamos al total (Cantidad x Precio)
            costoTotal += (precioAplicado * cantidad);
            actualizarBotonTotal();

            panelListaProductos.revalidate();
            panelListaProductos.repaint();
        }
    }

    private void actualizarBotonTotal() {
        BotonTotal.setText("TOTAL: $" + String.format("%.2f", costoTotal));
    }
    private void procesarSeleccionProductoCatalogo(Producto productoEncontrado) {
    if (productoEncontrado == null) return;

    // Variables finales que usará el flujo del ticket
    final double[] cantidadAAgregar = {1.0};
    final double[] precioAplicado = {productoEncontrado.getPrecioVenta()};

    // Validar si el producto seleccionado es a granel
    if (productoEncontrado.isEsGranel()) {
        // Bandera para saber si el usuario dio clic en aceptar
        final boolean[] aceptado = {false};

        // Crear ventana modal emergente
        JDialog dialogoGranel = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Venta a Granel", true);
        dialogoGranel.setLayout(new BorderLayout(10, 10));
        dialogoGranel.setSize(380, 240);
        dialogoGranel.setLocationRelativeTo(this);

        // Mensaje superior informativo
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblInstruccion = new JLabel("<html><center><b>" + productoEncontrado.getNombre() + "</b><br>Ingrese los gramos o seleccione una opción rápida:</center></html>");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 13));
        panelSuperior.add(lblInstruccion);
        dialogoGranel.add(panelSuperior, BorderLayout.NORTH);

        // Panel Central: Caja de texto + Botones de peso predefinido
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JTextField txtGramos = new JTextField();
        txtGramos.setFont(new Font("Arial", Font.BOLD, 14));
        txtGramos.putClientProperty("JTextField.placeholderText", "Ej. 500 para medio kilo");
        
        // Filtro para que el campo solo acepte números enteros
        txtGramos.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        panelCentral.add(txtGramos);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

        // Fila de botones rápidos (Asignamos los gramos correspondientes)
        JPanel panelBotonesRapidos = new JPanel(new GridLayout(1, 3, 8, 8));
        JButton btnUno = new JButton("1 Kg");
        JButton btnUnoMedia = new JButton("1.5 Kg");
        JButton btnDos = new JButton("2 Kg");

        btnUno.addActionListener(e -> txtGramos.setText("1000"));
        btnUnoMedia.addActionListener(e -> txtGramos.setText("1500"));
        btnDos.addActionListener(e -> txtGramos.setText("2000"));

        panelBotonesRapidos.add(btnUno);
        panelBotonesRapidos.add(btnUnoMedia);
        panelBotonesRapidos.add(btnDos);
        panelCentral.add(panelBotonesRapidos);
        
        dialogoGranel.add(panelCentral, BorderLayout.CENTER);

        // Panel Inferior: Botones de control de la ventana
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAceptar = new JButton("Aceptar");
        
        btnCancelar.addActionListener(e -> dialogoGranel.dispose());
        
        btnAceptar.addActionListener(e -> {
            String pesoTexto = txtGramos.getText().trim();
            if (pesoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoGranel, "Por favor, ingrese una cantidad.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!pesoTexto.matches("^\\d+$")) {
                JOptionPane.showMessageDialog(dialogoGranel, "Ingrese una cantidad válida en gramos.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double gramos = Double.parseDouble(pesoTexto);
            if (gramos <= 0) {
                JOptionPane.showMessageDialog(dialogoGranel, "La cantidad debe ser mayor a 0 gramos.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Realizar conversión a Kg para pasárselo al ticket
            cantidadAAgregar[0] = gramos / 1000.0;
            precioAplicado[0] = productoEncontrado.getPrecioPorKg();
            aceptado[0] = true;
            dialogoGranel.dispose();
        });

        panelInferior.add(btnCancelar);
        panelInferior.add(btnAceptar);
        dialogoGranel.add(panelInferior, BorderLayout.SOUTH);

        // Hacer visible la ventana modal
        dialogoGranel.setVisible(true);

        // Si el usuario cerró o canceló la ventana sin darle a Aceptar, detenemos el proceso
        if (!aceptado[0]) {
            return; 
        }
    }

    // Agregar el producto al ticket (funciona tanto para piezas unitarias como para peso a granel)
    agregarProductoAlTicket(productoEncontrado, cantidadAAgregar[0], precioAplicado[0]);
}

    private class FilaProducto extends JPanel {
        private JSpinner spinnerCantidad;
        private JLabel lblPrecio;
        private double precioUnitario;
        private String nombreProd;
        private double cantidadAnterior;
        public String codigoBarras;
        public int idProducto;


        public FilaProducto(String nombre, double precio, String codigoBarras, int idProducto, double cantidadInicial) {
            this.nombreProd = nombre;
            this.precioUnitario = precio;
            this.codigoBarras = codigoBarras;
            this.idProducto = idProducto;
            this.cantidadAnterior = cantidadInicial;

            this.setLayout(new GridBagLayout());
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            this.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 4, 0, 4);

            // Truncamos nombres largos: sin límite, el JLabel exige su ancho completo de texto
            // y GridBagLayout termina escondiendo el spinner y el precio en este renglón angosto.
            String nombreMostrado = nombre.length() > 18 ? nombre.substring(0, 17) + "…" : nombre;
            JLabel lblNombre = new JLabel(nombreMostrado);
            lblNombre.setToolTipText(nombre);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
            gbc.gridx = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;
            this.add(lblNombre, gbc);

            SpinnerNumberModel modelo = new SpinnerNumberModel(cantidadInicial, 0.0, 999.0, 1.0);
            spinnerCantidad = new JSpinner(modelo);
            Dimension dimSpinner = new Dimension(60, 26);
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

            lblPrecio = new JLabel("$" + String.format("%.2f", precio * cantidadInicial));
            lblPrecio.setFont(new Font("Arial", Font.PLAIN, 11));
            gbc.gridx = 2;
            gbc.weightx = 0.0; 
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST; 
            this.add(lblPrecio, gbc);

            spinnerCantidad.addChangeListener(e -> {
                double nuevaCantidad = ((Number) spinnerCantidad.getValue()).doubleValue();
                if (nuevaCantidad == 0) {
                    costoTotal -= (precioUnitario * cantidadAnterior);
                    panelListaProductos.remove(this);
                    productosAgregados.remove(nombreProd);
                    panelListaProductos.revalidate();
                    panelListaProductos.repaint();
                } else {
                    double diferencia = nuevaCantidad - cantidadAnterior;
                    costoTotal += (diferencia * precioUnitario);
                    lblPrecio.setText("$" + String.format("%.2f", precioUnitario * nuevaCantidad));
                    cantidadAnterior = nuevaCantidad;
                }
                actualizarBotonTotal();
            });
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // Convertimos el objeto Graphics a Graphics2D para acceder a funciones avanzadas de renderizado
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Habilitar Antialiasing para que la transición de colores se vea fluida y limpia
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Definir los colores usando tus códigos Hexadecimales
        Color colorInicio = Color.decode("#F0F2F4"); // Rosa arriba
        Color colorFin = Color.decode("#F0F2F4");    // Azul abajo
        
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
        setMinimumSize(new java.awt.Dimension(860, 640));
        setPreferredSize(new java.awt.Dimension(860, 640));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 640, 30));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, -1, -1));

        jPanel1.setBackground(new java.awt.Color(222, 237, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BotonTotal.setBackground(new java.awt.Color(71, 210, 165));
        BotonTotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BotonTotal.addActionListener(this::BotonTotalActionPerformed);
        jPanel1.add(BotonTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 578, 250, 70));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, 270, 660));

        jPanel2.setBackground(new java.awt.Color(158, 179, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane2.setViewportView(jPanel2);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 880, 660));

        jLabel1.setText("jLabel1");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
String skuEscaneado = jTextField1.getText().trim();

if (!skuEscaneado.isEmpty()) {
    // Buscar el producto en la BD
    ProductoDAO dao = new ProductoDAO();
    Producto productoEncontrado = dao.buscarPorSKUOBarra(skuEscaneado);

    if (productoEncontrado != null) {
        // Si existe, preparar variables base
        final double[] cantidadAAgregar = {1.0};
        final double[] precioAplicado = {productoEncontrado.getPrecioVenta()};
        final boolean[] continuarProceso = {true}; // Bandera de control por si cancela el diálogo

        // 2. Validar si es a granel
        if (productoEncontrado.isEsGranel()) {
            continuarProceso[0] = false; // Se detiene hasta que presione 'Aceptar'

            // Crear ventana modal emergente
            JDialog dialogoGranel = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Venta a Granel", true);
            dialogoGranel.setLayout(new BorderLayout(10, 10));
            dialogoGranel.setSize(380, 240);
            dialogoGranel.setLocationRelativeTo(this);

            // Mensaje superior informativo
            JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel lblInstruccion = new JLabel("<html><center><b>" + productoEncontrado.getNombre() + "</b><br>Ingrese los gramos o seleccione una opción rápida:</center></html>");
            lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 13));
            panelSuperior.add(lblInstruccion);
            dialogoGranel.add(panelSuperior, BorderLayout.NORTH);

            // Panel Central: Caja de texto + Botones rápidos
            JPanel panelCentral = new JPanel();
            panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
            panelCentral.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

            JTextField txtGramos = new JTextField();
            txtGramos.setFont(new Font("Arial", Font.BOLD, 14));
            txtGramos.putClientProperty("JTextField.placeholderText", "Ej. 500 para medio kilo");
            
            // Filtro para aceptar únicamente números enteros directos
            txtGramos.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c)) {
                        e.consume();
                    }
                }
            });
            panelCentral.add(txtGramos);
            panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

            // Fila de botones de peso rápido
            JPanel panelBotonesRapidos = new JPanel(new GridLayout(1, 3, 8, 8));
            JButton btnUno = new JButton("1 Kg");
            JButton btnUnoMedia = new JButton("1.5 Kg");
            JButton btnDos = new JButton("2 Kg");

            btnUno.addActionListener(e -> txtGramos.setText("1000"));
            btnUnoMedia.addActionListener(e -> txtGramos.setText("1500"));
            btnDos.addActionListener(e -> txtGramos.setText("2000"));

            panelBotonesRapidos.add(btnUno);
            panelBotonesRapidos.add(btnUnoMedia);
            panelBotonesRapidos.add(btnDos);
            panelCentral.add(panelBotonesRapidos);
            
            dialogoGranel.add(panelCentral, BorderLayout.CENTER);

            // Panel Inferior: Botones Aceptar / Cancelar
            JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            JButton btnCancelar = new JButton("Cancelar");
            JButton btnAceptar = new JButton("Aceptar");
            
            btnCancelar.addActionListener(e -> dialogoGranel.dispose());
            
            btnAceptar.addActionListener(e -> {
                String pesoTexto = txtGramos.getText().trim();
                if (pesoTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogoGranel, "Por favor, ingrese una cantidad.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!pesoTexto.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(dialogoGranel, "Ingrese una cantidad válida en gramos.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double gramos = Double.parseDouble(pesoTexto);
                if (gramos <= 0) {
                    JOptionPane.showMessageDialog(dialogoGranel, "La cantidad debe ser mayor a 0 gramos.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Conversión matemática a kilogramos decimales para el sistema
                cantidadAAgregar[0] = gramos / 1000.0;
                precioAplicado[0] = productoEncontrado.getPrecioPorKg();
                continuarProceso[0] = true; 
                dialogoGranel.dispose();
            });

            panelInferior.add(btnCancelar);
            panelInferior.add(btnAceptar);
            dialogoGranel.add(panelInferior, BorderLayout.SOUTH);

            // Abrir la ventana modal de forma síncrona
            dialogoGranel.setVisible(true);
        }

        // 3. Añadir al modelo si se validó correctamente o no era un producto a granel
        if (continuarProceso[0]) {
            agregarProductoAlTicket(productoEncontrado, cantidadAAgregar[0], precioAplicado[0]);
        }

    } else {
        // El producto no existe o está mal registrado
        JOptionPane.showMessageDialog(this, "Producto no encontrado: " + skuEscaneado, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    // PREPARACIÓN PARA EL SIGUIENTE ESCANEO (¡Crucial!)
    jTextField1.setText(""); 
    jTextField1.requestFocus(); 
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
