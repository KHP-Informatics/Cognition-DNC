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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation;

import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.TimeUtil;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientAddress;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientCarer;

import java.text.ParseException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnonymisationServiceTest extends IntegrationTest {

    @Autowired
    private AnonymisationService anonymisationService;

    @Test
    public void shouldAnonymisePatientCarer() throws ParseException {
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
        patient.addCarer(new PatientCarer("xyz", "abc"));
        String anonymisedText = anonymisationService.anonymisePatientHTML(patient, "<html>\n" +
                "<body>\n" +
                "<h>Ismail</h>\n" +
                "\n" +
                "<div>Ismail Kartoglu is our patient. His carer is XYZ ABC</div>\n" +
                "<div>His phone number is: 32123123456 and 50090051234</div>\n" +
                "<div>His post code is cb4 2za addressText</div>\n" +
                "NHS number 11122\n Date of birth is 09/05/1990" +
                "Ism MrK\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        assertTrue(anonymisedText.contains("carer is YYYYY YYYYY"));
    }

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
                "NHS number 11122\n Date of birth is 09/05/1990 09th May " +
                "Ism MrK\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        assertTrue(anonymisedText.contains("XXXXX"));
        assertTrue(anonymisedText.contains("Date of birth is DDDDD"));
        assertTrue(anonymisedText.contains("post code is PPPPP"));
        assertFalse(anonymisedText.contains("09th May"));
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
                "(0500 90051234 " +
                "Some random text that shouldn't be anonymised.");
        System.out.println(anonymisedText);
        assertTrue(anonymisedText.contains("HHHHH"));
        assertTrue(anonymisedText.contains("FFFFF"));
        assertTrue(anonymisedText.contains("XXXXX XXXXX XXXXX"));
        assertTrue(anonymisedText.contains("Some random text that shouldn't be anonymised."));
        assertFalse(anonymisedText.contains("XXXXX XXXXX XXXXXson"));
    }

    @Test
    public void shouldAnonymisePartiallyMatchingAddress() {
        Patient patient = new Patient();
        patient.addForeName("rich");
        patient.addForeName("richard");
        patient.addSurname("Jack");
        patient.addSurname("Jackson");
        patient.setNHSNumber("11122");
        PatientAddress patientAddress1 = new PatientAddress();
        patientAddress1.setAddress("Kidderpore Avenue Hampstead, London");
        patientAddress1.setPostCode("cb4 2za");
        patient.addAddress(patientAddress1);
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
                "(0500) 9005 1234 " +
                "Some random text that shouldn't be anonymised." +
                "" +
                "Address is Kidderpore Ave, (Hampstead, London.");
        System.out.println(anonymisedText);
        assertTrue(anonymisedText.contains("HHHHH"));
        assertTrue(anonymisedText.contains("FFFFF"));
        assertTrue(anonymisedText.contains("XXXXX XXXXX XXXXX"));
        assertTrue(anonymisedText.contains("Some random text that shouldn't be anonymised."));
        assertFalse(anonymisedText.contains("XXXXX XXXXX XXXXXson"));
        assertTrue(anonymisedText.contains("Address is AAAAA"));
    }

    @Test
    public void shouldWorkInPresenceOfDoubleQuotationMarks() {
        Patient patient = new Patient();
        patient.addForeName("rich");
        patient.addForeName("richard");
        patient.addSurname("Jack");
        patient.addSurname("Jackson");
        patient.setNHSNumber("11122");
        PatientAddress patientAddress1 = new PatientAddress();
        patientAddress1.setAddress("Kidderpore Avenue Hampstead, London");
        patientAddress1.setPostCode("cb4 2za");
        patient.addAddress(patientAddress1);
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
                "(0500) 9005 1234 " +
                "Some random text that shouldn't be anonymised." +
                "" +
                "Address is Kidderpore Ave, (Hampstead, London.");
        System.out.println(anonymisedText);
        assertTrue(anonymisedText.contains("HHHHH"));
        assertTrue(anonymisedText.contains("FFFFF"));
        assertTrue(anonymisedText.contains("XXXXX XXXXX XXXXX"));
        assertTrue(anonymisedText.contains("Some random text that shouldn't be anonymised."));
        assertFalse(anonymisedText.contains("XXXXX XXXXX XXXXXson"));
        assertTrue(anonymisedText.contains("Address is AAAAA"));
    }

}
