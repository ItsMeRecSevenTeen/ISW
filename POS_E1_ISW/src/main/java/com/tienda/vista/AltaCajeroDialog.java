/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.tienda.vista;

import com.tienda.dao.UsuarioDAO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author Rec17
 */
public class AltaCajeroDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AltaCajeroDialog.class.getName());

    /**
     * Creates new form AltaCajeroDialog
     */
    public AltaCajeroDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal); //Establece la ventana padre, en este caso "MainFram"
        initComponents();
        cargarUsuariosEnTabla();
        // Configurar Botón Modificar (Columna Index 4)
        ButtonColumn botonModificar = new ButtonColumn(jTable1, "Modificar", (row) -> {
            // Aquí extraemos el ID de la fila donde se hizo clic
            int idUsuario = (int) jTable1.getModel().getValueAt(row, 0);
            String nombre = (String) jTable1.getModel().getValueAt(row, 1);

            // Lanzamos la acción
            ejecutarModificarPassword(idUsuario, nombre);
        });
        jTable1.getColumnModel().getColumn(4).setCellRenderer(botonModificar);
        jTable1.getColumnModel().getColumn(4).setCellEditor(botonModificar);

        // Configurar Botón Ver (Columna Index 5)
        ButtonColumn botonVer = new ButtonColumn(jTable1, "Ver", (row) -> {
            int idUsuario = (int) jTable1.getModel().getValueAt(row, 0);

            // Aquí puedes lanzar el JOptionPane para mostrar el Hash real de la DB
            ejecutarMostrarHash(idUsuario, row);
        });
        jTable1.getColumnModel().getColumn(5).setCellRenderer(botonVer);
        jTable1.getColumnModel().getColumn(5).setCellEditor(botonVer);

        // Asociar la tecla enter con envío de credenciales
        jTextField1.addActionListener(e -> jButton1ActionPerformed(null));
        jPasswordField1.addActionListener(e -> jButton1ActionPerformed(null));
        jLabel1.putClientProperty("FlatLaf.style", "font: 24 'DearSans-Book'");
        
        jPasswordField1.putClientProperty("FlatLaf.style",
            "showRevealButton: true; " +
            "showCapsLock: true;");
        // Placeholders
        jTextField1.putClientProperty("JTextField.placeholderText", "Ingrese el nombre del nuevo cajero");
        jPasswordField1.putClientProperty("JTextField.placeholderText", "Ingrese contraseña de nuevo cajero");

        // nombre_usuario es VARCHAR(50) en la BD
        com.tienda.util.Sanitizador.limitarTexto(jTextField1, "^[a-zA-Z0-9_.\\-]*$", 50);
        com.tienda.util.Sanitizador.limitarLongitud(jPasswordField1, 100);
        
        jLabel2.putClientProperty("FlatLaf.style", "font: 26 'DearSans-Book'");
        
        jLabel3.putClientProperty("FlatLaf.style", "font: 26 'DearSans-Book'");
        
        //jButton1.putClientProperty("JButton.buttonType", "roundRect");
        jButton1.putClientProperty("FlatLaf.style", "font: 20 'DearSans-Book'");
        jButton1.setText("<html><body style='margin-top: 2px;'>Guardar</body></html>");
        
        //jButton2.putClientProperty("JButton.buttonType", "roundRect");
        jButton2.putClientProperty("FlatLaf.style", "font: 20 'DearSans-Book'");
        jButton2.setText("<html><body style='margin-top: 2px;'>Cancelar</body></html>");
        com.tienda.util.TablaUtil.fijarColumnas(jTable1);
        setLocationRelativeTo(parent); // Centrar la ventana modal en el centro del frame
    }
    
    private void cargarUsuariosEnTabla() {
        UsuarioDAO usuarioDao = new UsuarioDAO();
        List<Object[]> usuarios = usuarioDao.obtenerUsuarios();

        //Obtener el modelo del JTable
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        // Limpiar la tabla antes de cargarla para no duplicar filas si se refresca
        modelo.setRowCount(0);

        // El ciclo for-each que recorre la lista e inserta renglón por renglón
        for (Object[] usuario : usuarios) {
            modelo.addRow(usuario); // Añade el arreglo como una fila nueva
        }
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                super.setValue("********");
            }
        });
    }
    
    private void ejecutarModificarPassword(int idUsuario, String nombre) {
        UsuarioDAO usuarioDao = new UsuarioDAO();
        String nuevaPassword = JOptionPane.showInputDialog(this,
                "Ingrese la nueva contraseña para " + nombre + ":",
                "Modificar Contraseña", JOptionPane.QUESTION_MESSAGE);

        if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
            
            usuarioDao.actualizarPassword(idUsuario, nuevaPassword);
            JOptionPane.showMessageDialog(this, "Contraseña actualizada con exito!");
            cargarUsuariosEnTabla(); // Refrescar
        }
    }
    
    private void ejecutarMostrarHash(int idUsuario, int row) {
        //Extraer el nombre y el hash directamente del modelo de la tabla usando la fila seleccionada
        String nombreCajero = (String) jTable1.getModel().getValueAt(row, 1);
        String hashCompleto = (String) jTable1.getModel().getValueAt(row, 2);

        // Crea un JTextArea para mostrar el hash largo de forma limpia
        javax.swing.JTextArea txtHash = new javax.swing.JTextArea(hashCompleto);
        txtHash.setEditable(false); // Para que no se modifique
        txtHash.setBackground(null); // Color gris del sistema
        txtHash.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12)); // Fuente tipo código

        // Armar el mensaje estructurado
        Object[] mensaje = {
            "Usuario: " + nombreCajero + " (ID: " + idUsuario + ")",
            "\nHash SHA-256 almacenado en la Base de Datos:",
            txtHash
        };

        // Lanzar la alerta modal
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Seguridad de Contraseña (Encriptada)",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(505, 500));
        setName("Dar de alta nuevo cajero"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Nombre del cajero");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 310, -1));

        jLabel2.setText("Contraseña");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));
        getContentPane().add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 310, -1));

        jLabel3.setText("Usuarios existentes");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        jButton1.setBackground(new java.awt.Color(71, 210, 165));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Guardar");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, -1, -1));

        jButton2.setBackground(new java.awt.Color(255, 38, 105));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Cancelar");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 120, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Contraseña", "Rol", "Modificar", "Ver"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(7);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 440, 240));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose(); //Cerrar este dialog con su liberación del sistema
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Obtiene el texto de los campos
        String nombreCajero = jTextField1.getText().trim();
        String passwordCajero = new String(jPasswordField1.getPassword()); // JPasswordField
        //Valida que no estén vacíos
        if (nombreCajero.isEmpty() || passwordCajero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return; // Detiene la ejecución para que no cierre la ventana
        }

        // Mandar los datos a la base de datos usando el DAO
        UsuarioDAO cajeroDao = new UsuarioDAO();
        int resultado = cajeroDao.registrarCajero(nombreCajero, passwordCajero);

        switch (resultado) {
            case 1:
                JOptionPane.showMessageDialog(this, "Cajero agregado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuariosEnTabla(); // Refresca la tabla automáticamente
                jTextField1.setText(""); // Limpia los campos para que quede listo para otro registro
                jPasswordField1.setText("");
                this.dispose(); // Cerrar la ventana modal limpia
                break;

            case 1062:// Código de error cuando detecta campos ya existentes
                JOptionPane.showMessageDialog(this, "El nombre de usuario '" + nombreCajero + "' ya está registrado, Elije otro", "Usuario Duplicado", JOptionPane.WARNING_MESSAGE);
                jTextField1.requestFocus(); // Selecciona el campo de texto para que corrija el nombre rápido
                break;

            default:
                JOptionPane.showMessageDialog(this, "Hubo un error inesperado al guardar en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
