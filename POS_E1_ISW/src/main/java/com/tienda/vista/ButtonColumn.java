/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.vista;

/**
 *
 * @author Rec17
 */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.function.Consumer;
public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private final JTable table;
    private final JButton renderButton;
    private final JButton editButton;
    private final Consumer<Integer> action;
    private Object editorValue;

    public ButtonColumn(JTable table, String text, Consumer<Integer> action) {
        this.table = table;
        this.action = action;

        // Botón que se dibuja de fondo
        this.renderButton = new JButton(text);
        
        // Botón real que recibe el clic
        this.editButton = new JButton(text);
        this.editButton.setFocusPainted(false);
        this.editButton.addActionListener(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.editorValue = value;
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped(); // Cierra el editor inmediatamente al hacer clic
        action.accept(row);   // Ejecuta la acción pasándole la fila seleccionada
    }
}