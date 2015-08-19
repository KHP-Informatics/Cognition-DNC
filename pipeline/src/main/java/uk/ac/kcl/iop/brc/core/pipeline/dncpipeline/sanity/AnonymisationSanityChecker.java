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


package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.sanity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.TimeUtil;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientAddress;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation.AnonymisationService;

import javax.annotation.PostConstruct;
import java.text.ParseException;

import static junit.framework.TestCase.assertFalse;

@Component
public class AnonymisationSanityChecker {

    @Autowired
    private AnonymisationService anonymisationService;

    @PostConstruct
    public void checkBasicAnonymisationRules() throws ParseException {
        Patient patient = new Patient();
        patient.addForeName("TestName1");
        patient.addForeName("TestName2");
        patient.addSurname("TestLastName1");
        patient.addSurname("TestLastName1");
        patient.addNhsNumber("11122");
        PatientAddress patientAddress1 = new PatientAddress();
        patientAddress1.setAddress("Kidderpore Avenue Hampstead, London");
        patientAddress1.setPostCode("cb4 2za");
        PatientAddress patientAddress2 = new PatientAddress();
        patientAddress2.setAddress("addressText");
        patientAddress2.setPostCode("cb4 2za");
        patient.addAddress(patientAddress1);
        patient.addAddress(patientAddress2);
        patient.addPhoneNumber("50090051234");
        patient.addPhoneNumber("11090051234");
        patient.addDateOfBirth(TimeUtil.getDateFromString("09/05/1990", "dd/MM/yyyy"));

        String anonymisedText = anonymisationService.pseudonymisePersonPlainText(patient, "\n" +
                "\n" +
                "TestName1 TestName2 TestLastName1 TestLastName2\n" +
                "\n" +
                "\n" +
                "11122\n" +
                "50090051234" +
                "\n 09/05/1990" + "\n"
                + "cb42za"
                + " Some random text that should not be anonymised. \n" +
                "Address is Kidderpore Ave, (Hampstead, London.");
        System.out.println(anonymisedText);
        if (anonymisedText.contains("TestName1") || anonymisedText.contains("TestName2")) {
            throw new AssertionError("First name pseudonymisation is not working! Please check config/anonymisation/nameRules");
        }
        if (anonymisedText.contains("TestLastName1") || anonymisedText.contains("TestLastName2")) {
            throw new AssertionError("Last name pseudonymisation is not working! Please check config/anonymisation/nameRules");
        }
        if (anonymisedText.contains("11122")) {
            throw new AssertionError("NHS Number pseudonymisation is not working! Please check config/anonymisation/nhsIdRules");
        }
        if (anonymisedText.contains("50090051234")) {
            throw new AssertionError("Phone number pseudonymisation is not working! Please check config/anonymisation/phoneRules");
        }
        if (anonymisedText.contains("09/05/1990")) {
            throw new AssertionError("Date of birth pseudonymisation is not working! Please check config/anonymisation/dateOfBirthRules");
        }
        if (anonymisedText.contains("cb42za")) {
            throw new AssertionError("Post code pseudonymisation is not working! Please check config/anonymisation/addressRules");
        }
        if (! anonymisedText.contains("Some random text that should not be anonymised.")) {
            throw new AssertionError("Pseudonymisation rules anonymise everything? Please check your rules!");
        }

        if (! anonymisedText.contains("Address is AAAAA")) {
            throw new AssertionError("Approximate address pseudonymisation is not working. Please check config/anonymisation/addressRules!");
        }
    }

}
