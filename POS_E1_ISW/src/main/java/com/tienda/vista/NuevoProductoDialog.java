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
    // null = alta de producto nuevo; no null = edición del producto con ese id
    private Integer idProductoEditar;
    // RF-05: venta a granel (se vende por Kg en vez de por pieza)
    private final javax.swing.JCheckBox esGranelCheck = new javax.swing.JCheckBox("Producto a granel (se vende por Kg)");
    private final javax.swing.JTextField precioPorKgField = new javax.swing.JTextField();

    public NuevoProductoDialog(java.awt.Frame parent, boolean modal, InventarioPanel panelInventario) {
        this(parent, modal, panelInventario, null);
    }

    public NuevoProductoDialog(java.awt.Frame parent, boolean modal, InventarioPanel panelInventario, Integer idProducto) {
        super(parent, modal);
        initComponents();
        this.panelInventario = panelInventario;
        this.idProductoEditar = idProducto;

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

        // RF-05: checkbox + campo de precio por Kg para productos a granel
        sanitizarCampo(precioPorKgField, regexDecimal, 10);
        precioPorKgField.putClientProperty("JTextField.placeholderText", "Precio por Kg");
        precioPorKgField.setEnabled(false);
        esGranelCheck.addActionListener(e -> {
            precioPorKgField.setEnabled(esGranelCheck.isSelected());
            if (!esGranelCheck.isSelected()) {
                precioPorKgField.setText("");
            }
        });
        getContentPane().add(esGranelCheck, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 225, -1));
        getContentPane().add(precioPorKgField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 337, 150, -1));

        // Reemplaza los tipos hardcodeados del .form por los persistidos en la BD
        cargarTiposProducto();

        setLocationRelativeTo(parent);

        if (idProductoEditar != null) {
            cargarProductoParaEdicion(idProductoEditar);
        }
    }

    // Llena el combo con el placeholder + los tipos guardados en la tabla configuracion,
    // de modo que los tipos agregados en sesiones anteriores no se pierdan.
    private void cargarTiposProducto() {
        tipoProducto.removeAllItems();
        tipoProducto.addItem("Seleccione un tipo");
        for (String tipo : new ConfiguracionDAO().getTiposProducto()) {
            tipoProducto.addItem(tipo);
        }
    }

    private void cargarProductoParaEdicion(int idProducto) {
        com.tienda.dao.ProductoDAO prodDAO = new com.tienda.dao.ProductoDAO();
        com.tienda.modelo.Producto prod = prodDAO.obtenerProductoPorId(idProducto);
        if (prod == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el producto a editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle("Editar producto");
        nombre.setText(prod.getNombre());
        marca.setText(prod.getMarca());
        tamano.setText(prod.getContenidoNeto());
        precioCompra.setText(String.format("%.2f", prod.getPrecioCompra()));
        precio.setText(String.format("%.2f", prod.getPrecioVenta()));
        stock.setText(String.format("%.2f", prod.getStockActual()));
        stockMinimo1.setText(String.format("%.2f", prod.getStockMinimo()));
        CodigoBarras.setText(prod.getCodigoBarras());
        SKUgen.setText(prod.getSku());

        esGranelCheck.setSelected(prod.isEsGranel());
        precioPorKgField.setEnabled(prod.isEsGranel());
        precioPorKgField.setText(prod.isEsGranel() ? String.format("%.2f", prod.getPrecioPorKg()) : "");

        // tipo_producto no se persiste en la BD; se fija un valor válido para no bloquear el guardado
        if (tipoProducto.getItemCount() > 1) {
            tipoProducto.setSelectedIndex(1);
        }

        aceptar.setText("<html><body style='margin-top: 2px;'>Guardar cambios</body></html>");
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
    esGranelCheck.setSelected(false);
    precioPorKgField.setText("");
    precioPorKgField.setEnabled(false);

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
        setPreferredSize(new java.awt.Dimension(400, 500));
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
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 170, -1));

        aceptar.addActionListener(this::aceptarActionPerformed);
        getContentPane().add(aceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 420, -1, 30));

        generarSku.setText("Generar SKU");
        generarSku.addActionListener(this::generarSkuActionPerformed);
        getContentPane().add(generarSku, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, -1, -1));

        stockMinimo1.addActionListener(this::stockMinimo1ActionPerformed);
        getContentPane().add(stockMinimo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 150, -1));

        SKUgen.setEditable(false);
        SKUgen.setFocusable(false);
        jScrollPane1.setViewportView(SKUgen);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, 150, -1));

        CodigoBarras.addActionListener(this::CodigoBarrasActionPerformed);
        getContentPane().add(CodigoBarras, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 140, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarTipoActionPerformed
        String entrada = JOptionPane.showInputDialog(this, "Ingresa un nuevo tipo de producto:");

        // Cancelar o cerrar (botón X / Esc) devuelve null: no se agrega nada.
        if (entrada == null) {
            return;
        }

        String tipoProd = entrada.trim();
        if (tipoProd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El tipo de producto no puede estar vacío.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Sanitización: solo letras, números y espacios (sin '|' que es el delimitador
        // de la lista persistida ni otros símbolos que corrompan los datos). Máx. 30.
        if (!tipoProd.matches("^[a-zA-Z0-9áéíóúñÁÉÍÓÚÑ ]{1,30}$")) {
            JOptionPane.showMessageDialog(this, "El tipo solo puede contener letras, números y espacios (máximo 30 caracteres).", "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Evita duplicados (incluye el placeholder y los ya existentes), sin distinguir mayúsculas.
        for (int i = 0; i < tipoProducto.getItemCount(); i++) {
            if (tipoProducto.getItemAt(i).equalsIgnoreCase(tipoProd)) {
                JOptionPane.showMessageDialog(this, "Ese tipo de producto ya existe.", "Duplicado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        // Persiste primero; solo si se guarda en la BD se agrega al combo.
        if (new ConfiguracionDAO().agregarTipoProducto(tipoProd)) {
            tipoProducto.addItem(tipoProd);
            tipoProducto.setSelectedItem(tipoProd);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el tipo de producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                || CodigoBarras.getText().trim().isEmpty()
                || (tipoProducto.getSelectedIndex() <= 0 || tipoProducto.getSelectedItem().toString().equals("Seleccione un tipo"))) {

            javax.swing.JOptionPane.showMessageDialog(this, "Todos los campos son requeridos, incluyendo el Código de Barras", "Campos vacíos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (esGranelCheck.isSelected() && precioPorKgField.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese el precio por Kg para un producto a granel.", "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
            precioPorKgField.requestFocus();
            return;
        }

        try {
            // 1. Convertimos los textos a números reales (decimales: la BD usa DECIMAL(10,2) para soportar granel)
            double cantStockMinimo = Double.parseDouble(stockMinimo1.getText().trim());
            double cantStockActual = Double.parseDouble(stock.getText().trim());

            // 2. Hacemos tu validación de negocio
            if (cantStockMinimo > cantStockActual) {
                javax.swing.JOptionPane.showMessageDialog(this, "El Stock Mínimo no puede ser mayor que el Stock Actual", "Error de Inventario", javax.swing.JOptionPane.WARNING_MESSAGE);
                stockMinimo1.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            // Si el usuario metió letras, letras chinas, símbolos o dejó vacío algo numérico
            javax.swing.JOptionPane.showMessageDialog(this, "Los campos de Stock y Stock Mínimo deben ser números válidos", "Error de Formato", javax.swing.JOptionPane.ERROR_MESSAGE);
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

            boolean esGranel = esGranelCheck.isSelected();
            double dPrecioPorKg = esGranel ? Double.parseDouble(precioPorKgField.getText().trim()) : 0.0;

            // --- GUARDADO EN BASE DE DATOS ---
            com.tienda.dao.ProductoDAO prodDAO = new com.tienda.dao.ProductoDAO();
            boolean esEdicion = idProductoEditar != null;
            boolean guardadoExitoso = esEdicion
                    ? prodDAO.actualizarProducto(idProductoEditar, sku, nombreProd, dPrecioCompra, dPrecioVenta, dStockActual, dStockMinimo, codBarras, contenidoNeto, marcaProd, esGranel, dPrecioPorKg)
                    : prodDAO.guardarProducto(sku, nombreProd, dPrecioCompra, dPrecioVenta, dStockActual, dStockMinimo, codBarras, contenidoNeto, marcaProd, esGranel, dPrecioPorKg);

            if (guardadoExitoso) {
                javax.swing.JOptionPane.showMessageDialog(this, esEdicion ? "Producto actualizado correctamente" : "Producto registrado y guardado correctamente");

                // Actualizar la tabla del inventario
                if (this.panelInventario != null) {
                    this.panelInventario.cargarProductosEnTabla();
                }

                if (esEdicion) {
                    dispose();
                } else {
                    // Limpieza de campos para el siguiente producto
                    limpiarFormulario();
                }

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, esEdicion ? "No se pudo actualizar el producto." : "No se pudo registrar el producto.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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

    private void CodigoBarrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodigoBarrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodigoBarrasActionPerformed

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
