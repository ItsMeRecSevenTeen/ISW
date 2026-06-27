package com.tienda.util;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * Ajustes comunes para las JTable del sistema: deja las columnas fijas para que su
 * orden y ancho no cambien en tiempo de ejecución (las posiciones de columna están
 * cableadas en el código, así que reacomodarlas rompería la lógica).
 */
public final class TablaUtil {

    private TablaUtil() {
    }

    public static void fijarColumnas(JTable tabla) {
        JTableHeader cabecera = tabla.getTableHeader();
        cabecera.setReorderingAllowed(false); // no se pueden arrastrar/reacomodar columnas
        cabecera.setResizingAllowed(false);   // ancho de columna fijo

        // Quita el resaltado de FlatLaf al pasar el mouse o presionar la cabecera.
        cabecera.putClientProperty("FlatLaf.style", "hoverBackground: null; pressedBackground: null");
    }
}
