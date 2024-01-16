package it.sogei.arc.lab.openpdf;

import java.io.FileOutputStream;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;



public class OpenPdfMain {
	
	static {
	    // must set before the Logger
	    String path = OpenPdfMain.class.getClassLoader().getResource("logging.properties").getFile();
	    System.setProperty("java.util.logging.config.file", path);
	}
	
	public static void main(String[] args) {
		
		Logger logger = Logger.getLogger(OpenPdfMain.class.getSimpleName());

		logger.info("Start creating pdf...");
		
		// creo i font
		Font helvetica_10 = new Font(Font.FontFamily.HELVETICA, 10);
		Font helvetica_18 = new Font(Font.FontFamily.HELVETICA, 18);
		Font times_10 = new Font(Font.FontFamily.TIMES_ROMAN, 10);

		Document document = new Document(PageSize.A4); // setto la page size ed i margini
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));

			// apro il documento
			document.open();
			document.add(new Paragraph(" "));

			/*
			 * HEADER
			 */
			PdfPTable headerTable = new PdfPTable(1);
			headerTable.setTotalWidth(PageSize.A4.getWidth() - 100);
			headerTable.setHorizontalAlignment(Rectangle.ALIGN_CENTER);

			// cella 1 con il testo
			Paragraph title = new Paragraph("Header migrazione iText to OpenPDF", helvetica_10);
			PdfPCell titleCell = new PdfPCell(title);
			titleCell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(titleCell);

			// scrivo la tabella
			headerTable.writeSelectedRows(0, -1, 30, 830, writer.getDirectContent());

			/*
			 * BODY
			 */
			// carico un'immagine
			Image imgSogeiLogo = Image.getInstance("src/main/resources/images/Sogei_logo.png");
			imgSogeiLogo.scalePercent(20);

			// creo una tabella con due celle per gestire il titolo della pagina con
			// un'immagine
			PdfPTable titleTable = new PdfPTable(2);
			float[] widths = { 20f, 80f };
			titleTable.setWidths(widths);
			titleTable.setHorizontalAlignment(Element.ALIGN_LEFT);

			// cella con immagine
			PdfPCell pdfImgCell = new PdfPCell(imgSogeiLogo);
			pdfImgCell.setBorder(Rectangle.NO_BORDER);
			pdfImgCell.setVerticalAlignment(Element.ALIGN_CENTER);
			titleTable.addCell(pdfImgCell);

			// cella con titolo
			Paragraph titleParagraph = new Paragraph("Test migrazione iText a OpenPDF", helvetica_18);
			PdfPCell cell = new PdfPCell(titleParagraph);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

			titleTable.addCell(cell);
			document.add(titleTable);

			// paragrafo con il corpo della pagina
			String paragraphBody = "One goal of LibrePDF is to maintain compatibility with iText, to be a drop-in replacement of iText.\n"
					+ "But compatibility is not always possible, so we collect all changes you need to do in your code.";
			Paragraph subtitleParagraph = new Paragraph(paragraphBody, times_10);
			subtitleParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(subtitleParagraph);

			// CARICAMENTO IMMAGINE TIFF
			// con iText
			String filePath = "src/main/resources/images/dwsample-tiff-640.tif";

			// caso 1: tiff con una sola pagina
//			Image tif = Image.getInstance(filePath);
//			tif.scalePercent(50);
//            document.add(tif);

			// caso 2: tiff con più pagine
			RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(filePath);
			int pages = TiffImage.getNumberOfPages(randomAccess);
			logger.info("Numero pagine TIFF: " + pages);

			for (int i = 1; i <= pages; i++) {
				Image image = TiffImage.getTiffImage(randomAccess, i);
				image.scalePercent(50);
				document.add(image);
			}

			// con OpenPDF
//            File imageFile = new File("src/main/resources/images/dwsample-tiff-640.tif");
//		    try (ImageInputStream iis = ImageIO.createImageInputStream(imageFile)) {
//		        ImageReader reader = getTiffImageReader();
//		        reader.setInput(iis);
//		        int pages = reader.getNumImages(true);
//		        for (int imageIndex = 0; imageIndex < pages; imageIndex++) {
//		            BufferedImage bufferedImage = reader.read(imageIndex);
//		            Image image = Image.getInstance(bufferedImage, null, false);
//		            image.scalePercent(50);
//		            document.add(image);
//		        }
//		    }

			/*
			 * FOOTER
			 */
			// creo una tabella con due celle
			PdfPTable footerTable = new PdfPTable(2);
			footerTable.setTotalWidth(PageSize.A4.getWidth() - 100);
			footerTable.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
			float[] widthsFooterTable = { 90, 10 };
			footerTable.setWidths(widthsFooterTable);

			// cella 1 con il testo
			Paragraph titleFooter = new Paragraph("AIT.ARC - Architetture", helvetica_10);
			PdfPCell paragraphCell = new PdfPCell(titleFooter);
			paragraphCell.setBorder(Rectangle.NO_BORDER);
			footerTable.addCell(paragraphCell);

			// cella 2 con il numero di pagina
			int pageNumber = document.getPageNumber() + 1; // parte da zero
			Paragraph pageNumberText = new Paragraph("Page " + pageNumber, helvetica_10);
			PdfPCell pageNumberCell = new PdfPCell(pageNumberText);
			pageNumberCell.setBorder(Rectangle.NO_BORDER);
			pageNumberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			footerTable.addCell(pageNumberCell);

			// scrivo la tabella
			footerTable.writeSelectedRows(0, -1, 50, 50, writer.getDirectContent());

			/*
			 * METADATA
			 */
			logger.info("Pdf created without errors...");
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			// step 5: we close the document
			document.close();
		}

	}

//	private static ImageReader getTiffImageReader() {
//        Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByFormatName("TIFF");
//        if (!imageReaders.hasNext()) {
//            throw new UnsupportedOperationException("No TIFF Reader found!");
//        }
//        return imageReaders.next();
//    }

}
