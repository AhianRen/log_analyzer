package com.nt.log_analyzer.utils;

import java.io.OutputStream;
import java.util.List;


import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.nt.log_analyzer.model.LogModel;

public class PdfUtil {
	
	@Test
	public static void createPdf(List<LogModel> data,OutputStream outputStream) throws Exception {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		
		
		document.open();

		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		Font font = new Font(bfChinese);
		//new Font(bfChinese, 18, Font.BOLD);
		
		for (LogModel logModel : data) {
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Phrase(logModel.toString(), font));
			document.add(paragraph);
		}
		
		
		document.close();
		writer.close();
		
	
	}
	
}	
