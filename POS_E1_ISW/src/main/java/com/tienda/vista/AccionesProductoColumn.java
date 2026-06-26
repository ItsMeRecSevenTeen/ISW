package com.tienda.vista;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.IntConsumer;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer + editor de la columna "Acciones" del inventario: dos botones
 * (Modificar / Borrar) por fila, resueltos contra el id_producto oculto de esa fila.
 */
public class AccionesProductoColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private final JTable table;
    private final IntConsumer accionModificar;
    private final IntConsumer accionBorrar;

    private final JPanel panelRender;
    private final JPanel panelEditor;
    private final JButton botonModificarEditor;
    private final JButton botonBorrarEditor;

    public AccionesProductoColumn(JTable table, IntConsumer accionModificar, IntConsumer accionBorrar) {
        this.table = table;
        this.accionModificar = accionModificar;
        this.accionBorrar = accionBorrar;

        this.panelRender = crearPanelBotones(new JButton("Modificar"), new JButton("Borrar"));

        this.botonModificarEditor = new JButton("Modificar");
        this.botonBorrarEditor = new JButton("Borrar");
        this.botonModificarEditor.addActionListener(this);
        this.botonBorrarEditor.addActionListener(this);
        this.panelEditor = crearPanelBotones(botonModificarEditor, botonBorrarEditor);
    }

    private JPanel crearPanelBotones(JButton botonModificar, JButton botonBorrar) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        Insets margen = new Insets(1, 6, 1, 6);
        botonModificar.setMargin(margen);
        botonBorrar.setMargin(margen);
        botonModificar.setFocusPainted(false);
        botonBorrar.setFocusPainted(false);
        panel.add(botonModificar);
        panel.add(botonBorrar);
        return panel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panelRender;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panelEditor;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int filaModelo = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped(); // Cierra el editor antes de abrir diálogos o recargar la tabla

        if (e.getSource() == botonModificarEditor) {
            accionModificar.accept(filaModelo);
        } else if (e.getSource() == botonBorrarEditor) {
            accionBorrar.accept(filaModelo);
        }
    }
}
