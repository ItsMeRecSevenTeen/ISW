/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tienda.vista;

import com.tienda.dao.TurnoCajaDAO;
import com.tienda.modelo.TurnoCaja;
import com.tienda.util.Sesion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * RF-11: arqueo y cierre de turno de caja.
 *
 * @author Rec17
 */
public class CierreCaja extends javax.swing.JPanel {

    private final TurnoCajaDAO turnoCajaDAO = new TurnoCajaDAO();
    private TurnoCaja turno;
    private JTextField txtEfectivoContado;
    private JLabel lblResultado;
    private JButton btnFinalizar;

    public CierreCaja() {
        initComponents();
        this.setOpaque(false);
        construirContenido();
    }

    private void construirContenido() {
        this.setLayout(new BorderLayout());

        int idTurno = Sesion.getInstancia().getIdTurno();
        turno = idTurno > 0 ? turnoCajaDAO.obtenerTurno(idTurno) : null;

        if (turno == null) {
            JLabel lblError = new JLabel("No hay un turno de caja activo para cerrar.", SwingConstants.CENTER);
            lblError.setForeground(Color.WHITE);
            lblError.setFont(new Font("Arial", Font.BOLD, 16));
            this.add(lblError, BorderLayout.CENTER);

            JButton btnVolver = new JButton("Regresar al inicio");
            btnVolver.addActionListener(e -> regresarALogin());
            JPanel panelBoton = new JPanel();
            panelBoton.setOpaque(false);
            panelBoton.add(btnVolver);
            this.add(panelBoton, BorderLayout.SOUTH);
            return;
        }

        JPanel panelTicket = new JPanel();
        panelTicket.setLayout(new BoxLayout(panelTicket, BoxLayout.Y_AXIS));
        panelTicket.setBackground(Color.WHITE);
        panelTicket.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelTicket.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Cierre de turno");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTicket.add(lblTitulo);
        panelTicket.add(Box.createRigidArea(new Dimension(0, 12)));

        double totalEsperado = turno.getFondoInicial() + turno.getTotalVentas();

        panelTicket.add(crearRenglon("Apertura (fondo inicial):", turno.getFondoInicial()));
        panelTicket.add(crearRenglon("Total de ventas del turno:", turno.getTotalVentas()));
        panelTicket.add(crearRenglon("Total esperado en caja:", totalEsperado));
        panelTicket.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel panelInput = new JPanel(new BorderLayout(8, 0));
        panelInput.setBackground(Color.WHITE);
        panelInput.setMaximumSize(new Dimension(320, 32));
        panelInput.add(new JLabel("Efectivo contado: $"), BorderLayout.WEST);

        txtEfectivoContado = new JTextField();
        txtEfectivoContado.putClientProperty("JTextField.placeholderText", "Efectivo físico en caja");
        com.tienda.util.Sanitizador.limitarTexto(txtEfectivoContado, "^\\d*(\\.\\d{0,2})?$", 10);
        panelInput.add(txtEfectivoContado, BorderLayout.CENTER);
        panelTicket.add(panelInput);
        panelTicket.add(Box.createRigidArea(new Dimension(0, 10)));

        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 14));
        panelTicket.add(lblResultado);
        panelTicket.add(Box.createRigidArea(new Dimension(0, 12)));

        btnFinalizar = new JButton("Finalizar Turno");
        btnFinalizar.setBackground(Color.decode("#47D2A5"));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.putClientProperty("JButton.buttonType", "roundRect");
        btnFinalizar.addActionListener(e -> finalizarTurno(totalEsperado));
        panelTicket.add(btnFinalizar);

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.add(Box.createVerticalGlue());
        panelCentral.add(panelTicket);
        panelCentral.add(Box.createVerticalGlue());

        this.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearRenglon(String etiqueta, double monto) {
        JPanel renglon = new JPanel(new BorderLayout());
        renglon.setBackground(Color.WHITE);
        renglon.setMaximumSize(new Dimension(320, 24));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JLabel lblMonto = new JLabel("$" + String.format("%.2f", monto));
        lblMonto.setFont(new Font("Monospaced", Font.PLAIN, 13));

        renglon.add(lblEtiqueta, BorderLayout.WEST);
        renglon.add(lblMonto, BorderLayout.EAST);
        return renglon;
    }

    private void finalizarTurno(double totalEsperado) {
        String texto = txtEfectivoContado.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el efectivo contado en caja.", "Dato requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double efectivoContado = Double.parseDouble(texto);
        double diferencia = efectivoContado - totalEsperado;

        boolean confirmado = ConfirmacionDialog.confirmarAccionDestructiva(this, "Confirmación",
                "¿Desea finalizar y cerrar el turno de caja? Esta acción no se puede deshacer.", "Finalizar turno");
        if (!confirmado) {
            return;
        }

        if (!turnoCajaDAO.cerrarTurno(turno.getIdTurno(), efectivoContado)) {
            JOptionPane.showMessageDialog(this, "No se pudo cerrar el turno en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensajeCuadre = diferencia == 0
                ? "Caja exacta, sin faltante ni sobrante."
                : diferencia > 0
                        ? String.format("Sobrante de $%.2f", diferencia)
                        : String.format("Faltante de $%.2f", Math.abs(diferencia));

        ReporteCierrePdf.exportar(this, turno, efectivoContado, diferencia);

        JOptionPane.showMessageDialog(this, "Turno cerrado correctamente.\n" + mensajeCuadre, "Cierre de caja", JOptionPane.INFORMATION_MESSAGE);

        Sesion.getInstancia().cerrarSesion();
        regresarALogin();
    }

    private void regresarALogin() {
        MainFrame framePrincipal = (MainFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        if (framePrincipal != null) {
            framePrincipal.cambiarPanel(new LoginPanel());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color colorInicio = Color.decode("#232AA8");
        Color colorFin = Color.decode("#9EB3FF");

        GradientPaint degradadoVertical = new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFin);

        g2d.setPaint(degradadoVertical);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1195, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
