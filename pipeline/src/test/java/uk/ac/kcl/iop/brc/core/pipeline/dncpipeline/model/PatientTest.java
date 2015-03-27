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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import org.junit.Test;

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
        patient.setNHSNumber("111");
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

}
