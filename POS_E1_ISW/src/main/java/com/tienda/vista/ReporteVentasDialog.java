package com.tienda.vista;

import com.tienda.dao.ReporteDAO;
import com.tienda.modelo.Reporte;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * RF-12: muestra al Administrador el total de ventas brutas y la ganancia neta del día.
 */
public class ReporteVentasDialog extends JDialog {

    public ReporteVentasDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Reporte de ventas diario");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        Reporte reporte = new ReporteDAO().generarReporteDiario();

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contenido.setBackground(Color.decode("#F0F2F4"));

        JLabel lblTitulo = new JLabel("Ventas de hoy (" + java.time.LocalDate.now() + ")");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.decode("#1A1A2E"));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenido.add(lblTitulo);
        contenido.add(Box.createRigidArea(new Dimension(0, 16)));

        contenido.add(crearRenglon("Ventas brutas del día:", reporte.getTotalVentasBrutas()));
        contenido.add(Box.createRigidArea(new Dimension(0, 8)));
        contenido.add(crearRenglon("Ganancia neta del día:", reporte.getGananciaNeta()));
        contenido.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.putClientProperty("JButton.buttonType", "roundRect");
        btnCerrar.addActionListener(e -> dispose());
        contenido.add(btnCerrar);

        setContentPane(contenido);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel crearRenglon(String etiqueta, double monto) {
        JPanel renglon = new JPanel(new BorderLayout(20, 0));
        renglon.setBackground(Color.decode("#F0F2F4"));
        renglon.setMaximumSize(new Dimension(320, 28));
        renglon.setPreferredSize(new Dimension(320, 28));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEtiqueta.setForeground(Color.decode("#1A1A2E"));

        JLabel lblMonto = new JLabel("$" + String.format("%.2f", monto));
        lblMonto.setFont(new Font("Arial", Font.BOLD, 14));
        lblMonto.setForeground(Color.decode("#1A1A2E"));

        renglon.add(lblEtiqueta, BorderLayout.WEST);
        renglon.add(lblMonto, BorderLayout.EAST);
        return renglon;
    }
}
