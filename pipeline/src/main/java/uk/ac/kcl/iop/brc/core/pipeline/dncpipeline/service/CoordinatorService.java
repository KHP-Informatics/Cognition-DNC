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


package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spark.Request;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.CoordinatesDao;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.get;

@Service
public class CoordinatorService {

    private static Logger logger = Logger.getLogger(CoordinatorService.class);
    public static String NO_COORDINATE_LEFT = "NO_COORDINATE_TO_PROCESS";

    @Autowired
    private CoordinatesDao coordinatesDao;

    private List<DNCWorkCoordinate> allCoordinates;

    private long lastCheckpoint = 0;


    /**
     * Starts serving coordinates to clients who request work.
     */
    public void startServer() {
        logger.info("Loading coordinates from DB.");
        allCoordinates = coordinatesDao.getCoordinates();
        if (CollectionUtils.isEmpty(allCoordinates)) {
            logger.warn("No coordinates to process! Exiting!");
            return;
        }
        get("/", (request, response) -> handleRequest(request));
    }

    private synchronized String handleRequest(Request request) {
        if (! relevantRequest(request)) {
            return "";
        }
        String cognitionName = request.headers("CognitionName");
        logger.info("Handling request from " + cognitionName + " " + request.ip());

        int chunkSize = Integer.valueOf(request.headers("ChunkSize"));
        logger.info("Assigning " + chunkSize + " coordinates to " + cognitionName);

        List<DNCWorkCoordinate> workLoad = getChunkOfList(allCoordinates, lastCheckpoint, chunkSize);
        if (CollectionUtils.isEmpty(workLoad)) {
            return NO_COORDINATE_LEFT;
        }
        long newCheckPoint = lastCheckpoint + chunkSize;
        logger.info("Serving from " + lastCheckpoint + " to " + newCheckPoint + " to " + cognitionName + " " + request.ip());
        lastCheckpoint = newCheckPoint;

        return new Gson().toJson(workLoad);
    }

    private boolean relevantRequest(Request request) {
        if (request == null) {
            return false;
        }
        String dncRequest = request.headers("DNCRequest");

        return !StringUtils.isBlank(dncRequest) && "true".equalsIgnoreCase(dncRequest);
    }

    public List<DNCWorkCoordinate> getChunkOfList(List<DNCWorkCoordinate> coordinateList, long start, int size) {
        return coordinateList.stream().skip(start).limit(size).collect(Collectors.toList());
    }
}
