package org.gooru.doc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;



public class ItextPDFUtil {


	public static void concatPDFs(List<InputStream> streamOfPDFFiles,
			OutputStream outputStream, boolean paginate) {

		Document document = new Document();
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
								+ currentPageNumber + " of " + totalPages, 520,
								5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * @param inputStream
	 *            Input PDF file
	 * @param outputStream
	 *            Output PDF file
	 * @param fromPage
	 *            start page from input PDF file
	 * @param toPage
	 *            end page from input PDF file
	 */
	public static void splitPDF(InputStream inputStream,
			OutputStream outputStream, int fromPage, int toPage) {
		
		Document document = new Document();
		document.setMargins(0, 0, 0, 0);
		
		try {
			PdfReader inputPDF = new PdfReader(inputStream);

			int totalPages = inputPDF.getNumberOfPages();

			// make fromPage equals to toPage if it is greater
			if (fromPage > toPage) {
				fromPage = toPage;
			}
			if (toPage > totalPages) {
				toPage = totalPages;
			}

			if (fromPage == 0) {
				fromPage = 1;
			}

			if (toPage == 0) {
				toPage = totalPages;
			}

			/*// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
			PdfImportedPage page;

			while (fromPage <= toPage) {
				document.newPage();
				page = writer.getImportedPage(inputPDF, fromPage);
				cb.addTemplate(page, 0, 0);
				fromPage++;
			}
			outputStream.flush();*/

		    //PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		    PdfCopy copy = new PdfCopy(document, outputStream);
		    document.open();
		    //PdfImportedPage page;
		    // Go through all pages
		    for (int i = fromPage; i <= toPage; i++) {
		        //page = writer.getImportedPage(inputPDF, i);		        
		        //Image instance = Image.getInstance(page);		        
		        //document.add(instance);
		    	copy.addPage(copy.getImportedPage(inputPDF, i));
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				document.close();
			} catch (Exception ioe) {
			}
			try {
				inputStream.close();
			} catch (Exception ioe) {
			}
			try {
				outputStream.close();
			} catch (Exception ioe) {
			}
		}
	}

}
