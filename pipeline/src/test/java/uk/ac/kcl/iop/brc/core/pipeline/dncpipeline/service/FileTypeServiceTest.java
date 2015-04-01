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
