package com.tienda.util;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Filtros de entrada reutilizables para JTextField/JPasswordField, en línea con el
 * patrón ya usado en NuevoProductoDialog/AperturaCaja: valida formato y longitud máxima
 * en cada tecleo, en vez de solo revisar el texto hasta que el usuario da "Aceptar".
 */
public class Sanitizador {

    private Sanitizador() {
    }

    public static void limitarTexto(JTextField campo, String regex, int maxLongitud) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String siguiente = actual.substring(0, offset) + string + actual.substring(offset);
                if (siguiente.matches(regex) && siguiente.length() <= maxLongitud) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String siguiente = actual.substring(0, offset) + text + actual.substring(offset + length);
                if (siguiente.matches(regex) && siguiente.length() <= maxLongitud) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    // Para contraseñas: solo limita longitud, sin restringir qué caracteres puede usar el usuario
    public static void limitarLongitud(JTextField campo, int maxLongitud) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= maxLongitud) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (fb.getDocument().getLength() - length + text.length() <= maxLongitud) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
}
