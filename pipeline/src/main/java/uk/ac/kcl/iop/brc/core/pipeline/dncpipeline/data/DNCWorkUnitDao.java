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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.BaseDao;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.SessionWrapper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.BlobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.ClobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.exception.WorkCoordinateNotFound;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;

import java.util.List;

@Repository
public class DNCWorkUnitDao extends BaseDao {

    @Autowired
    private BlobHelper blobHelper;

    @Autowired
    private ClobHelper clobHelper;

    /**
     *
     * @param coordinate Coordinate of the text in the source database.
     * @return The text in the coordinate.
     */
    public String getTextFromCoordinate(DNCWorkCoordinate coordinate) {
        if (coordinate.isBinary()) {
            throw new IllegalArgumentException("Coordinate is not a text coordinate. It's a binary one.");
        }

        Object result = getObjectFromCoordinate(coordinate);

        return clobHelper.getStringFromExpectedClob(result);
    }

    /**
     *
     * @param coordinate Coordinate of the binary object in the source database.
     * @return A byte array of the binary object.
     */
    public byte[] getByteFromCoordinate(DNCWorkCoordinate coordinate) {
        if (! coordinate.isBinary()) {
            throw new IllegalArgumentException("Coordinate is a text coordinate but binary is expected");
        }

        Object blobProxy = getObjectFromCoordinate(coordinate);

        return blobHelper.getByteFromExpectedBlobObject(blobProxy);
    }

    private Object getObjectFromCoordinate(DNCWorkCoordinate coordinate) {
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query coordinateQuery = sessionWrapper.getNamedQuery("getObjectFromCoordinate");
            String queryString = coordinateQuery.getQueryString();
            queryString = queryString.replace(":sourceTable", coordinate.getSourceTable())
                    .replace(":sourceColumn", coordinate.getSourceColumn())
                    .replace(":pkColumnName", coordinate.getPkColumnName())
                    .replace(":id", Long.toString(coordinate.getIdInSourceTable()));

            List result = getSQLResultFromSource(queryString);

            if (CollectionUtils.isEmpty(result)) {
                throw new WorkCoordinateNotFound("Coordinate is invalid. No data found at " + coordinate);
            }

            return result.get(0);
        } finally {
            sessionWrapper.closeSession();
        }
    }

    /**
     *
     * @param coordinate The original coordinate of the text
     * @param processedText The processed text to be saved via saveTextToCoordinate named-query.
     */
    public void saveConvertedText(DNCWorkCoordinate coordinate, String processedText) {
        SessionWrapper sessionWrapper = getCurrentTargetSession();
        try {
            Query query = sessionWrapper.getNamedQuery("saveTextToCoordinate");
            String queryString = query.getQueryString();
            SQLQuery sqlQuery = sessionWrapper.createSQLQuery(queryString);
            sqlQuery.setParameter(0, coordinate.getSourceTable());
            sqlQuery.setParameter(1, coordinate.getSourceColumn());
            sqlQuery.setParameter(2, coordinate.getIdInSourceTable());
            sqlQuery.setParameter(3, processedText);
            sqlQuery.executeUpdate();
        } finally {
            sessionWrapper.closeSession();
        }
    }

}
