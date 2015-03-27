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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;
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
        //insert into :targetTable (src_table, src_col, doc_ID, processedText) values(':sourceTable', ':sourceColumn', :sourceId, ':anonymisedText')
        dao.executeSQLQueryForTarget("create table SaveTable(src_table varchar(100), src_col varchar(100), doc_ID int, processedText varchar(100), updatetime varchar(100))");
        DNCWorkCoordinate coordinate = new DNCWorkCoordinate().sourceTable("SaveTable")
                .sourceColumn("someBinaryColumn").idInSourceTable(4L);
        coordinate.setUpdateTime("2015-02-11 16:18:43.42");
        dao.saveConvertedText(coordinate, "anonymised text");
        List list = dao.getSQLResultFromTarget("select processedText from SaveTable where doc_Id=4");

        String text = (String) list.get(0);

        assertThat(text, equalTo("anonymised text"));
    }

    @Test
    @Ignore
    public void testSqlServerText() {
        String text = dao.getTextFromCoordinate(new DNCWorkCoordinate().idInSourceTable(1L).pkColumnName("ID").sourceTable("tblPatientFreetext").sourceColumn("textContent"));

        assertTrue(text != null);
    }


}
