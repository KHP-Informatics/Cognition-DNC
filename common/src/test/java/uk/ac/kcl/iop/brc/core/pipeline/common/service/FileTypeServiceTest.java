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

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class FileTypeServiceTest {

    @Test
    public void shouldCheckIfFileTypeIsPDF() throws Exception {
        FileTypeService service = new FileTypeService();
        File file = new File(getClass().getClassLoader().getResource("pdfexample.pdf").getPath());
        byte[] bytes = convertFileToBytes(file);

        assertThat(service.isPDF(bytes), equalTo(true));
    }

    public byte[] convertFileToBytes(File file){
        FileInputStream fileInputStream;
        byte[] bFile = new byte[(int) file.length()];

        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bFile;
    }

}
