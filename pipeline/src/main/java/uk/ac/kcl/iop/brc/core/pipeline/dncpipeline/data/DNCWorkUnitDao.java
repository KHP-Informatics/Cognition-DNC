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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.BaseDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.BlobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.ClobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.exception.WorkCoordinateNotFound;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;

import java.util.List;

@Repository
public class DNCWorkUnitDao extends BaseDao {

    @Autowired
    private BlobHelper blobHelper;

    @Autowired
    private ClobHelper clobHelper;

    public String getTextFromCoordinate(DNCWorkCoordinate coordinate) {
        if (coordinate.isBinary()) {
            throw new IllegalArgumentException("Coordinate is not a text coordinate. It's a binary one.");
        }

        Object result = getObjectFromCoordinate(coordinate);

        return clobHelper.getStringFromExpectedClob(result);
    }

    public byte[] getByteFromCoordinate(DNCWorkCoordinate coordinate) {
        if (! coordinate.isBinary()) {
            throw new IllegalArgumentException("Coordinate is a text coordinate but binary is expected");
        }

        Object blobProxy = getObjectFromCoordinate(coordinate);

        return blobHelper.getByteFromExpectedBlobObject(blobProxy);
    }

    private Object getObjectFromCoordinate(DNCWorkCoordinate coordinate) {
        Query coordinateQuery = getCurrentSourceSession().getNamedQuery("getObjectFromCoordinate");
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
    }

    public void saveConvertedText(DNCWorkCoordinate coordinate, String anonymisedText) {
        Query query = getCurrentTargetSession().getNamedQuery("saveTextToCoordinate");
        String queryString = query.getQueryString();
        SQLQuery sqlQuery = getCurrentTargetSession().createSQLQuery(queryString);
        sqlQuery.setParameter(0, coordinate.getSourceTable());
        sqlQuery.setParameter(1, coordinate.getSourceColumn());
        sqlQuery.setParameter(2, coordinate.getIdInSourceTable());
        sqlQuery.setParameter(3, anonymisedText);
        sqlQuery.executeUpdate();
    }

}
