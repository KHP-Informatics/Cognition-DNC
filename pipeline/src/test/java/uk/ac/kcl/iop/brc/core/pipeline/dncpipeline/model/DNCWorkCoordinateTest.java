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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import uk.ac.kcl.iop.brc.core.pipeline.common.helper.JsonHelper;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DNCWorkCoordinateTest {

    @Test
    public void shouldReturnTrueIfTypeIsBinary() {
        DNCWorkCoordinate DNCWorkCoordinate = new DNCWorkCoordinate().type("binary");

        assertTrue(DNCWorkCoordinate.isBinary());
    }

    @Test
    public void shouldReturnFalseWhenTypeIsNotBinary() {
        DNCWorkCoordinate DNCWorkCoordinate = new DNCWorkCoordinate().type("notbinary");

        assertFalse(DNCWorkCoordinate.isBinary());
    }

    @Test
    public void shouldConvertToJson() {
        DNCWorkCoordinate DNCWorkCoordinate = new DNCWorkCoordinate().
                idInSourceTable(5L).patientId(1L).pkColumnName("PrimaryKey").type("binary")
                .sourceColumn("SourceColumn").sourceTable("SourceTable");

        String jsonString = DNCWorkCoordinate.toJson();

        assertTrue(jsonString.contains("SourceColumn"));
        assertTrue(jsonString.contains("SourceTable"));
        assertTrue(jsonString.contains("binary"));
        assertTrue(jsonString.contains("PrimaryKey"));
    }

    @Test
    public void shouldLoadListFromFile() {
        JsonHelper<DNCWorkCoordinate> jsonHelper = new JsonHelper<>(DNCWorkCoordinate[].class);

        String path = getClass().getClassLoader().getResource("testworkcoordinates.json").getPath();
        List<DNCWorkCoordinate> DNCWorkCoordinates = jsonHelper.loadListFromFile(new File(path));

        assertThat(DNCWorkCoordinates.size(), equalTo(6));
        assertThat(DNCWorkCoordinates.get(0).getUpdateTime(), equalTo("2015-02-11 16:18:43.42"));
    }

    @Test
    public void shouldReturnEmptyStringIfUpdateTimeIsNull() {
        DNCWorkCoordinate cwc = new DNCWorkCoordinate();
        cwc.setUpdateTime(null);

        assertThat(cwc.getUpdateTime(), equalTo(""));
    }

}
