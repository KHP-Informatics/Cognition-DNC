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
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.BlobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.ClobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.exception.WorkCoordinateNotFound;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

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
        queryString = queryString.replace(":sourceTable", coordinate.getSourceTable())
                .replace(":sourceColumn", coordinate.getSourceColumn())
                .replace(":sourceId", Long.toString(coordinate.getIdInSourceTable()))
                .replace(":anonymisedText", StringEscapeUtils.escapeSql(anonymisedText))
                .replace(":updateTime", coordinate.getUpdateTime());

        executeSQLQueryForTarget(queryString);
    }

}
