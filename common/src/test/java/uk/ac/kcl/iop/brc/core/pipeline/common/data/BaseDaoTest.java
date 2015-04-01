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
