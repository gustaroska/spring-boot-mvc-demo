package com.demo.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.service.ReportService;

@Controller
public class PageController {

	@Autowired
	private ReportService reportService;
	
	@RequestMapping("/")
	public String index(Model model, Locale locale, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "index";
	}
	
	@RequestMapping("/pdf/error")
	public void errorPdf(HttpServletResponse response) throws IOException {
		InputStream inputStream = null;
		try {
		//Loading an existing document
	      File file = new ClassPathResource("files/error.pdf").getFile();
	      PDDocument document = PDDocument.load(file);
	       
	      //Retrieving the pages of the document 
	      PDPage page = document.getPage(0);
	      PDPageContentStream contentStream = new PDPageContentStream(
    		    document, page, PDPageContentStream.AppendMode.APPEND, true
    		);
	      
	      //Begin the Content stream 
	      contentStream.beginText(); 
	       
	      //Setting the font to the Content stream  
	      contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

	      //Setting the position for the line 
	      contentStream.newLineAtOffset(25, 500);

	      String text = "This is the sample document and we are adding content to it.";

	      //Adding text in the form of string 
	      contentStream.showText(text);      

	      //Ending the content stream
	      contentStream.endText();

	      System.out.println("Content added");
	      contentStream.close();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			document.save(byteArrayOutputStream);
			document.close();
			
			inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		}catch (Exception e) {
			e.printStackTrace();
		}

		response.setContentType("application/pdf");
	    
	    int nRead;
	    while ((nRead = inputStream.read()) != -1) {
	        response.getWriter().write(nRead);
	    }
	}

	@RequestMapping("/pdf/simple")
	public void showPdfSimple(HttpServletResponse response) throws Exception {
		
		InputStream inputStream = reportService.createSimplePdf();

		response.setContentType("application/pdf");
	    
	    int nRead;
	    while ((nRead = inputStream.read()) != -1) {
	        response.getWriter().write(nRead);
	    }
		
	}
	
	@RequestMapping("/pdf/complex")
	public void showPdfComplex(HttpServletResponse response) throws Exception {
		
		InputStream inputStream = reportService.createComplexPdf();

		response.setContentType("application/pdf");
	    
	    int nRead;
	    while ((nRead = inputStream.read()) != -1) {
	        response.getWriter().write(nRead);
	    }
		
		
	}
	
	
}
