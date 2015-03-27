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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.TimeUtil;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PatientDaoTest extends IntegrationTest {

    @Autowired
    private PatientDao patientDao;

    @Before
    public void initDb() {
        patientDao.executeSQLQueryForSource("create table tblPatient(ID int, nhs_no varchar(100), dob date, PRIMARY KEY (ID))");
        patientDao.executeSQLQueryForSource("create table tblPatientNames(first_name varchar(100), last_name varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("create table tblPatientAddresses(Address1 varchar(100), postcode varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("create table tblPatientPhoneNumbers(number varchar(100), patient_id int)");
    }

    @After
    public void dropDb() {
        patientDao.executeSQLQueryForSource("drop table tblPatient");
        patientDao.executeSQLQueryForSource("drop table tblPatientNames");
        patientDao.executeSQLQueryForSource("drop table tblPatientAddresses");
        patientDao.executeSQLQueryForSource("drop table tblPatientPhoneNumbers");
    }

    @Test
    public void shouldFetchPatientById() {
        patientDao.executeSQLQueryForSource("insert into tblPatient values(1, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('michael', 'gregorski', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('micha', 'gregor', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address1', 'cb4 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address2', 'cb1 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('213123', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('0778', 1)");

        Patient patient = patientDao.getPatient(1L);

        assertThat(patient.getForeNames().contains("michael"), equalTo(true));
        assertThat(patient.getSurnames().contains("gregorski"), equalTo(true));
        assertThat(patient.getNHSNumber(), equalTo("123123"));
        assertThat(patient.getPhoneNumbers().contains("0778"), equalTo(true));
        assertThat(patient.getAddresses().get(0).getAddress(), equalTo("address1"));
        assertThat(patient.getAddresses().get(0).getPostCode(), equalTo("cb4 2za"));
        assertThat(patient.getAddresses().get(1).getPostCode(), equalTo("cb1 2za"));
        assertThat(TimeUtil.getFormattedDate(patient.getDateOfBirth(), "dd/MM/yyyy"), equalTo("09/05/1990"));
    }

    @Test
    public void shouldFetchPatientsWithLimit() {
        patientDao.executeSQLQueryForSource("insert into tblPatient values(1, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into tblPatient values(2, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into tblPatient values(3, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into tblPatient values(4, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into tblPatient values(5, '123123', '1990-05-09')");

        List<Patient> patients = patientDao.getPatientsWithBetween(1, 3);

        assertThat(patients.size(), equalTo(3));
    }

    @Test
    @Ignore
    public void shouldFetchPatientFasterAfterCachingPatient() {
        patientDao.executeSQLQueryForSource("insert into tblPatient values(1, '123123', '')");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('michael', 'gregorski', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('micha', 'gregor', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address1','cb4 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address2','cb4 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('213123', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('0778', 1)");

        long start1 = System.nanoTime();
        Patient patient = patientDao.getPatient(1L);
        long end1 = System.nanoTime();
        long diff1 = end1-start1;
        long start2 = System.nanoTime();
        patient = patientDao.getPatient(1L);
        long end2 = System.nanoTime();
        long diff2 = end2-start2;

        assertTrue(diff2 <= diff1);
    }

}
