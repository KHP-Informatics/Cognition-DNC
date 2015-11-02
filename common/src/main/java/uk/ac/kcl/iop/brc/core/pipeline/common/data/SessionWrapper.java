package uk.ac.kcl.iop.brc.core.pipeline.common.data;


import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class SessionWrapper {

    private static Logger logger = Logger.getLogger(SessionWrapper.class);

    private Session session;

    public SessionWrapper(Session session) {
        this.session = session;
    }

    public Query getNamedQuery(String name) {
        return session.getNamedQuery(name);
    }

    public void closeSession() {
        try {
            session.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public SQLQuery createSQLQuery(String sql) {
        return session.createSQLQuery(sql);
    }
}
