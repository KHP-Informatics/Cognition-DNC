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

import uk.ac.kcl.iop.brc.core.pipeline.common.service.DocumentConversionService;
import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.exception.PrimaryKeyNotAvailableException;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.IdAndData;
import org.hibernate.engine.jdbc.SerializableBlobProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MetaDataDaoTest extends IntegrationTest {

    @Autowired
    private MetaDataDao dao;

    @Autowired
    private DocumentConversionService documentConversionService;

    @Before
    public void initDb() {
        dao.useSourceSession();
        dao.executeSQLQueryForSource("create table TestTable1(id int, BinData blob, PRIMARY KEY (id))");
    }

    @After
    public void deleteDb() {
        dao.useSourceSession();
        dao.executeSQLQueryForSource("drop table TestTable1");
    }

    @Test
    public void shouldGetTypeOfColumn() {
        DbColumnTypes type = dao.getTypeOfColumn("TESTTABLE1", "BINDATA");

        assertThat(type, equalTo(DbColumnTypes.BLOB));
    }

    @Test
    public void shouldGetTableNames() {
        List<String> tables = dao.getTableNames("PUBLIC");
        assertThat(tables.contains("TESTTABLE1"), equalTo(true));
    }

    @Test
    @Ignore
    public void shouldGetIdAndColumn() {
        dao.executeSQLQueryForSource("insert into TestTable1 values(1, '')");
        dao.executeSQLQueryForSource("insert into TestTable1 values(2, '')");

        List<IdAndData> idAndDataList = dao.getIdAndColumn("TESTTABLE1", "BINDATA");

        assertThat(idAndDataList.size(), equalTo(2));
    }

    @Test
    public void shouldGetColumnsOfTable() {
        List<String> columns = dao.getColumnsOfTable("TESTTABLE1");

        assertThat(columns.size(), equalTo(2));
    }

    @Test
    public void shouldGetIdColumnOfTable() throws PrimaryKeyNotAvailableException {
        String idColumn = dao.getIdColumnOfTable("TESTTABLE1");
        assertThat(idColumn, equalTo("ID"));
    }

    @Test(expected = PrimaryKeyNotAvailableException.class)
    public void shouldThrowExceptionIfPKIsNotAvailable() {
        dao.executeSQLQueryForSource("create table TestTable2(id int, BinData blob)");
        dao.getIdColumnOfTable("TESTTABLE2");
    }

    @Test
    @Ignore
    public void shouldSwitchFromSourceToTarget() {
        dao.executeSQLQueryForSource("alter table TestTable1 add textColumn varchar(100)");
        dao.executeSQLQueryForSource("insert into TestTable1 values(1, '', 'text from source')");

        dao.executeSQLQueryForTarget("create table TargetTable(id int, data varchar(100))");

        dao.useSourceSession();
        List<String> tableNames = dao.getTableNames("PUBLIC");

        assertThat(tableNames.contains("TargetTable"), equalTo(false));
        assertThat(tableNames.contains("TARGETTABLE"), equalTo(false));
        assertThat(tableNames.contains("targettable"), equalTo(false));

        dao.useTargetSession();
        tableNames = dao.getTableNames("PUBLIC");

        assertThat(tableNames.contains("TARGETTABLE"), equalTo(true));
    }

    @Test
    @Ignore
    public void shouldLoadByteFromDB() throws SQLException {
        List<IdAndData> idAndColumn = dao.getIdAndColumn("varbinarytest", "Document");
        IdAndData idAndData = idAndColumn.get(0);
        Object data = idAndData.getData();
        SerializableBlobProxy blobProxy = (SerializableBlobProxy) Proxy.getInvocationHandler(data);
        Blob wrappedBlob = blobProxy.getWrappedBlob();
        byte[] bytes = wrappedBlob.getBytes(1, (int) wrappedBlob.length());
        String convert = documentConversionService.convertToText(bytes);
        System.out.println(convert);
    }
}
