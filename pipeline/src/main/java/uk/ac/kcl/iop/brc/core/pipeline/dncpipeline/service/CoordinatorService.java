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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spark.Request;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.CoordinatesDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.get;

@Service
public class CoordinatorService {

    private static Logger logger = Logger.getLogger(CoordinatorService.class);

    @Autowired
    private CoordinatesDao coordinatesDao;

    private List<DNCWorkCoordinate> allCoordinates;

    private long lastCheckpoint = 0;

    private int chunkSize = 200;

    @PostConstruct
    public void init() {
        logger.info("Loading coordinates from DB.");
        allCoordinates = coordinatesDao.getCoordinates();
    }

    public void startServer() {
        get("/", (request, response) -> handleRequest(request));
    }

    private synchronized String handleRequest(Request request) {
        String cognitionName = request.headers("CognitionName");
        logger.info("Handling request from " + cognitionName + " " + request.ip());
        if (CollectionUtils.isEmpty(allCoordinates)) {
            logger.warn("No coordinates to process!");
            return "";
        }
        List<DNCWorkCoordinate> workLoad = allCoordinates.stream().skip(lastCheckpoint).limit(chunkSize).collect(Collectors.toList());
        logger.info("Serving from " + lastCheckpoint + " to " +
                (lastCheckpoint + chunkSize) + " to " + cognitionName + " " + request.ip());
        lastCheckpoint += chunkSize;
        return new Gson().toJson(workLoad);
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

}
