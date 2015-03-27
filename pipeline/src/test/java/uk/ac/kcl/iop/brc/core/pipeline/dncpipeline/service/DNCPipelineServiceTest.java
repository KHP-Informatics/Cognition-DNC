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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DNCPipelineServiceTest {

    @InjectMocks
    private DNCPipelineService service;

    @Mock
    private JsonHelper<DNCWorkCoordinate> mockJsonHelper;

    @Mock
    private DNCWorkUnitDao DNCWorkUnitDao;

    @Mock
    private PatientDao patientDao;

    @Mock
    private AnonymisationService anonymisationService;

    @Mock
    private DocumentConversionService documentConversionService;

    @Test
    public void shouldCallTextPipelineFromFileCorrectly() {
        service.setConversionFormat("text");

        List<DNCWorkCoordinate> DNCWorkCoordinates = new ArrayList<>();
        DNCWorkCoordinate cwc = new DNCWorkCoordinate();
        cwc.setPatientId(1L);
        cwc.setType("text");
        DNCWorkCoordinates.add(cwc);
        Patient patient = new Patient();
        when(mockJsonHelper.loadListFromFile(any(File.class))).thenReturn(DNCWorkCoordinates);
        when(DNCWorkUnitDao.getTextFromCoordinate(any(DNCWorkCoordinate.class))).thenReturn("val");
        when(patientDao.getPatient(1L)).thenReturn(patient);
        when(anonymisationService.anonymisePatientPlainText(patient, "val")).thenReturn("anonymised");

        service.startCreateModeWithFile("mockFile");

        verify(DNCWorkUnitDao).getTextFromCoordinate(any(DNCWorkCoordinate.class));
        verify(patientDao).getPatient(1L);
        verify(anonymisationService).anonymisePatientPlainText(patient, "val");
        verify(DNCWorkUnitDao).saveConvertedText(cwc, "anonymised");
        verify(documentConversionService, times(0)).convertToText(any(byte[].class));
        verify(documentConversionService, times(0)).convertToXHTML(any(byte[].class));
    }

    @Test
    public void shouldCallBinaryPipelineForTextFromFileCorrectly() {
        service.setConversionFormat("text");

        List<DNCWorkCoordinate> DNCWorkCoordinates = new ArrayList<>();
        DNCWorkCoordinate cwc = new DNCWorkCoordinate();
        cwc.setPatientId(1L);
        cwc.setType("binary");
        DNCWorkCoordinates.add(cwc);
        Patient patient = new Patient();
        when(mockJsonHelper.loadListFromFile(any(File.class))).thenReturn(DNCWorkCoordinates);
        when(DNCWorkUnitDao.getByteFromCoordinate(any(DNCWorkCoordinate.class))).thenReturn(new byte[1]);
        when(patientDao.getPatient(1L)).thenReturn(patient);
        when(anonymisationService.anonymisePatientPlainText(patient, "val")).thenReturn("anonymised");
        when(documentConversionService.convertToText(any(byte[].class))).thenReturn("val");

        service.startCreateModeWithFile("mockFile");

        verify(DNCWorkUnitDao).getByteFromCoordinate(any(DNCWorkCoordinate.class));
        verify(patientDao).getPatient(1L);
        verify(anonymisationService).anonymisePatientPlainText(patient, "val");
        verify(DNCWorkUnitDao).saveConvertedText(cwc, "anonymised");
        verify(documentConversionService, times(1)).convertToText(any(byte[].class));
    }

    @Test
    public void shouldCallBinaryPipelineForHTMLFromFileCorrectly() {
        service.setConversionFormat("html");

        List<DNCWorkCoordinate> DNCWorkCoordinates = new ArrayList<>();
        DNCWorkCoordinate cwc = new DNCWorkCoordinate();
        cwc.setPatientId(1L);
        cwc.setType("binary");
        DNCWorkCoordinates.add(cwc);
        Patient patient = new Patient();
        when(mockJsonHelper.loadListFromFile(any(File.class))).thenReturn(DNCWorkCoordinates);
        when(DNCWorkUnitDao.getByteFromCoordinate(any(DNCWorkCoordinate.class))).thenReturn(new byte[1]);
        when(patientDao.getPatient(1L)).thenReturn(patient);
        when(anonymisationService.anonymisePatientHTML(patient, "val")).thenReturn("anonymised");
        when(documentConversionService.convertToXHTML(any(byte[].class))).thenReturn("val");

        service.startCreateModeWithFile("mockFile");

        verify(DNCWorkUnitDao).getByteFromCoordinate(any(DNCWorkCoordinate.class));
        verify(patientDao).getPatient(1L);
        verify(anonymisationService).anonymisePatientHTML(patient, "val");
        verify(DNCWorkUnitDao).saveConvertedText(cwc, "anonymised");
        verify(documentConversionService, times(1)).convertToXHTML(any(byte[].class));
    }

}
