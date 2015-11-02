/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu

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

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseDao {

    @Autowired
    @Qualifier("sourceSessionFactory")
    private SessionFactory sourceSessionFactory;

    @Autowired
    @Qualifier("targetSessionFactory")
    private SessionFactory targetSessionFactory;

    private static Logger logger = Logger.getLogger(BaseDao.class);

    public SessionFactory getSourceSessionFactory() {
        return sourceSessionFactory;
    }

    public void setSourceSessionFactory(SessionFactory sourceSessionFactory) {
        this.sourceSessionFactory = sourceSessionFactory;
    }

    public SessionWrapper getCurrentSourceSession() {
        return new SessionWrapper(sourceSessionFactory.openSession());
    }

    public SessionWrapper getCurrentTargetSession() {
        return new SessionWrapper(targetSessionFactory.openSession());
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
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            SQLQuery query = sessionWrapper.createSQLQuery(sqlQuery);
            query.executeUpdate();
        } finally {
            sessionWrapper.closeSession();
        }
    }

    public void executeSQLQueryForTarget(String sqlQuery) {
        SessionWrapper currentTargetSession = getCurrentTargetSession();
        try {
            SQLQuery query = currentTargetSession.createSQLQuery(sqlQuery);
            query.executeUpdate();
        } finally {
            currentTargetSession.closeSession();
        }
    }

    public List getSQLResultFromSource(String sqlQuery) {
        SessionWrapper currentSourceSession = getCurrentSourceSession();
        try {
            SQLQuery query = currentSourceSession.createSQLQuery(sqlQuery);
            return query.list();
        } finally {
            currentSourceSession.closeSession();
        }
    }

    public List getSQLResultFromTarget(String sqlQuery) {
        SessionWrapper sessionWrapper = getCurrentTargetSession();
        try {
            SQLQuery query = sessionWrapper.createSQLQuery(sqlQuery);
            return query.list();
        } finally {
            sessionWrapper.closeSession();
        }
    }

}
