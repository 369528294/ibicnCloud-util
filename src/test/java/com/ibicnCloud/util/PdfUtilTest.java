package com.ibicnCloud.util;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

public class PdfUtilTest {
	
	/*@Before
	public void createPdfTest() {
		String path = "D:/ab.pdf";
		String[] contents = {"你好","我是张梅甫"};
		PDFUtil.createPDF(path, contents, true);
		assertTrue(true);
	}*/
	
	/*@Test
	public void readPdfTest() throws UtilException {
		//如何读取PDF
		String path = "D:/ab.pdf";
		String[] contents = {"你好","我是张梅甫"};
		List list = FileUtil.readFileToList(path);
		assertEquals(contents[0], (String)list.get(0));
		assertEquals(contents[1], (String)list.get(1));
		System.out.println(list);
	}*/
}
