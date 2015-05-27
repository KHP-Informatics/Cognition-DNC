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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class PatientTest {

    @Test
    public void shouldConvertJsonToPatient() {
        Patient patient = new Patient();
        patient.addForeName("Ismail");
        patient.addSurname("Kartoglu");
        patient.addNhsNumber("111");
        PatientAddress patientAddress1 = new PatientAddress();
        patientAddress1.setAddress("addressText");
        patientAddress1.setPostCode("cb4 2za");
        PatientAddress patientAddress2 = new PatientAddress();
        patientAddress2.setAddress("addressText");
        patientAddress2.setPostCode("cb4 2za");
        patient.addAddress(patientAddress1);
        patient.addAddress(patientAddress2);
        patient.addPhoneNumber("5009005");
        patient.addPhoneNumber("32123");

        String json = patient.toJson();

        assertTrue(json.contains("{"));
        assertTrue(json.contains("}"));
        assertTrue(json.contains("\"foreNames\":[\"Ismail\"]"));
        assertTrue(json.contains("\"surnames\":[\"Kartoglu\"]"));
    }

    @Test
    public void shouldGetNamesInDescendingLengthOrder() {
        Patient patient = new Patient();
        patient.addForeName("Emre");
        patient.addForeName("Ismail");

        List<String> names = patient.getForeNamesInDescendingLengthOrder();

        assertThat(names.get(0), equalTo("Ismail"));
    }

    @Test
    public void shouldSeparateNamesWithMultipleWords() {
        Patient patient = new Patient();
        patient.addForeName("Michael Gregorski");

        List<String> names = patient.getSeparatedForeNames();

        assertThat(names.get(0), equalTo("Michael"));
        assertThat(names.get(1), equalTo("Gregorski"));
    }

    @Test
    public void shouldSeparateSurnamesWithMultipleWords() {
        Patient patient = new Patient();
        patient.addSurname("Michael Gregorski");

        List<String> names = patient.getSeparatedSurnames();

        assertThat(names.get(0), equalTo("Michael"));
        assertThat(names.get(1), equalTo("Gregorski"));
    }

    @Test
    public void shouldReturnEmptyListWhenNamesListIsNull() {
        Patient patient = new Patient();
        patient.setForeNames(null);
        patient.setSurnames(null);

        List<String> names = patient.getSeparatedForeNames();
        List<String> surnames = patient.getSeparatedForeNames();

        assertThat(names.size(), equalTo(0));
        assertThat(surnames.size(), equalTo(0));
    }

    @Test
    public void shouldSplitForeNamesWhenThereIsDash() {
        Patient patient = new Patient();
        List<String> forenames = Arrays.asList("Richard-Jackson");
        patient.setForeNames(forenames);

        List<String> separatedForeNames = patient.getForeNamesInDescendingLengthOrder();

        assertThat(separatedForeNames.contains("Richard-Jackson"), equalTo(true));
        assertThat(separatedForeNames.contains("Richard"), equalTo(true));
        assertThat(separatedForeNames.contains("Jackson"), equalTo(true));
    }

    @Test
    public void shouldSplitLastNamesWhenThereIsDash() {
        Patient patient = new Patient();
        List<String> surnames = Arrays.asList("Richard-Jackson");
        patient.setSurnames(surnames);

        List<String> separatedSurnames = patient.getLastNamesInDescendingLengthOrder();

        assertThat(separatedSurnames.contains("Richard-Jackson"), equalTo(true));
        assertThat(separatedSurnames.contains("Richard"), equalTo(true));
        assertThat(separatedSurnames.contains("Jackson"), equalTo(true));
    }

    @Test
    public void shouldNotAddNamesWithLessThanFourCharacters() {
        Patient patient = new Patient();
        List<String> surnames = Arrays.asList("Richard-The");
        patient.setSurnames(surnames);

        List<String> separatedSurnames = patient.getLastNamesInDescendingLengthOrder();

        assertThat(separatedSurnames.contains("Richard-The"), equalTo(true));
        assertThat(separatedSurnames.contains("The"), equalTo(false));
    }

}
