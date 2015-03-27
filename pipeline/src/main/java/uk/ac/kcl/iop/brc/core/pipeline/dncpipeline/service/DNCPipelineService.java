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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import uk.ac.kcl.iop.brc.core.pipeline.common.helper.JsonHelper;
import uk.ac.kcl.iop.brc.core.pipeline.common.service.DocumentConversionService;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.DNCWorkUnitDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.PatientDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation.AnonymisationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.List;

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

    private JsonHelper<DNCWorkCoordinate> jsonHelper = new JsonHelper(DNCWorkCoordinate[].class);

    public void startCreateModeFromWS() {
        throw new NotImplementedException();
    }

    /**
     * Anonymise the DNC Work Coordinates (DWC) specified in the jSON file
     * whose path is given as argument.
     * @param filePath File path of the jSON file that contains DNC Work Coordinates.
     */
    public void startCreateModeWithFile(String filePath) {
        logger.info("Loading work units from file.");
        List<DNCWorkCoordinate> DNCWorkCoordinates = jsonHelper.loadListFromFile(new File(filePath));

        DNCWorkCoordinates.parallelStream().forEach(coordinate -> {
            logger.info("Processing coordinate " + coordinate);
            if (coordinate.isBinary()) {
                anonymiseBinaryCoordinate(coordinate);
            } else {
                anonymiseTextCoordinate(coordinate);
            }
        });
        logger.info("Finished all.");
    }

    @Transactional("targetTX")
    private void anonymiseTextCoordinate(DNCWorkCoordinate coordinate) {
        try {
            logger.info("Anonymising text, coordinates: " + coordinate);
            String text = dncWorkUnitDao.getTextFromCoordinate(coordinate);
            Patient patient = patientDao.getPatient(coordinate.getPatientId());
            text = anonymisationService.anonymisePatientPlainText(patient, text);
            saveAnonymisedText(coordinate, text);
        } catch (Exception ex) {
            logger.info("Could not process coodinate " + coordinate);
            ex.printStackTrace();
        }
    }

    @Transactional("targetTX")
    private void anonymiseBinaryCoordinate(DNCWorkCoordinate coordinate) {
        try {
            logger.info("Anonymising binary, coordinates: " + coordinate);
            Patient patient = patientDao.getPatient(coordinate.getPatientId());
            byte[] bytes = dncWorkUnitDao.getByteFromCoordinate(coordinate);
            String text = convertBinary(bytes);
            text = anonymsisePatientText(patient, text);
            saveAnonymisedText(coordinate, text);
        } catch (Exception ex) {
            logger.error("Could not process coordinate " + coordinate);
            ex.printStackTrace();
        }

    }

    private String anonymsisePatientText(Patient patient, String text) {
        if (conversionPreferenceIsHTML()) {
            text = anonymisationService.anonymisePatientHTML(patient, text);
        } else {
            text = anonymisationService.anonymisePatientPlainText(patient, text);
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

    private void saveAnonymisedText(DNCWorkCoordinate coordinate, String text) {
        dncWorkUnitDao.saveConvertedText(coordinate, text);
    }

    public void setConversionFormat(String conversionFormat) {
        this.conversionFormat = conversionFormat;
    }
}