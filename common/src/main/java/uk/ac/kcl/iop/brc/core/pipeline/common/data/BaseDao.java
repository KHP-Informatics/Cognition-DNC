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
