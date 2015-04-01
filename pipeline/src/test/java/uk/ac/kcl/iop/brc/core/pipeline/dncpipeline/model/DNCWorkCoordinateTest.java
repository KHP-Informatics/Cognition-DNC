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
