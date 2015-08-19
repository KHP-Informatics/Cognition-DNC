/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu, Richard G. Jackson

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
