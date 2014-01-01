package com.ibicnCloud.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Date;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFUtil {

	/*
	 * row表示表格的行数 rotate表示横竖，true是坚
	 */
	public static void createPDFExcel(String path, String[] content, int row, Boolean rotate) {
		if (content == null) {
			return;
		}
		try {
			File myFile = new File(path);
			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			Document document = new Document(rotate == true ? PageSize.A4 : PageSize.A4.rotate());// 创建word文档,并设置纸张的大小
			PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			document.setMargins(3.5f * 28.5f, 1.5f * 28.5f, 3.17f * 28.5f, 3.17f * 28.5f);
			// 下面是解决中文的问题
			BaseFont bf = BaseFont.createFont("c:\\windows\\fonts\\simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font nobold = FontFactory.getFont("Arial", 22, Font.NORMAL);
			Font bold = FontFactory.getFont("Arial", 22, Font.BOLD);
			Font zn = new Font(bf);
			PdfPTable table = new PdfPTable(row);// 大小必须刚刚好，缺一个单元整行都没了
			for (int i = 0; i < content.length; i++) {
				PdfPCell cell = new PdfPCell();
				cell.addElement(new Paragraph(content[i], zn));
				table.addCell(cell);
			}
			document.add(table);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createPDF(String path, String[] content, Boolean rotate) {
		try {
			File myFile = new File(path);
			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			Document document = new Document(rotate == true ? PageSize.A4 : PageSize.A4.rotate());// 创建word文档,并设置纸张的大小
			PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			document.setMargins(3.5f * 28.5f, 1.5f * 28.5f, 3.17f * 28.5f, 3.17f * 28.5f);
			// 下面是解决中文的问题
			BaseFont bf = BaseFont.createFont("c:\\windows\\fonts\\simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font nobold = FontFactory.getFont("Arial", 22, Font.NORMAL);
			Font bold = FontFactory.getFont("Arial", 22, Font.BOLD);
			Font zn = new Font(bf);
			for (int i = 0; i < content.length; i++) {
				Paragraph p = new Paragraph(content[i], new Font(bf));
				document.add(p);
			}
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
