/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tienda.vista;

import com.tienda.dao.ConfiguracionDAO;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import com.tienda.dao.ProductoDAO;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;


/**
 *
 * @author Rec17
 */
public class InventarioPanel extends javax.swing.JPanel {

    // id_producto de cada fila, en el mismo orden que las filas de jTable1.
    // DefaultTableModel.addRow() normaliza cada fila a exactamente getColumnCount()
    // elementos (rellena con null si faltan, TRUNCA si sobran), así que no se puede
    // guardar el id en una columna "oculta" más allá de las 7 declaradas.
    private final java.util.List<Integer> idsProductosEnTabla = new java.util.ArrayList<>();

    /**
     * Creates new form InventarioPanel
     */
    public InventarioPanel() {
        initComponents();

        // Crea el menú flotante
        this.setOpaque(false);
        JPopupMenu menuUsuario = new JPopupMenu();

        //Crea la opción de cerrar sesión
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");
        // Icono
        // itemCerrarSesion.setIcon(Icono); //Por si quiero importar un icono, pero
        // no lo dice Diseño
        cargarProductosEnTabla();
        // Agregar la acción al botón de cerrar sesión
        itemCerrarSesion.addActionListener(e -> {
            MainFrame framePrincipal = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            if (framePrincipal != null) {
                // Instanciar el nuevo panel del Login de Administrador
                LoginPanel LoginPanel = new LoginPanel(); //Para cada ventana, cambiar su constructor

                // Mandar a hacer el intercambio
                framePrincipal.cambiarPanel(LoginPanel);
            }
        });

        // RF-12: reporte de ventas diario, solo accesible para el Administrador
        JMenuItem itemReporte = new JMenuItem("Reporte de ventas diario");
        itemReporte.addActionListener(e -> {
            java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
            new ReporteVentasDialog(parentFrame, true).setVisible(true);
        });
        menuUsuario.add(itemReporte);
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
        //Establece la fuente a tamaño 56
        jLabel2.putClientProperty("FlatLaf.style", "font: 56 'Arial Rounded MT Bold'");
        jLabel1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/adminblanco.svg", (float) 2.0));
        // Centrar el contenido (icono + texto) horizontalmente
//        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//
//        // Mover el texto abajo del icono (por defecto Swing lo pone a la derecha)
//        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
//        
        

        // Darle espacio de separación entre el icono y el texto
        jButton1.setText("<html><body style='margin-top: 2px;'>Dar de alta nuevo producto</body></html>");

        jButton1.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/plus.svg", (float) 1.0));
        jButton1.putClientProperty("FlatLaf.style", "font: 20 'DearSans-Book'");
        jButton1.putClientProperty("JButton.buttonType", "roundRect");
        
        jButton2.setText("<html><body style='margin-top: 2px;'>Añadir Cajero</body></html>");
        jButton2.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/useradd.svg", (float) 1.0));
        jButton2.putClientProperty("FlatLaf.style", "font: 20 'DearSans-Book'");
        jButton2.putClientProperty("JButton.buttonType", "roundRect");
        
        jButton3.setText("<html><body style='margin-top: 2px;'>Modificar IVA</body></html>");
        jButton3.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/iva.svg", (float) 1.0));
        jButton3.putClientProperty("FlatLaf.style", "font: 20 'DearSans-Book'");
        jButton3.putClientProperty("JButton.buttonType", "roundRect");
        
        jLabel2.setIconTextGap(20);

        configurarColumnaAcciones();
        configurarResaltadoStockCritico();
    }

    // Columna "Acciones": botones Modificar/Borrar resueltos contra idsProductosEnTabla
    private void configurarColumnaAcciones() {
        AccionesProductoColumn columnaAcciones = new AccionesProductoColumn(jTable1, this::abrirEdicionProducto, this::confirmarYBorrarProducto);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(columnaAcciones);
        jTable1.getColumnModel().getColumn(4).setCellEditor(columnaAcciones);
    }

    private void abrirEdicionProducto(int filaModelo) {
        int idProducto = idsProductosEnTabla.get(filaModelo);
        java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);

        NuevoProductoDialog dialog = new NuevoProductoDialog(parentFrame, true, this, idProducto);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void confirmarYBorrarProducto(int filaModelo) {
        int idProducto = idsProductosEnTabla.get(filaModelo);
        String nombreProducto = (String) jTable1.getModel().getValueAt(filaModelo, 0);

        boolean confirmado = ConfirmacionDialog.confirmarAccionDestructiva(this, "Confirmación",
                "¿Desea eliminar el producto \"" + nombreProducto + "\" del catálogo?", "Eliminar");

        if (!confirmado) {
            return;
        }

        ProductoDAO productoDAO = new ProductoDAO();
        if (productoDAO.desactivarProducto(idProducto)) {
            JOptionPane.showMessageDialog(this, "Producto eliminado del catálogo.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarProductosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // RIU-02: resalta en naranja (#FFA500) las filas con stock_actual <= stock_minimo
    private void configurarResaltadoStockCritico() {
        DefaultTableCellRenderer rendererStock = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int filaModelo = table.convertRowIndexToModel(row);
                double stockActual = ((Number) table.getModel().getValueAt(filaModelo, 5)).doubleValue();
                double stockMinimo = ((Number) table.getModel().getValueAt(filaModelo, 6)).doubleValue();
                if (!isSelected) {
                    c.setBackground(stockActual <= stockMinimo ? Color.decode("#FFA500") : Color.WHITE);
                }
                return c;
            }
        };

        for (int columna = 0; columna < jTable1.getColumnModel().getColumnCount(); columna++) {
            if (columna != 4) {
                jTable1.getColumnModel().getColumn(columna).setCellRenderer(rendererStock);
            }
        }
    }

    public void cargarProductosEnTabla() {
        // Obtencion el modelo por defecto del jTable
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        // Limpieza de  la tabla para que no se dupliquen los registros al recargar
        modelo.setRowCount(0);
        idsProductosEnTabla.clear();

        // Instanciar el DAO y traer la lista de objetos
        ProductoDAO productoDAO = new ProductoDAO();
        List<Object[]> productos = productoDAO.obtenerProductos();

        // Recorrer la lista e ir agregando fila por fila al modelo de la tabla
        for (Object[] fila : productos) {
            modelo.addRow(fila);
            // El id_producto (fila[7]) se guarda aparte: addRow() trunca las filas a las
            // 7 columnas declaradas, así que ese 8vo elemento nunca sobrevive en el modelo.
            idsProductosEnTabla.add((Integer) fila[7]);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        // Convertimos el objeto Graphics a Graphics2D para acceder a funciones avanzadas de renderizado
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Habilitar Antialiasing para que la transición de colores se vea fluida y limpia
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Definir los colores usando tus códigos Hexadecimales
        Color colorInicio = Color.decode("#232AA8"); // Rosa arriba
        Color colorFin = Color.decode("#DEEDFF");    // Azul abajo
        
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Administrador");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 460, -1));
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 130, 130));

        jButton1.setText("Dar de alta nuevo producto");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 370, -1));

        jButton2.setText("Agregar cajero");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, 270, -1));

        jButton3.setText("Modificar IVA");
        jButton3.addActionListener(this::jButton3ActionPerformed);
        add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 190, 260, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "SKU", "Precio", "Precio de compra", "Acciones", "Stock actual", "Stock mínimo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(10);
        }

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 1090, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //Instancia de ConfiguracionDAO y hace la query a la DB
        ConfiguracionDAO configDao = new ConfiguracionDAO();
        int ivaActual = configDao.getIVA();
        // Spinner (ValorInicial, Mínimo, Máximo, Paso/Incremento)
        // Numero 16 como valor inicial con rango de 0 a 100 y avanza de 1 en 1
        SpinnerNumberModel modeloIva = new SpinnerNumberModel(ivaActual, 0, 100, 1);
        JSpinner spinnerIva = new JSpinner(modeloIva);

        // Hacer que el texto del spinner termine con el símbolo de porcentaje
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinnerIva, "#'%'");
        spinnerIva.setEditor(editor);

        // Meter el spinner dentro de un JDialog modal usando JOptionPane
        // Crea la ventana pequeña, centrada y con botones de OK/Cancelar
        int opcion = JOptionPane.showOptionDialog(
                this, // Ventana padre (bloquea el MainFrame)
                spinnerIva, // Componente que se va a mostrar dentro
                "Modificar porcentaje de IVA", // Título de la ventanita
                JOptionPane.OK_CANCEL_OPTION, // Botones debajo
                JOptionPane.QUESTION_MESSAGE, // Ícono de la ventana
                null, null, null
        );

        // Procesado de la respuesta del usuario
        if (opcion == JOptionPane.OK_OPTION) {
            // Obtenemos el valor numérico directamente
            int nuevoIva = (int) spinnerIva.getValue();

            if (configDao.modificarIVA(nuevoIva)) {
                JOptionPane.showMessageDialog(this, "IVA actualizado en la base de datos", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el IVA", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Buscar el frame principal que sostiene esta ventana modal
        java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);

        // Instanciamos nuestro diálogo personalizado (parent, modal)
        AltaCajeroDialog dialog = new AltaCajeroDialog(parentFrame, true);

        // Mostrarlo en ventana
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);

        NuevoProductoDialog dialog = new NuevoProductoDialog(null, true, this);
        dialog.setLocationRelativeTo(this); // Para que se centre bonito en la pantalla
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables
}
