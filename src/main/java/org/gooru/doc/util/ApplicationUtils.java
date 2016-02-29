package org.gooru.doc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApplicationUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUtils.class);

  public static String encryptUrl(String url) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA-1"); // step 2
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error while authenticating user - No algorithm exists. ", e);
    }
    try {
      md.update(url.getBytes("UTF-8")); // step 3
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Error while authenticating user - ", e);
    }
    byte mdbytes[] = md.digest(); // step 4
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    // String hash = (new Base64Encoder()).encode(raw); // step 5
    String hash = sb.toString();
    return hash; // step 6
  }

  public static void splitAndGeneratePDF(String inputFile, String outputFile, int startPage, int endPage) {
    try {
      ItextPDFUtil.splitPDF(new FileInputStream(inputFile), new FileOutputStream(outputFile), startPage, endPage);

      // remove the file once the new pdf is generated
      File inFile = new File(inputFile);
      File outFile = new File(outputFile);

      if (outFile.exists()) {
        inFile.delete();
      } else {
        LOGGER.info("Error while deleting file :" + inputFile);
      }

    } catch (Exception e) {
      LOGGER.info("Error while splitting PDF: " + inputFile + "\n" + e);
    }
  }

  @SuppressWarnings("deprecation")
  public static void downloadFileFromWeb(String url, String destFilePath) throws Exception {

    if (url.contains("http://") || url.contains("https://")) {
      url =
          StringUtils.substringBeforeLast(url, "/") + "/"
              + URLEncoder.encode(StringUtils.substringAfterLast(url, "/"), "UTF-8").replace("%2520", "%20").replace("%E2%80%99%C2%80%C2%99", "’");
    } else {
      url = URLEncoder.encode(url).replace("%2520", "%20").replace("%E2%80%99%C2%80%C2%99", "’");
    }
    URL srcUrl = new URL(url);
    URLConnection urlCon = (URLConnection) srcUrl.openConnection();
    urlCon.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

    urlCon.connect();
    InputStream inputStream = urlCon.getInputStream();

    File outputFile = new File(destFilePath);

    OutputStream out = new FileOutputStream(outputFile);
    byte buf[] = new byte[1024];
    int len;

    while ((len = inputStream.read(buf)) > 0)
      out.write(buf, 0, len);
    out.close();
    inputStream.close();
  }

  public static String getDocumentViewerEngine(String engine, String userAgent) {
    if (engine == null) {
      if (userAgent.indexOf("Firefox") > -1 || userAgent.indexOf("Chrome") > -1) {
        engine = "pdfjs";
      } else if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Safari") > -1 || engine == "flex") {
        engine = "flex";
      }
    }
    if (engine == null) {
      engine = "gdocs";
    }
    return engine;
  }

}
