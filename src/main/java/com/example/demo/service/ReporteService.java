package com.example.demo.service;


import com.example.demo.model.ComposicionInventario;
import com.example.demo.model.Producto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteService {
	
	public void exportar(HttpServletResponse response, List<Producto> productos,
			List<ComposicionInventario> composicion, long totalProductos, long productosAgotados, long stockBajo,
			int precisionInventario, String usuario) throws IOException {

		Document document = new Document(PageSize.A4, 40, 40, 50, 40);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

// ==========================
// FUENTES
// ==========================

		Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new Color(31, 58, 98));

		Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.GRAY);

		Font seccion = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new Color(31, 58, 98));

		Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10);

// ==========================
// TÍTULO
// ==========================

		Paragraph pTitulo = new Paragraph("SIGI", titulo);
		pTitulo.setAlignment(Element.ALIGN_CENTER);
		document.add(pTitulo);

		Paragraph sistema = new Paragraph("Sistema Integrado de Gestión de Inventarios", subtitulo);

		sistema.setAlignment(Element.ALIGN_CENTER);
		document.add(sistema);

		Paragraph reporte = new Paragraph("REPORTE EJECUTIVO DE INVENTARIO",
				FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(43, 179, 214)));

		reporte.setAlignment(Element.ALIGN_CENTER);
		reporte.setSpacingBefore(12);
		document.add(reporte);

		document.add(new Paragraph(" "));

		Paragraph datos = new Paragraph("Generado por: " + usuario + "\nFecha: " + java.time.LocalDateTime.now()
				.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normal);

		datos.setSpacingAfter(18);

		document.add(datos);
		
	    // =====================================
	    // RESUMEN EJECUTIVO
	    // =====================================

	    Paragraph resumen = new Paragraph(
	            "RESUMEN EJECUTIVO",
	            seccion);

	    resumen.setSpacingAfter(12);

	    document.add(resumen);

	    PdfPTable indicadores = new PdfPTable(4);

	    indicadores.setWidthPercentage(100);

	    indicadores.setSpacingAfter(20);

	    indicadores.setWidths(new float[]{2,2,2,2});

	    indicadores.addCell(crearCard(
	            "Productos",
	            String.valueOf(totalProductos)));

	    indicadores.addCell(crearCard(
	            "Agotados",
	            String.valueOf(productosAgotados)));

	    indicadores.addCell(crearCard(
	            "Stock Bajo",
	            String.valueOf(stockBajo)));

	    indicadores.addCell(crearCard(
	            "Precisión",
	            precisionInventario + "%"));

	    document.add(indicadores);
	    
	    Paragraph comp = new Paragraph(
	            "COMPOSICIÓN DEL INVENTARIO",
	            seccion);

	    comp.setSpacingAfter(10);

	    document.add(comp);

	    PdfPTable tablaComp = new PdfPTable(2);

	    tablaComp.setWidthPercentage(60);

	    tablaComp.setSpacingAfter(20);

	    tablaComp.setWidths(new float[]{4,2});

	    tablaComp.addCell(encabezado("Categoría"));

	    tablaComp.addCell(encabezado("Cantidad"));

	    for (ComposicionInventario item : composicion) {

	        tablaComp.addCell(crearCelda(item.categoria()));

	        tablaComp.addCell(
	                crearCelda(
	                        String.valueOf(item.cantidad()),
	                        Element.ALIGN_CENTER));

	    }

	    document.add(tablaComp);
	    document.close();

	}
	
	private PdfPCell crearCard(String titulo, String valor) {

	    PdfPCell cell = new PdfPCell();

	    cell.setPadding(14);
	    cell.setBorderColor(new Color(220, 220, 220));
	    cell.setBorderWidth(1f);
	    cell.setMinimumHeight(70);

	    Paragraph pTitulo = new Paragraph(
	            titulo,
	            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.GRAY));

	    pTitulo.setAlignment(Element.ALIGN_CENTER);

	    Paragraph pValor = new Paragraph(
	            valor,
	            FontFactory.getFont(
	                    FontFactory.HELVETICA_BOLD,
	                    22,
	                    new Color(31, 58, 98)));

	    pValor.setAlignment(Element.ALIGN_CENTER);

	    cell.addElement(pTitulo);
	    cell.addElement(pValor);

	    return cell;
	}
	
	private PdfPCell encabezado(String texto) {

	    PdfPCell celda = new PdfPCell(
	            new Phrase(
	                    texto,
	                    FontFactory.getFont(
	                            FontFactory.HELVETICA_BOLD,
	                            11,
	                            Color.WHITE)));

	    celda.setBackgroundColor(new Color(43, 179, 214));
	    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
	    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    celda.setPadding(8);

	    return celda;
	}
	
	private PdfPCell crearCelda(String texto) {

	    PdfPCell celda = new PdfPCell(
	            new Phrase(
	                    texto,
	                    FontFactory.getFont(
	                            FontFactory.HELVETICA,
	                            10,
	                            Color.BLACK)));

	    celda.setPadding(7);
	    celda.setBorderColor(new Color(230, 230, 230));

	    return celda;
	}
	private PdfPCell crearCelda(String texto, int alineacion) {

	    PdfPCell celda = crearCelda(texto);

	    celda.setHorizontalAlignment(alineacion);

	    return celda;
	}
}