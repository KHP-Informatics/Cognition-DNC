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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class BaseDao {

    @Autowired
    @Qualifier("sourceSessionFactory")
    private SessionFactory sourceSessionFactory;

    @Autowired
    @Qualifier("targetSessionFactory")
    private SessionFactory targetSessionFactory;

    private Session session;

    @PostConstruct
    public void init() {
        session = getCurrentSourceSession();
    }

    public SessionFactory getSourceSessionFactory() {
        return sourceSessionFactory;
    }

    public void setSourceSessionFactory(SessionFactory sourceSessionFactory) {
        this.sourceSessionFactory = sourceSessionFactory;
    }

    public void useSourceSession() {
        session = getCurrentSourceSession();
    }

    public void useTargetSession() {
        session = getCurrentTargetSession();
    }

    public Session getCurrentSourceSession() {
        return sourceSessionFactory.openSession();
    }

    public Session getCurrentTargetSession() {
        return targetSessionFactory.openSession();
    }

    public SessionFactory getTargetSessionFactory() {
        return targetSessionFactory;
    }

    public void setTargetSessionFactory(SessionFactory targetSessionFactory) {
        this.targetSessionFactory = targetSessionFactory;
    }

    public Session createSourceSession() {
        return sourceSessionFactory.openSession();
    }

    public void executeSQLQueryForSource(String sqlQuery) {
        Query query = getCurrentSourceSession().createSQLQuery(sqlQuery);
        query.executeUpdate();
    }

    public void executeSQLQueryForTarget(String sqlQuery) {
        Query query = getCurrentTargetSession().createSQLQuery(sqlQuery);
        query.executeUpdate();
    }

    public List getSQLResultFromSource(String sqlQuery) {
        Query query = getCurrentSourceSession().createSQLQuery(sqlQuery);
        return query.list();
    }

    public List getSQLResultFromTarget(String sqlQuery) {
        Query query = getCurrentTargetSession().createSQLQuery(sqlQuery);
        return query.list();
    }

    public Session getSession() {
        return session;
    }
}
