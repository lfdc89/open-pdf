package it.sogei.arc.lab.openpdf;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class OpenPdfMain {

	public static void main(String[] args) {

		System.out.println("Start creating pdf...");

		Document document = new Document(PageSize.A4, 50, 50, 50, 50); // setto la page size ed i margini
		try {
			PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));

			BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
			BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER, "Cp1252", false);
			BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252", false);

			HeaderFooter header = new HeaderFooter(new Phrase("This is a test pdf to try OpenPDF library", new Font(bf_courier)), false);
			header.setAlignment(Element.ALIGN_LEFT);
			document.setHeader(header);
			
			Image imgSogeiLogo = Image.getInstance("src/main/resources/images/Sogei_logo.png");
			imgSogeiLogo.setAbsolutePosition(100, 100);
			imgSogeiLogo.scalePercent(50);
			
			HeaderFooter footer = new HeaderFooter(new Phrase("Sogei 2023", new Font(bf_courier)), false);
			header.setAlignment(Element.ALIGN_LEFT);
			document.setFooter(footer);
			
			Paragraph parIntestazione = new Paragraph("iText to OpenPDF migration sample");
			parIntestazione.setAlignment(Element.ALIGN_LEFT);
			parIntestazione.setFont(new Font(bf_helv));
			
			

			document.open();
			document.add(parIntestazione);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();

		System.out.println("Pdf created without errors...");
	}

}
