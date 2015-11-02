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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class DNCWorkUnitDaoTest extends IntegrationTest {

    @Autowired
    private DNCWorkUnitDao dao;

    @Before
    public void initDb() {
        dao.executeSQLQueryForSource("create table TestTable1(id int, BinData blob, PRIMARY KEY (id))");
    }

    @After
    public void deleteDb() {
        dao.executeSQLQueryForSource("drop table TestTable1");
    }

    @Test
    public void shouldGetTextFromCoordinate() {
        DNCWorkCoordinate coordinate = new DNCWorkCoordinate().idInSourceTable(1L).pkColumnName("ID")
                .sourceTable("TestTable1").sourceColumn("textColumn");

        dao.executeSQLQueryForSource("alter table TestTable1 add textColumn varchar(100)");
        dao.executeSQLQueryForSource("insert into TestTable1 values(1, '', 'text from coordinate')");

        String textFromCoordinate = dao.getTextFromCoordinate(coordinate);

        assertThat(textFromCoordinate, equalTo("text from coordinate"));
    }

    @Test
    public void shouldSaveAnonymisedTextToTargetDB() {
        dao.executeSQLQueryForTarget("create table TBLTESTOUTPUTCOORDINATE (SOURCETABLE varchar(100), SOURCECOLUMN varchar(100), IDINSOURCETABLE int, PROCESSEDTEXT varchar(100), UPDATETIME varchar(100))");
        DNCWorkCoordinate coordinate = new DNCWorkCoordinate().sourceTable("TBLTESTOUTPUTCOORDINATE")
                .sourceColumn("someBinaryColumn").idInSourceTable(4L);
        coordinate.setUpdateTime("2015-02-11 16:18:43.42");
        dao.saveConvertedText(coordinate, "anonymised text");
        List list = dao.getSQLResultFromTarget("select processedText from TBLTESTOUTPUTCOORDINATE where IDINSOURCETABLE=4");

        String text = (String) list.get(0);

        assertThat(text, equalTo("anonymised text"));
        dao.executeSQLQueryForTarget("DROP TABLE TBLTESTOUTPUTCOORDINATE");
    }

    @Test
    @Ignore
    public void testSqlServerText() {
        String text = dao.getTextFromCoordinate(new DNCWorkCoordinate().idInSourceTable(1L).pkColumnName("ID").sourceTable("tblPatientFreetext").sourceColumn("textContent"));

        assertTrue(text != null);
    }


}
