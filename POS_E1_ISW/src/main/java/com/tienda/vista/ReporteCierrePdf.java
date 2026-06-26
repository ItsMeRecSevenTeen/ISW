package com.tienda.vista;

import com.tienda.modelo.TurnoCaja;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 * RNF-11: exporta el corte de caja a un PDF (un par de KB, muy por debajo del límite de 5MB).
 */
public class ReporteCierrePdf {

    private ReporteCierrePdf() {
    }

    public static void exportar(Component parent, TurnoCaja turno, double efectivoContado, double diferencia) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("corte_caja_turno_" + turno.getIdTurno() + ".pdf"));
        chooser.setDialogTitle("Guardar corte de caja (PDF)");
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = chooser.getSelectedFile();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        double totalEsperado = turno.getFondoInicial() + turno.getTotalVentas();

        String[] lineas = {
            "Apertura: " + (turno.getFechaApertura() != null ? turno.getFechaApertura().format(formato) : "-"),
            "Cierre: " + LocalDateTime.now().format(formato),
            "",
            String.format("Fondo inicial: $%.2f", turno.getFondoInicial()),
            String.format("Total de ventas: $%.2f", turno.getTotalVentas()),
            String.format("Total esperado: $%.2f", totalEsperado),
            String.format("Efectivo contado: $%.2f", efectivoContado),
            diferencia == 0 ? "Cuadre exacto"
                    : diferencia > 0 ? String.format("Sobrante: $%.2f", diferencia)
                    : String.format("Faltante: $%.2f", Math.abs(diferencia))
        };

        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage();
            documento.addPage(pagina);

            try (PDPageContentStream contenido = new PDPageContentStream(documento, pagina)) {
                PDType1Font fuenteTitulo = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fuenteTexto = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float y = 760;
                contenido.beginText();
                contenido.setFont(fuenteTitulo, 16);
                contenido.newLineAtOffset(50, y);
                contenido.showText("Corte de Caja - Turno #" + turno.getIdTurno());
                contenido.endText();

                y -= 30;
                contenido.setFont(fuenteTexto, 12);
                for (String linea : lineas) {
                    contenido.beginText();
                    contenido.newLineAtOffset(50, y);
                    contenido.showText(linea);
                    contenido.endText();
                    y -= 20;
                }
            }

            documento.save(archivo);
            JOptionPane.showMessageDialog(parent, "PDF guardado en: " + archivo.getAbsolutePath(), "Exportado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "No se pudo generar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
