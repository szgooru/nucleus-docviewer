package org.gooru.doc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gooru.doc.component.PDFFlexViewer;
import org.gooru.doc.component.PDFJsViewer;
import org.gooru.doc.constants.Constants;
import org.gooru.doc.util.ApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DocumentViewerController {

  private final Logger logger = LoggerFactory.getLogger(DocumentViewerController.class);

  @Autowired
  private PDFJsViewer pdfJsViewer;

  @Autowired
  private PDFFlexViewer pdfFlexViewer;

  @RequestMapping(method = RequestMethod.GET, value = "/view")
  public ModelAndView viewDocument(@RequestParam(value = Constants.URL, required = true) String url, @RequestParam(value = Constants.START_PAGE,
    required = false) Integer startPage, @RequestParam(value = Constants.END_PAGE, required = false) Integer endPage, @RequestParam(
    value = Constants.ENGINE, required = false) String engine, HttpServletRequest request, HttpServletResponse response) throws Exception {
    ModelAndView mav = null;
    if (endPage != null && endPage.intValue() == 1000) {
      endPage = null;
    }
    if (startPage == null || startPage == 0) {
      startPage = 1;
    }
    try {
      engine = ApplicationUtils.getDocumentViewerEngine(engine, request.getHeader(Constants.USER_AGENT));
System.out.println("engine" + engine);
      if (engine.equalsIgnoreCase("pdfjs")) {
        mav = pdfJsViewer.render(url, startPage, endPage);
      } else if (engine.equalsIgnoreCase("flex")) {
        mav = pdfFlexViewer.render(url, startPage, endPage);
      }      
    } catch (Exception e) {
      System.out.println(e);
      logger.warn("Rendering PDF file using google doc viewer, looks like other pdf rendering mechanism is failed {}", e);
    }
    if (mav == null) {
      mav = new ModelAndView("redirect:https://docs.google.com/gview?embedded=true&url=" + url);
    }
    return mav;

  }

}
