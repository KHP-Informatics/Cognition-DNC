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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation;

import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.TimeUtil;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientAddress;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnonymisationServiceTest extends IntegrationTest {

    @Autowired
    private AnonymisationService anonymisationService;

    @Test
    public void shouldAnonymisePatient() throws ParseException {
        Patient patient = new Patient();
        patient.addForeName("Ismail");
        patient.addSurname("Kartoglu");
        patient.setNHSNumber("11111");
        PatientAddress patientAddress1 = new PatientAddress();
        patientAddress1.setAddress("addressText");
        patientAddress1.setPostCode("cb4 2za");
        PatientAddress patientAddress2 = new PatientAddress();
        patientAddress2.setAddress("addressText");
        patientAddress2.setPostCode("cb4 2za");
        patient.addAddress(patientAddress1);
        patient.addAddress(patientAddress2);
        patient.addPhoneNumber("50090051234");
        patient.addPhoneNumber("11090051234");
        patient.setDateOfBirth(TimeUtil.getDateFromString("09/05/1990", "dd/MM/yyyy"));
        String anonymisedText = anonymisationService.anonymisePatientHTML(patient, "<html>\n" +
                "<body>\n" +
                "<h>Ismail</h>\n" +
                "\n" +
                "<div>Ismail Kartoglu is our patient.</div>\n" +
                "<div>His phone number is: 32123123456 and 50090051234</div>\n" +
                "<div>His post code is cb4 2za addressText</div>\n" +
                "NHS number 11122\n Date of birth is 09/05/1990" +
                "Ism MrK\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        assertTrue(anonymisedText.contains("XXXXX"));
        assertTrue(anonymisedText.contains("Date of birth is DDDDD"));
        assertTrue(anonymisedText.contains("post code is PPPPP"));
    }

    @Test
    public void testRandomNumberGenerator() {
        System.out.println(RandomUtils.nextInt());
    }

    @Test
    public void shouldNotReplaceHtmlTags() {
        Patient patient = new Patient();
        patient.addForeName("rich");
        patient.addForeName("richard");
        patient.addSurname("Jackson");
        patient.setNHSNumber("11122");
        PatientAddress patientAddress1 = new PatientAddress();
        patientAddress1.setAddress("addressText");
        patientAddress1.setPostCode("cb4 2za");
        PatientAddress patientAddress2 = new PatientAddress();
        patientAddress2.setAddress("addressText");
        patientAddress2.setPostCode("cb4 2za");
        patient.addAddress(patientAddress1);
        patient.addAddress(patientAddress2);
        patient.addPhoneNumber("50090051234");
        patient.addPhoneNumber("11090051234");
        String anonymisedText = anonymisationService.anonymisePatientHTML(patient, "<html>\n" +
                "<body>\n" +
                "<rich>Richard RICH Jackson</rich>\n" +
                "\n" +
                "Ism MrK\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        assertTrue(anonymisedText.contains("<rich>"));
        assertTrue(anonymisedText.contains("XXXXX XXXXX XXXXX"));
    }

    @Test
    public void shouldAnonymiseFirstNameAndLastNameInDescendingOrderOfLength() {
        Patient patient = new Patient();
        patient.addForeName("rich");
        patient.addForeName("richard");
        patient.addSurname("Jack");
        patient.addSurname("Jackson");
        patient.setNHSNumber("11122");
        PatientAddress patientAddress1 = new PatientAddress();
        patientAddress1.setAddress("addressText");
        patientAddress1.setPostCode("cb4 2za");
        PatientAddress patientAddress2 = new PatientAddress();
        patientAddress2.setAddress("addressText");
        patientAddress2.setPostCode("cb4 2za");
        patient.addAddress(patientAddress1);
        patient.addAddress(patientAddress2);
        patient.addPhoneNumber("50090051234");
        patient.addPhoneNumber("11090051234");
        String anonymisedText = anonymisationService.anonymisePatientPlainText(patient, "\n" +
                "\n" +
                "Richard RICH Jackson\n" +
                "\n" +
                "Ism MrK\n" +
                "\n" +
                "\n" +
                "11122\n" +
                "50090051234");
        System.out.println(anonymisedText);
        assertTrue(anonymisedText.contains("HHHHH"));
        assertTrue(anonymisedText.contains("FFFFF"));
        assertTrue(anonymisedText.contains("XXXXX XXXXX XXXXX"));
        assertFalse(anonymisedText.contains("XXXXX XXXXX XXXXXson"));
    }

}
