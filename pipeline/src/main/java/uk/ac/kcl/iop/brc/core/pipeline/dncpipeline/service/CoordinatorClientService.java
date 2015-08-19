/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu, Richard G. Jackson

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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.kcl.iop.brc.core.pipeline.common.helper.JsonHelper;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;

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

        String jsonCoordinates = "";
        while (thereAreCoordinatesToProcess(jsonCoordinates)) {
            try {
                jsonCoordinates = getCoordinatesAsJsonFromServer();
                if (jsonCoordinates.contains(CoordinatorService.NO_COORDINATE_LEFT))  {
                    logger.info("No more coordinate left to process.");
                    break;
                }
                List<DNCWorkCoordinate> coordinates = convertJsonCoordinateToObjects(jsonCoordinates);
                pipelineService.processCoordinates(coordinates);
            } catch (Exception e) {
                handleException(jsonCoordinates, e);
            }
        }

    }

    private boolean thereAreCoordinatesToProcess(String jsonCoordinates) {
        return ! jsonCoordinates.contains(CoordinatorService.NO_COORDINATE_LEFT);
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

    private List<DNCWorkCoordinate> convertJsonCoordinateToObjects(String jsonCoordinates) throws UnirestException {
        saveCoordinatesInFile(jsonCoordinates, LAST_SET_OF_COORDINATES_FILENAME);
        return jsonHelper.loadListFromFile(new File(LAST_SET_OF_COORDINATES_FILENAME));
    }

    private void saveCoordinatesInFile(String jsonCoordinates, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println(jsonCoordinates);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCoordinatesAsJsonFromServer() throws UnirestException {
        HttpResponse<String> jsonResponse = Unirest.get(serverAddress)
                .header("accept", "application/json")
                .header("DNCRequest", "true")
                .header("CognitionName", cognitionName)
                .header("ChunkSize", chunkSize)
                .asString();

        return jsonResponse.getBody();
    }

    private void setCognitionNameIfNull() {
        if (StringUtils.isBlank(cognitionName)) {
            cognitionName = "CognitionClient" + RandomUtils.nextInt(100000);
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
