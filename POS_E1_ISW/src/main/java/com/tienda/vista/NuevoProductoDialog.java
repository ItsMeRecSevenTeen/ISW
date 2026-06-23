/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.tienda.vista;

import com.tienda.dao.ConfiguracionDAO;
import com.tienda.vista.InventarioPanel;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author karolina
 */
public class NuevoProductoDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NuevoProductoDialog.class.getName());

    /**
     * Creates new form NuevoProductoDialog
     */
    // Conexión continua con el panel de inventario
    private InventarioPanel panelInventario;
    
    public NuevoProductoDialog(java.awt.Frame parent, boolean modal, InventarioPanel panelInventario) {
        super(parent, modal);
        initComponents();
        this.panelInventario = panelInventario;

        // --- SECCIÓN DE HARDENING CON LÍMITES DE BASE DE DATOS ---
    
    // 1. Filtros para Dinero y Stocks (decimal(10,2) -> Máximo 10 caracteres incluyendo el punto, ej: 9999999.99)
    String regexDecimal = "^\\d*(\\.\\d{0,2})?$";
    sanitizarCampo(CodigoBarras, "^[a-zA-Z0-9]*$", 20); //Hardening ligero para el código de barras
    sanitizarCampo(precio, regexDecimal, 10);
    sanitizarCampo(precioCompra, regexDecimal, 10);
    sanitizarCampo(stock, regexDecimal, 10);
    sanitizarCampo(stockMinimo1, regexDecimal, 10);
    
    
    // 2. Filtros para Campos de Texto (Mapeados con los VARCHAR de tu tabla)
    String regexTextoSeguro = "^[a-zA-Z0-9áéíóúñÁÉÍÓÚÑ\\s\\-_\\.]*$";
    sanitizarCampo(nombre, regexTextoSeguro, 100); // varchar(100) en la BD
    sanitizarCampo(marca, regexTextoSeguro, 50);    // varchar(50) en la BD
    
    // 3. Filtro para Tamaño / Contenido Neto (Mapeado con contenido_neto)
    String regexTamano = "^[a-zA-Z0-9\\s\\.]*$";
    sanitizarCampo(tamano, regexTamano, 50);       // varchar(50) en la BD

        jLabel2.putClientProperty("FlatLaf.style", "font: 19 'DearSans-Book'");
        nombre.putClientProperty("JTextField.placeholderText", "Nombre del producto");
        marca.putClientProperty("JTextField.placeholderText", "Marca");
        precio.putClientProperty("JTextField.placeholderText", "Precio");
        precioCompra.putClientProperty("JTextField.placeholderText", "Precio de compra");
        stockMinimo1.putClientProperty("JTextField.placeholderText", "Stock mínimo");
        stock.putClientProperty("JTextField.placeholderText", "Stock");
        tamano.putClientProperty("JTextField.placeholderText", "Tamaño");
        aceptar.putClientProperty("FlatLaf.style", "font: 15 'DearSans-Book'");
        aceptar.setText("<html><body style='margin-top: 2px;'>Aceptar</body></html>");
 
        jLabel3.putClientProperty("FlatLaf.style", "font: 19 'DearSans-Book'");
        
//jButton2.putClientProperty("JButton.buttonType", "roundRect");
        agregarTipo.putClientProperty("FlatLaf.style", "font: 15 'DearSans-Book'");
        agregarTipo.setText("<html><body style='margin-top: 2px;'>Agregar tipo</body></html>");
        setLocationRelativeTo(parent);
         String Tamano=tamano.getText();    
    }
    private void limpiarFormulario() {
    nombre.setText("");
    marca.setText("");
    tamano.setText("");
    precioCompra.setText("");
    precio.setText("");
    stock.setText("");
    stockMinimo1.setText("");
    CodigoBarras.setText("");
    
    // Reseteamos el SKU a su estado inicial
    SKUgen.setText("---"); 
    
    // Si tu combo de tipo de producto tiene elementos, puedes regresarlo al primero:
    if (tipoProducto.getItemCount() > 0) {
        tipoProducto.setSelectedIndex(0);
    }
    
    // Ponemos el foco del teclado de nuevo en el primer campo para comodidad del usuario
    nombre.requestFocus(); 
}
private void sanitizarCampo(javax.swing.JTextField campo, String regex, int maxLongitud) {
    ((javax.swing.text.AbstractDocument) campo.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nextText = currentText.substring(0, offset) + string + currentText.substring(offset);

            // Valida el formato Y que no exceda el tamaño de la base de datos
            if (nextText.matches(regex) && nextText.length() <= maxLongitud) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nextText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

            // Valida el formato Y que no exceda el tamaño de la base de datos
            if (nextText.matches(regex) && nextText.length() <= maxLongitud) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipoProducto = new javax.swing.JComboBox<>();
        agregarTipo = new javax.swing.JButton();
        precioCompra = new javax.swing.JTextField();
        precio = new javax.swing.JTextField();
        stock = new javax.swing.JTextField();
        tamano = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        marca = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        aceptar = new javax.swing.JButton();
        generarSku = new javax.swing.JButton();
        stockMinimo1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        SKUgen = new javax.swing.JTextPane();
        CodigoBarras = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 30, 505, 500));
        setName("nuevoProducto"); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 450));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tipoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un tipo", "Refrescos", "Frituras", "Lacteos", "Dulces" }));
        tipoProducto.addActionListener(this::tipoProductoActionPerformed);
        getContentPane().add(tipoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 150, -1));

        agregarTipo.setText("Agregar tipo +");
        agregarTipo.addActionListener(this::agregarTipoActionPerformed);
        getContentPane().add(agregarTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, -1, -1));

        precioCompra.addActionListener(this::precioCompraActionPerformed);
        getContentPane().add(precioCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 150, -1));

        precio.addActionListener(this::precioActionPerformed);
        getContentPane().add(precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 150, -1));

        stock.addActionListener(this::stockActionPerformed);
        getContentPane().add(stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 150, -1));

        tamano.addActionListener(this::tamanoActionPerformed);
        getContentPane().add(tamano, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 150, -1));

        nombre.addActionListener(this::nombreActionPerformed);
        getContentPane().add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 150, -1));

        marca.addActionListener(this::marcaActionPerformed);
        getContentPane().add(marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 150, -1));

        jLabel2.setText("Tipo de producto");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 170, -1));

        jLabel3.setFont(new java.awt.Font("Helvetica", 1, 18)); // NOI18N
        jLabel3.setText("Código de barras");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 170, -1));

        aceptar.addActionListener(this::aceptarActionPerformed);
        getContentPane().add(aceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, -1, 30));

        generarSku.setText("Generar SKU");
        generarSku.addActionListener(this::generarSkuActionPerformed);
        getContentPane().add(generarSku, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, -1, -1));

        stockMinimo1.addActionListener(this::stockMinimo1ActionPerformed);
        getContentPane().add(stockMinimo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 150, -1));

        SKUgen.setEditable(false);
        SKUgen.setFocusable(false);
        jScrollPane1.setViewportView(SKUgen);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, 150, -1));
        getContentPane().add(CodigoBarras, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 330, 140, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarTipoActionPerformed
         String tipoProd=JOptionPane.showInputDialog("ingresa un nuevo tipo de producto: ");
        tipoProducto.addItem(tipoProd);
    }//GEN-LAST:event_agregarTipoActionPerformed

    private void precioCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioCompraActionPerformed
     /* try{    String PrecioCompra=precioCompra.getText();   
    double preciodouble=Double.parseDouble(PrecioCompra);
    }catch (NumberFormatException e){
        JOptionPane.showMessageDialog(this, "ingresar un número valido");
    }*/
    }//GEN-LAST:event_precioCompraActionPerformed

    private void precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioActionPerformed
     /*   try{    String Precio=precio.getText();   
    double preciodouble2=Double.parseDouble(Precio);
    }catch (NumberFormatException e){
        JOptionPane.showMessageDialog(this, "ingresar un número valido");
    }*/
    }//GEN-LAST:event_precioActionPerformed

    private void stockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockActionPerformed
       /*  try{    String Precio=precioCompra.getText();   
    double preciodouble3=Double.parseDouble(Precio);
    }catch (NumberFormatException e){
        JOptionPane.showMessageDialog(this, "ingresar un número valido");
    }*/
    }//GEN-LAST:event_stockActionPerformed

    private void tamanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tamanoActionPerformed
           // String Tamano=tamano.getText();        // TODO add your handling code here:

    }//GEN-LAST:event_tamanoActionPerformed

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
     //String NombreP=nombre.getText();
    }//GEN-LAST:event_nombreActionPerformed

    private void marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marcaActionPerformed
       // String Marca=marca.getText();       
    }//GEN-LAST:event_marcaActionPerformed

    private void tipoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoProductoActionPerformed
       // String productType=tipoProducto.getSelectedItem().toString();
    }//GEN-LAST:event_tipoProductoActionPerformed

    private void aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarActionPerformed
        // --- VALIDACIONES DE CAMPOS VACÍOS ---
        if (nombre.getText().trim().isEmpty() || marca.getText().trim().isEmpty()
                || tamano.getText().trim().isEmpty() || precioCompra.getText().trim().isEmpty()
                || precio.getText().trim().isEmpty() || stock.getText().trim().isEmpty()
                || stockMinimo1.getText().trim().isEmpty()
                || CodigoBarras.getText().trim().isEmpty()) {

            javax.swing.JOptionPane.showMessageDialog(this, "Todos los campos son requeridos, incluyendo el Código de Barras.", "Campos vacíos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sku = SKUgen.getText().trim();
        if (sku.equals("---") || sku.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "El SKU no se ha generado correctamente.", "Error de SKU", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // --- EXTRACCIÓN DE DATOS ---
            String nombreProd = nombre.getText().trim();
            String marcaProd = marca.getText().trim();
            String contenidoNeto = tamano.getText().trim();
            String codBarras = CodigoBarras.getText().trim();

            double dPrecioCompra = Double.parseDouble(precioCompra.getText().trim());
            double dPrecioVenta = Double.parseDouble(precio.getText().trim());
            double dStockActual = Double.parseDouble(stock.getText().trim());
            double dStockMinimo = Double.parseDouble(stockMinimo1.getText().trim());

            // --- GUARDADO EN BASE DE DATOS ---
            com.tienda.dao.ProductoDAO prodDAO = new com.tienda.dao.ProductoDAO();
            boolean guardadoExitoso = prodDAO.guardarProducto(sku, nombreProd, dPrecioCompra, dPrecioVenta, dStockActual, dStockMinimo, codBarras, contenidoNeto, marcaProd);

            if (guardadoExitoso) {
                javax.swing.JOptionPane.showMessageDialog(this, "Producto registrado y guardado correctamente");

                // Actualizar la tabla del inventario
                if (this.panelInventario != null) {
                    this.panelInventario.cargarProductosEnTabla();
                }

                // Limpieza de campos para el siguiente producto
                limpiarFormulario();

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar el producto.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique los precios y stocks. Ingrese solo números válidos.", "Error de Formato", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_aceptarActionPerformed

    private void generarSkuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarSkuActionPerformed
String txtNombre = nombre.getText().trim();
    String txtMarca = marca.getText().trim();
    String txtTamano = tamano.getText().trim();

    // Validar que los campos no estén vacíos para evitar errores
    if (txtNombre.isEmpty() || txtMarca.isEmpty() || txtTamano.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos para generar el SKU.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
        return; // Sale del método si falta información
    }

        // Dentro del evento del botón que genera el SKU en NuevoProductoDialog.java
// 1. Instanciamos el DAO de configuración
        com.tienda.dao.ConfiguracionDAO configDAO = new com.tienda.dao.ConfiguracionDAO();

// 2. Le mandamos los textos limpios de los JTextFields al método inteligente
        String skuGarantizado = configDAO.SKU(
                nombre.getText().trim(),
                marca.getText().trim(),
                tamano.getText().trim()
        );

// 3. Seteamos el JTextPane o JTextField del resultado con el código de 10 caracteres
        SKUgen.setText(skuGarantizado);
    }//GEN-LAST:event_generarSkuActionPerformed

    private void stockMinimo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockMinimo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stockMinimo1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                NuevoProductoDialog dialog = new NuevoProductoDialog(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CodigoBarras;
    private javax.swing.JTextPane SKUgen;
    private javax.swing.JButton aceptar;
    private javax.swing.JButton agregarTipo;
    private javax.swing.JButton generarSku;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField marca;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField precio;
    private javax.swing.JTextField precioCompra;
    private javax.swing.JTextField stock;
    private javax.swing.JTextField stockMinimo1;
    private javax.swing.JTextField tamano;
    private javax.swing.JComboBox<String> tipoProducto;
    // End of variables declaration//GEN-END:variables
}
