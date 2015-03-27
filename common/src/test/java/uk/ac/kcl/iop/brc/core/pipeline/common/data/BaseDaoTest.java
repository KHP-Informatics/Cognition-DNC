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

package uk.ac.kcl.iop.brc.core.pipeline.common.data;

import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class BaseDaoTest extends IntegrationTest {

    @Autowired
    private BaseDao baseDao;

    @Test
    public void shouldCreateTable() {
        Session session = baseDao.getSourceSessionFactory().openSession();
        SQLQuery sqlQuery = session.createSQLQuery("create table TestTable(id int)");
        sqlQuery.executeUpdate();

        session.createSQLQuery("insert into TestTable values(1)").executeUpdate();
        session.createSQLQuery("insert into TestTable values(2)").executeUpdate();

        SQLQuery selectQuery = session.createSQLQuery("select * from TestTable");
        List list = selectQuery.list();

        assertThat(list.size(), equalTo(2));
    }

}
