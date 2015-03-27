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

import uk.ac.kcl.iop.brc.core.pipeline.common.data.BaseDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.typehandlers.TypeHandler;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.exception.PrimaryKeyNotAvailableException;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.IdAndData;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DbColumnService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MetaDataDao extends BaseDao {

    private static Logger logger = Logger.getLogger(MetaDataDao.class);

    @Autowired
    private DbColumnService dbColumnService;

    @Value("${databaseName}")
    private String databaseName;

    public DbColumnTypes getTypeOfColumn(String tableName, String columnName) {
        Query query = getSession().getNamedQuery("getColumnType");
        query.setParameter("tableName", tableName);
        query.setParameter("columnName", columnName);
        List list = query.list();
        String result = (String) list.get(0);

        TypeHandler dbTypeHandlerForDb = dbColumnService.getDbTypeHandlerForDb(databaseName);
        if (dbTypeHandlerForDb == null) {
            throw new IllegalArgumentException("Can't handle database " + databaseName);
        }

        return DbColumnTypes.getFromColumnTypeAndDb(result, dbTypeHandlerForDb);
    }

    public List<String> getTableNames(String schemaName) {
        List<String> tableNames = new ArrayList<>();
        Query query = getSession().getNamedQuery("getTableNames");
        query.setParameter("schemaName", schemaName);
        List list = query.list();
        for (Object tableObj : list) {
            String tableName = (String) tableObj;
            tableNames.add(tableName);
        }

        return tableNames;
    }

    /**
     * Fetches a list of id-data pairs from the given table and the given column
     * @param tableName Name of the DB table
     * @param columnName Name of the column
     * @return A list of IdAndData objects
     */
    public List<IdAndData> getIdAndColumn(String tableName, String columnName) {
        String idColumnOfTable = getIdColumnOfTable(tableName);
        if (StringUtils.isEmpty(idColumnOfTable)) {
            logger.warn("Ignoring the table " + tableName + " as it does not have a primary key field!");
            throw new PrimaryKeyNotAvailableException("Table " + tableName + " has no PK");
        }
        Query query = getSession().createSQLQuery("SELECT " + idColumnOfTable + ", " + columnName + " FROM " + tableName);
        List list = query.list();
        List<IdAndData> idAndDataList = new ArrayList<>();
        list.parallelStream().forEach(rowObj -> {
            Object[] row = (Object[]) rowObj;
            Integer id = (Integer) row[0];
            Object data = row[1];
            idAndDataList.add(new IdAndData(id, data));
        });
        return idAndDataList;
    }

    /**
     * @param tableName
     * @return the name of the PK column of the given table
     * @throws PrimaryKeyNotAvailableException if no PK column is available
     */
    public String getIdColumnOfTable(String tableName) throws PrimaryKeyNotAvailableException {
        Query query = getSession().getNamedQuery("getIdColumnOfTable");
        query.setParameter("tableName", tableName);
        List list = query.list();
        if (CollectionUtils.isEmpty(list)) {
            throw new PrimaryKeyNotAvailableException("Primary key is not available");
        }
        return (String) list.get(0);
    }

    /**
     * @param tableName
     * @return A list of colum names of the given table
     */
    public List<String> getColumnsOfTable(String tableName) {
        Query query = getSession().getNamedQuery("getColumnsOfTable");
        query.setParameter("tableName", tableName);
        return (List<String>) query.list();
    }

}
