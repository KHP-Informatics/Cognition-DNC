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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.kcl.iop.brc.core.pipeline.common.helper.JsonHelper;
import uk.ac.kcl.iop.brc.core.pipeline.common.service.DocumentConversionService;
import uk.ac.kcl.iop.brc.core.pipeline.common.service.FileTypeService;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.StringTools;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.CoordinatesDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.DNCWorkUnitDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.PatientDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation.AnonymisationService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DNCPipelineService {

    private static Logger logger = Logger.getLogger(DNCPipelineService.class);

    @Autowired
    private AnonymisationService anonymisationService;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DNCWorkUnitDao dncWorkUnitDao;

    @Autowired
    private DocumentConversionService documentConversionService;

    @Value("${conversionFormat}")
    private String conversionFormat;

    @Autowired
    private FileTypeService fileTypeService;

    @Autowired
    private CoordinatesDao coordinatesDao;

    @Value("${ocrEnabled}")
    private String ocrEnabled;

    @Value("${pseudonymEnabled}")
    private String pseudonymEnabled;

    private JsonHelper<DNCWorkCoordinate> jsonHelper = new JsonHelper(DNCWorkCoordinate[].class);

    private boolean noPseudonym = false;

    private List<DNCWorkCoordinate> failedCoordinates = Collections.synchronizedList(new ArrayList<>());

    private List<DNCWorkCoordinate> ocrQueue = Collections.synchronizedList(new ArrayList<>());

    /**
     * Anonymise the DNC Work Coordinates (DWC) specified in a view/table in the source DB.
     *
     */
    public void startCreateModeWithDBView() {
        logger.info("Retrieving coordinates from DB.");

        List<DNCWorkCoordinate> dncWorkCoordinates = coordinatesDao.getCoordinates();

        dncWorkCoordinates.parallelStream().forEach(this::processSingleCoordinate);
        logger.info("Finished all non-OCR. Processing the OCR queue now.");
        processOCRQueue();
        logger.info("Finished all.");
        dumpFailedCoordinates();
    }

    /**
     * Anonymise the DNC Work Coordinates (DWC) specified in the jSON file
     * whose path is given as argument.
     * @param filePath File path of the jSON file that contains DNC Work Coordinates.
     */
    public void startCreateModeWithFile(String filePath) {
        logger.info("Loading work units from file.");

        List<DNCWorkCoordinate> workCoordinates = jsonHelper.loadListFromFile(new File(filePath));

        workCoordinates.parallelStream().forEach(this::processSingleCoordinate);
        logger.info("Finished all non-OCR. Processing the OCR queue now.");
        processOCRQueue();
        logger.info("Finished all.");
        dumpFailedCoordinates();
    }

    public void processCoordinates(List<DNCWorkCoordinate> coordinateQueue) {
        coordinateQueue.parallelStream().forEach(this::processSingleCoordinate);
    }

    private void processTextCoordinate(DNCWorkCoordinate coordinate) {
        try {
            logger.info("Anonymising text, coordinates: " + coordinate);
            String text = dncWorkUnitDao.getTextFromCoordinate(coordinate);
            if (pseudonymisationIsEnabled()) {
                Patient patient = patientDao.getPatient(coordinate.getPatientId());
                text = anonymisationService.pseudonymisePersonPlainText(patient, text);
            }
            saveText(coordinate, text);
        } catch (Exception ex) {
            logger.info("Could not process coordinate " + coordinate);
            failedCoordinates.add(coordinate);
            ex.printStackTrace();
        }
    }

    private void processBinaryCoordinate(DNCWorkCoordinate coordinate) {
        try {
            byte[] bytes = dncWorkUnitDao.getByteFromCoordinate(coordinate);
            String text = convertBinary(bytes);
            if (StringTools.noContentInHtml(text) && ocrIsEnabled()) {
                logger.info("Skipping OCR coordinate " + coordinate);
                ocrQueue.add(coordinate);
                return;
            }
            if (pseudonymisationIsEnabled()) {
                logger.info("Pseudonymising binary, coordinates: " + coordinate);
                Patient patient = patientDao.getPatient(coordinate.getPatientId());
                text = pseudonymisePersonText(patient, text);
            }
            saveText(coordinate, text);
        } catch (Exception ex) {
            logger.error("Could not process coordinate " + coordinate );
            failedCoordinates.add(coordinate);
            ex.printStackTrace();
        }
    }

    private boolean pseudonymisationIsEnabled() {
        if (! noPseudonym) {
            return true;
        }
        return "1".equals(pseudonymEnabled) || "true".equalsIgnoreCase(pseudonymEnabled);
    }

    private boolean ocrIsEnabled() {
        return "true".equalsIgnoreCase(ocrEnabled) || "1".equals(ocrEnabled);
    }

    private String tryOCR(byte[] bytes) throws Exception {
        if (! fileTypeService.isPDF(bytes)) {
            logger.info("Ignoring non-PDF file for OCR. OCR cannot be applied to Non-PDF Files.");
        }

        return documentConversionService.getContentFromImagePDF(bytes);
    }

    private String pseudonymisePersonText(Patient patient, String text) {
        if (conversionPreferenceIsHTML()) {
            text = anonymisationService.pseudonymisePersonHTML(patient, text);
        } else {
            text = anonymisationService.pseudonymisePersonPlainText(patient, text);
        }
        return text;
    }

    private String convertBinary(byte[] bytes) {
        if (conversionPreferenceIsHTML()) {
            return documentConversionService.convertToXHTML(bytes);
        }
        return documentConversionService.convertToText(bytes);
    }

    private boolean conversionPreferenceIsHTML() {
        return conversionFormat.equalsIgnoreCase("html") || conversionFormat.equalsIgnoreCase("xhtml");
    }

    private void saveText(DNCWorkCoordinate coordinate, String text) {
        dncWorkUnitDao.saveConvertedText(coordinate, text);
    }

    public void setConversionFormat(String conversionFormat) {
        this.conversionFormat = conversionFormat;
    }

    private void processOCRQueue() {
        ocrQueue.parallelStream().forEach(coordinate -> {
            logger.info("Processing OCR coordinate " + coordinate);
            byte[] bytes = dncWorkUnitDao.getByteFromCoordinate(coordinate);
            try {
                String text = tryOCR(bytes);
                if (pseudonymisationIsEnabled()) {
                    Patient patient = patientDao.getPatient(coordinate.getPatientId());
                    text = pseudonymisePersonText(patient, text);
                }
                saveText(coordinate, text);
            } catch (Exception e) {
                failedCoordinates.add(coordinate);
                logger.error(e.getMessage());
            }
        });
    }

    private void processSingleCoordinate(DNCWorkCoordinate coordinate) {
        logger.info("Processing coordinate " + coordinate);
        if (coordinate.isBinary()) {
            processBinaryCoordinate(coordinate);
        } else {
            processTextCoordinate(coordinate);
        }
    }

    public void setNoPseudonym(boolean noPseudonym) {
        this.noPseudonym = noPseudonym;
    }

    public void dumpFailedCoordinates() {
        if (CollectionUtils.isEmpty(failedCoordinates)) {
            return;
        }
        Gson gson = new Gson();
        String failedJson = gson.toJson(failedCoordinates);
        PrintWriter writer;
        try {
            writer = new PrintWriter("failedCoordinates.json", "UTF-8");
            writer.println(failedJson);
            writer.close();
            logger.info("Dumped all failed coordinates to failedCoordinates.json. You can process them by --createMode --file=failedCoordinates.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
