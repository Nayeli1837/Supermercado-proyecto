package com.example.demo.service;

// Clases utilizadas para obtener los datos de los productos y su composición.
import com.example.demo.model.ComposicionInventario;
import com.example.demo.model.Producto;

// Herramientas para crear documentos PDF y organizar su contenido.
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

// Servicio encargado de generar y exportar los reportes del inventario en PDF.
@Service
public class ReporteService {

	// Genera el reporte con los datos generales, indicadores y composición del inventario.
	public void exportar(HttpServletResponse response, List<Producto> productos,
			List<ComposicionInventario> composicion, long totalProductos, long productosAgotados, long stockBajo,
			int precisionInventario, String usuario) throws IOException {

		// Configura el tamaño de la hoja y los márgenes del documento PDF.
		Document document = new Document(PageSize.A4, 40, 40, 50, 40);

		// Prepara el archivo PDF para enviarlo como respuesta al usuario.
		PdfWriter.getInstance(document, response.getOutputStream());

		// Abre el documento para comenzar a agregar su contenido.
		document.open();

// ==========================
// FUENTES
// ==========================

		// Define los estilos de letra utilizados en las diferentes partes del reporte.
		Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new Color(31, 58, 98));

		Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.GRAY);

		Font seccion = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new Color(31, 58, 98));

		Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10);

// ==========================
// TÍTULO
// ==========================

		// Agrega el nombre del sistema en la parte superior del reporte.
		Paragraph pTitulo = new Paragraph("SIGI", titulo);
		pTitulo.setAlignment(Element.ALIGN_CENTER);
		document.add(pTitulo);

		// Muestra el nombre completo del sistema debajo del título.
		Paragraph sistema = new Paragraph("Sistema Integrado de Gestión de Inventarios", subtitulo);

		sistema.setAlignment(Element.ALIGN_CENTER);
		document.add(sistema);

		// Agrega el título principal del reporte ejecutivo.
		Paragraph reporte = new Paragraph("REPORTE EJECUTIVO DE INVENTARIO",
				FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(43, 179, 214)));

		reporte.setAlignment(Element.ALIGN_CENTER);
		reporte.setSpacingBefore(12);
		document.add(reporte);

		// Agrega un espacio para separar el encabezado de los siguientes datos.
		document.add(new Paragraph(" "));

		// Muestra el usuario que generó el reporte y la fecha y hora de creación.
		Paragraph datos = new Paragraph("Generado por: " + usuario + "\nFecha: " + java.time.LocalDateTime.now()
				.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normal);

		datos.setSpacingAfter(18);

		document.add(datos);

	    // =====================================
	    // RESUMEN EJECUTIVO
	    // =====================================

	    // Título de la sección que presenta los principales datos del inventario.
	    Paragraph resumen = new Paragraph(
	            "RESUMEN EJECUTIVO",
	            seccion);

	    resumen.setSpacingAfter(12);

	    document.add(resumen);

	    // Crea una tabla de cuatro columnas para mostrar los indicadores principales.
	    PdfPTable indicadores = new PdfPTable(4);

	    indicadores.setWidthPercentage(100);

	    indicadores.setSpacingAfter(20);

	    indicadores.setWidths(new float[]{2,2,2,2});

	    // Agrega los indicadores de productos, agotados, stock bajo y precisión.
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

	    // Título de la sección que muestra cómo se distribuyen los productos.
	    Paragraph comp = new Paragraph(
	            "COMPOSICIÓN DEL INVENTARIO",
	            seccion);

	    comp.setSpacingAfter(10);

	    document.add(comp);

	    // Crea una tabla para presentar las categorías y sus cantidades.
	    PdfPTable tablaComp = new PdfPTable(2);

	    tablaComp.setWidthPercentage(60);

	    tablaComp.setSpacingAfter(20);

	    tablaComp.setWidths(new float[]{4,2});

	    // Define los encabezados de las columnas de la tabla.
	    tablaComp.addCell(encabezado("Categoría"));

	    tablaComp.addCell(encabezado("Cantidad"));

	    // Recorre las categorías para agregar sus datos al reporte.
	    for (ComposicionInventario item : composicion) {

	        tablaComp.addCell(crearCelda(item.categoria()));

	        // Muestra la cantidad correspondiente a cada categoría.
	        tablaComp.addCell(
	                crearCelda(
	                        String.valueOf(item.cantidad()),
	                        Element.ALIGN_CENTER));

	    }

	    // Agrega la tabla al documento y finaliza la generación del PDF.
	    document.add(tablaComp);
	    document.close();

	}

	// Crea las tarjetas que muestran los indicadores principales del inventario.
	private PdfPCell crearCard(String titulo, String valor) {

	    PdfPCell cell = new PdfPCell();

	    // Define el espacio interno, los bordes y la altura de la tarjeta.
	    cell.setPadding(14);
	    cell.setBorderColor(new Color(220, 220, 220));
	    cell.setBorderWidth(1f);
	    cell.setMinimumHeight(70);

	    // Crea el texto del indicador, como Productos o Stock Bajo.
	    Paragraph pTitulo = new Paragraph(
	            titulo,
	            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.GRAY));

	    pTitulo.setAlignment(Element.ALIGN_CENTER);

	    // Muestra el valor del indicador con un tamaño de letra más grande.
	    Paragraph pValor = new Paragraph(
	            valor,
	            FontFactory.getFont(
	                    FontFactory.HELVETICA_BOLD,
	                    22,
	                    new Color(31, 58, 98)));

	    pValor.setAlignment(Element.ALIGN_CENTER);

	    // Agrega el título y el valor dentro de la tarjeta.
	    cell.addElement(pTitulo);
	    cell.addElement(pValor);

	    return cell;
	}

	// Crea las celdas de encabezado utilizadas en las tablas del reporte.
	private PdfPCell encabezado(String texto) {

	    PdfPCell celda = new PdfPCell(
	            new Phrase(
	                    texto,
	                    FontFactory.getFont(
	                            FontFactory.HELVETICA_BOLD,
	                            11,
	                            Color.WHITE)));

	    // Aplica el color de fondo y la alineación del texto del encabezado.
	    celda.setBackgroundColor(new Color(43, 179, 214));
	    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
	    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    celda.setPadding(8);

	    return celda;
	}

	// Crea las celdas normales utilizadas para mostrar los datos de las tablas.
	private PdfPCell crearCelda(String texto) {

	    PdfPCell celda = new PdfPCell(
	            new Phrase(
	                    texto,
	                    FontFactory.getFont(
	                            FontFactory.HELVETICA,
	                            10,
	                            Color.BLACK)));

	    // Define el espacio interno y el color de los bordes de cada celda.
	    celda.setPadding(7);
	    celda.setBorderColor(new Color(230, 230, 230));

	    return celda;
	}

	// Crea una celda utilizando la alineación indicada.
	private PdfPCell crearCelda(String texto, int alineacion) {

	    // Primero crea la celda y luego establece la posición de su contenido.
	    PdfPCell celda = crearCelda(texto);

	    celda.setHorizontalAlignment(alineacion);

	    return celda;
	}
}