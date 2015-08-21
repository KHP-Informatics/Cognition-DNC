/*
        Copyright (c) 2015 King's College London

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package uk.ac.kcl.iop.brc.core.pipeline.common.service;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;
import uk.ac.kcl.iop.brc.core.pipeline.common.exception.CanNotProcessCoordinateException;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.assertTrue;

public class DocumentConversionServiceTest {

    private DocumentConversionService service;

    @Before
    public void init() throws TikaException, IOException, SAXException {
        service = new DocumentConversionService();
    }

    @Test
    public void shouldConvertWordDocToText() throws IOException, TikaException, SAXException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("testDoc.doc");
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);

        String text = service.convertToText(bytes);

        assertTrue(text.contains("test"));
    }

    @Test
    public void shouldConvertDocToXHTML() throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("docxexample.docx");
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);

        String text = service.convertToXHTML(bytes);
        System.out.println(text);

        assertTrue(text.contains("<body>"));
        assertTrue(text.contains("<tr>"));
    }

    @Test
    public void shouldConvertPDFToXHTML() throws IOException, TikaException, SAXException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("pdfexample.pdf");
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);

        String text = service.convertToXHTML(bytes);
        System.out.println(text);

        assertTrue(text.contains("<body>"));
        assertTrue(text.contains("Introduction"));
    }

    @Test
    public void shouldTryOCRByConvertingToTiff() throws TikaException, IOException, SAXException, CanNotProcessCoordinateException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("test_ocr_pdf.pdf");
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);

        String text = service.tryOCRByConvertingToTiff(new DNCWorkCoordinate().sourceTable("table1"), bytes);
        System.out.println(text);

        assertTrue(text.contains("This is a random"));
    }

}
