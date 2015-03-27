/*
        The MIT License (MIT)
        Copyright (c) 2015 King's College London

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import static junit.framework.Assert.assertEquals;

@Ignore
public class FileTypeServiceTest {

    @Test
    public void testGetType() throws Exception {
        System.out.println("getType");
        //URL defaultImage = this.getClass().getResource("docexample.doc");
        URL resource = getClass().getClassLoader().getResource("docexample.doc");
        File docexample = new File(resource.getPath());


        byte[] bdocexample = convertFileToBytes(docexample);
        FileTypeService instance = new FileTypeService();

        String expResult = "application/msword";
        String result = instance.getType(bdocexample);
        assertEquals(expResult, result);


        docexample = new File(getClass().getClassLoader().getResource("docxexample.docx").toURI());
        bdocexample = convertFileToBytes(docexample);
        expResult ="application/zip";
        result = instance.getType(bdocexample);
        assertEquals(expResult, result);

        docexample = new File(getClass().getClassLoader().getResource("pdfexample.pdf").getPath());
        bdocexample = convertFileToBytes(docexample);
        expResult ="application/pdf";
        result = instance.getType(bdocexample);
        assertEquals(expResult, result);


        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public byte[] convertFileToBytes(File file){
        FileInputStream fileInputStream=null;
        byte[] bFile = new byte[(int) file.length()];

        try {
            //convertToText file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (int i = 0; i < bFile.length; i++) {
                System.out.print((char)bFile[i]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return bFile;
    }

}
