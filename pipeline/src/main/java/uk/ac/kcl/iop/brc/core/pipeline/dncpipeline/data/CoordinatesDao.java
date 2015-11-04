/*
        Cognition-DNC (Dynamic Name Concealer)         Developed by Ismail Kartoglu (https://github.com/iemre)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Biomedical Research Centre for Mental Health

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

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.BaseDao;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.SessionWrapper;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CoordinatesDao extends BaseDao {

    private static Logger logger = Logger.getLogger(CoordinatesDao.class);

    /**
     *
     * @return A list of all coordinates to be processed. Coordinates are retreieved
     * via the named-query "getCoordinates".
     */
    public List<DNCWorkCoordinate> getCoordinates() {
        SessionWrapper session = getCurrentSourceSession();
        try {
            Query getCoordinates = session.getNamedQuery("getCoordinates");
            List<DNCWorkCoordinate> coordinateList = getCoordinates
                    .setResultTransformer(Transformers.aliasToBean(DNCWorkCoordinate.class))
                    .list();
            return coordinateList;
        } catch (Exception ex) {
            logger.error("Error loading coordinates. Please check getCoordinates query. " + ex.getMessage());
            return new ArrayList<>();
        } finally {
            session.closeSession();
        }
    }

}
