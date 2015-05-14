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

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.BaseDao;

import java.util.ArrayList;
import java.util.List;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;

@Repository
public class CoordinatesDao extends BaseDao {

    private static Logger logger = Logger.getLogger(CoordinatesDao.class);

    /**
     *
     * @return A list of all coordinates to be processed. Coordinates are retreieved
     * via the named-query "getCoordinates".
     */
    public List<DNCWorkCoordinate> getCoordinates() {
        try {
            Query getCoordinates = getCurrentSourceSession().getNamedQuery("getCoordinates");
            List<DNCWorkCoordinate> coordinateList = getCoordinates
                    .setResultTransformer(Transformers.aliasToBean(DNCWorkCoordinate.class))
                    .list();
            return coordinateList;
        } catch (Exception ex) {
            logger.error("Error loading coordinates. Please check getCoordinates query. " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
