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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.kcl.iop.brc.core.pipeline.common.helper.JsonHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CoordinatorClientService {

    private static String LAST_SET_OF_COORDINATES_FILENAME = "lastBatchOfCoordinates.json";

    private static Logger logger = Logger.getLogger(CoordinatorClientService.class);

    @Autowired
    private DNCPipelineService pipelineService;

    private JsonHelper<DNCWorkCoordinate> jsonHelper = new JsonHelper(DNCWorkCoordinate[].class);

    private String serverAddress;

    private String cognitionName;

    private String chunkSize = "10000";

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Starts retrieving coordinates from the coordinator server via HTTP.
     */
    public void startProcessing() {
        setCognitionNameIfNull();
        logger.info(cognitionName + " is starting processing documents now.");

        List<DNCWorkCoordinate> workCoordinates;
        String jsonCoordinates = "";
        while (! jsonCoordinates.equalsIgnoreCase(CoordinatorService.NO_COORDINATE_LEFT)) {
            try {
                jsonCoordinates = getCoordinatesAsJsonFromServer();
                workCoordinates = getDncWorkCoordinates(jsonCoordinates);
                pipelineService.processCoordinates(workCoordinates);
            } catch (Exception e) {
                handleException(jsonCoordinates, e);
            }
        }

    }

    private void handleException(String jsonCoordinates, Exception e) {
        String failedCoordinateFile = "failedCoordinates" + RandomUtils.nextInt() + ".json";
        saveCoordinatesInFile(jsonCoordinates, failedCoordinateFile);
        e.printStackTrace();
        System.out.println("There were errors in the last batch. " +
                "Last batch's coordinates were written in " + failedCoordinateFile + " file. " +
                "So you can re-process them by using --createMode --file=" + failedCoordinateFile + " arguments. " +
                "Press enter to continue on to the next batch.");
        try {
            System.in.read();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private List<DNCWorkCoordinate> getDncWorkCoordinates(String jsonCoordinates) throws UnirestException {
        saveCoordinatesInFile(jsonCoordinates, LAST_SET_OF_COORDINATES_FILENAME);
        return jsonHelper.loadListFromFile(new File(LAST_SET_OF_COORDINATES_FILENAME));
    }

    private void saveCoordinatesInFile(String jsonCoordinates, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(LAST_SET_OF_COORDINATES_FILENAME, "UTF-8");
            writer.println(jsonCoordinates);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCoordinatesAsJsonFromServer() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.get(serverAddress)
                .header("accept", "application/json")
                .header("DNCRequest", "true")
                .header("CognitionName", cognitionName)
                .header("ChunkSize", chunkSize)
                .asJson();

        return jsonResponse.getBody().toString();
    }

    private void setCognitionNameIfNull() {
        if (StringUtils.isBlank(cognitionName)) {
            cognitionName = "DNCCognitionClient" + RandomUtils.nextInt(100000);
        }

    }

    public void setCognitionName(String cognitionName) {
        this.cognitionName = cognitionName;
    }

    public String getCognitionName() {
        return cognitionName;
    }

    public void setChunkSize(String chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String getChunkSize() {
        return chunkSize;
    }
}
