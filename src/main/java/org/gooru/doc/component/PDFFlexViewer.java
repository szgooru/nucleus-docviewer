package org.gooru.doc.component;

import java.io.File;
import java.util.Properties;

import org.gooru.doc.constants.Constants;
import org.gooru.doc.util.ApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.pdf.PdfReader;

@Component
public class PDFFlexViewer {

  @Autowired
  @javax.annotation.Resource(name = "documentConstants")
  private Properties documentConstants;

  private final Logger logger = LoggerFactory.getLogger(PDFFlexViewer.class);

  public ModelAndView render(String url, Integer startPage, Integer endPage) throws Exception {
    final  String cacheKey = url + "~" + startPage + "~" + endPage;
    final String encryptedName = ApplicationUtils.encryptUrl(cacheKey);
    final String outputSwfFileName = encryptedName + ".swf";
    final String outputSwfFolderPath = getCacheFileLocation() + outputSwfFileName;
    final String swfFile = getCacheFileBaseUrl() + outputSwfFileName;
    final String pdfDocFileName = encryptedName + ".pdf";
    final String pdfFilePath = getCacheFileLocation() + pdfDocFileName;
    final String tempPDFDocFileName = encryptedName + "_temp" + ".pdf";
    final String tempPDFFilePath = getCacheFileLocation() + tempPDFDocFileName;
    // Check File exists
   final  File docFile = new File(pdfFilePath);
    if (!docFile.exists()) {
      url = url.replace(" ", "%20");
      url = url.replace("â", "’");
      url = url.replace("%C2%80%C2%99", " ");
      ApplicationUtils.downloadFileFromWeb(url, tempPDFFilePath);
      PdfReader.unethicalreading = true;
      PdfReader inputPDF = new PdfReader(tempPDFFilePath);
      int totalPages = inputPDF.getNumberOfPages();
      if (endPage == null || endPage == 0) {
        endPage = totalPages;
      }
      if (endPage != 0) {
        logger.warn("splitting");
        ApplicationUtils.splitAndGeneratePDF(tempPDFFilePath, pdfFilePath, startPage, endPage);
      } else {
        ApplicationUtils.downloadFileFromWeb(url, pdfFilePath);
      }

      // Remove temporary file if exists
      File tempFile = new File(tempPDFFilePath);
      if (tempFile.exists()) {
        tempFile.delete();
      }
    }
    File conSWFfile = new File(getCacheFileLocation() + outputSwfFileName);
    if (!conSWFfile.exists()) {
      try {
       final String convertCommand =
            "/usr/bin/pdf2swf" + " " + getCacheFileLocation() + pdfDocFileName + " -o " + outputSwfFolderPath + " -G -f -T 9 -t";
        logger.info("\n Convert to swf file : " + convertCommand + "\n");
        Process process =  Runtime.getRuntime().exec(convertCommand);
        process.waitFor();
      } catch (Exception e) {
        logger.warn("PDF2SWF Conversion failed:exception:", e);
      }
    }

    File convertedfile = new File(getCacheFileLocation() + outputSwfFileName);
    if (convertedfile.exists()) {
      ModelAndView mav = new ModelAndView();
      mav.setViewName("documentviewer");
      mav.addObject("swfFile", swfFile);
      mav.addObject("startPage", startPage);
      mav.addObject("endPage", endPage);
      return mav;
    }
    return null;   
  }

  private String getCacheFileLocation() {
    return documentConstants.getProperty(Constants.CACHE_FILE_LOCATION);
  }

  private String getCacheFileBaseUrl() {
    return documentConstants.getProperty(Constants.CACHE_FILE_BASE_URL);
  }
}
