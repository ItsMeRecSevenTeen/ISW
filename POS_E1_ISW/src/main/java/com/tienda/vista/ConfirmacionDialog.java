package com.tienda.vista;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * RIU-03: confirmación explícita para acciones irreversibles (eliminar producto,
 * cerrar turno de caja, anular venta), con los colores definidos en RIU-05:
 * confirmar/destructivo #FF2669, cancelar #47D2A5.
 */
public class ConfirmacionDialog {

    public static boolean confirmarAccionDestructiva(Component parent, String titulo, String mensaje, String textoConfirmar) {
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBackground(Color.decode("#47D2A5"));
        botonCancelar.setForeground(Color.WHITE);
        botonCancelar.putClientProperty("JButton.buttonType", "roundRect");

        JButton botonConfirmar = new JButton(textoConfirmar);
        botonConfirmar.setBackground(Color.decode("#FF2669"));
        botonConfirmar.setForeground(Color.WHITE);
        botonConfirmar.putClientProperty("JButton.buttonType", "roundRect");

        JOptionPane panel = new JOptionPane(mensaje, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION,
                null, new Object[]{botonCancelar, botonConfirmar}, botonCancelar);
        JDialog dialogo = panel.createDialog(parent, titulo);

        boolean[] confirmado = {false};
        botonCancelar.addActionListener(e -> {
            confirmado[0] = false;
            dialogo.dispose();
        });
        botonConfirmar.addActionListener(e -> {
            confirmado[0] = true;
            dialogo.dispose();
        });

        dialogo.setVisible(true);
        return confirmado[0];
    }
}
