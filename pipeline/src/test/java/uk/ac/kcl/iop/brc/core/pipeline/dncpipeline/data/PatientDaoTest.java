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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.TimeUtil;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientCarer;

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
        patientDao.executeSQLQueryForSource("create table tblPatientCarers(first_name varchar(100), last_name varchar(100), patient_id int)");
    }

    @After
    public void dropDb() {
        patientDao.executeSQLQueryForSource("drop table tblPatient");
        patientDao.executeSQLQueryForSource("drop table tblPatientNames");
        patientDao.executeSQLQueryForSource("drop table tblPatientAddresses");
        patientDao.executeSQLQueryForSource("drop table tblPatientPhoneNumbers");
        patientDao.executeSQLQueryForSource("drop table tblPatientCarers");
    }

    @Test
    public void shouldNotTerminateWhenCarerTableIsMissing() {
        patientDao.executeSQLQueryForSource("drop table tblPatientCarers");
        patientDao.executeSQLQueryForSource("insert into tblPatient values(1, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('michael', 'gregorski', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('micha', 'gregor', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address1', 'cb4 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address2', 'cb1 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('213123', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('0778', 1)");

        Patient patient = patientDao.getPatient(1L);

        assertThat(patient.getForeNames().contains("michael"), equalTo(true));

        patientDao.executeSQLQueryForSource("create table tblPatientCarers(first_name varchar(100), last_name varchar(100), patient_id int)");
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
    public void shouldFetchPatientCarersById() {
        patientDao.executeSQLQueryForSource("insert into tblPatient values(1, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('michael', 'gregorski', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('micha', 'gregor', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address1', 'cb4 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address2', 'cb1 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('213123', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('0778', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientCarers values('Richard', 'Jackson', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientCarers values('Ismail', 'Kartoglu', 1)");

        Patient patient = patientDao.getPatient(1L);

        List<PatientCarer> carers = patient.getCarers();

        assertThat(carers.size(), equalTo(2));
        assertThat(carers.get(0).getFirstName(), equalTo("Richard"));
        assertThat(carers.get(1).getFirstName(), equalTo("Ismail"));
        assertThat(carers.get(0).getLastName(), equalTo("Jackson"));
        assertThat(carers.get(1).getLastName(), equalTo("Kartoglu"));
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
