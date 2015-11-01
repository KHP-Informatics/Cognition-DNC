/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spark.utils.IOUtils;
import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;

/**
 *
 * @author rich
 */
public class FullPipelineIntegrationTests extends IntegrationTest{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FullPipelineIntegrationTests.class);
    @Autowired
    private PatientDao patientDao;

    @Before
    public void initDb() {
        patientDao.executeSQLQueryForSource("create table tblPatient(ID int, nhs_no varchar(100), dob date, PRIMARY KEY (ID))");
        patientDao.executeSQLQueryForSource("create table tblPatientNames(first_name varchar(100), last_name varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("create table dateOfBirths(patient_id int, dob date)");
        patientDao.executeSQLQueryForSource("create table nhsNumbers(patient_id int, nhs_no varchar(100))");        
        patientDao.executeSQLQueryForSource("create table tblPatientAddresses(Address1 varchar(100), postcode varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("create table tblPatientPhoneNumbers(number varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("create table tblPatientCarers(first_name varchar(100), last_name varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("create table testDocs(patient_id int, doc_id int, doc VARBINARY(100) )");        
        patientDao.executeSQLQueryForSource("create view testCoordinateView AS SELECT patient_id, 'testDocs', 'doc', doc_id, 'binary',null FROM testDocs");

        insertBinaries(patientDao.createSourceSession()); 
        
                
        patientDao.executeSQLQueryForSource("insert into tblPatient values(1, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into dateOfBirths values(1, '1990-05-09')");
        patientDao.executeSQLQueryForSource("insert into dateOfBirths values(1, '1990-05-10')");
        patientDao.executeSQLQueryForSource("insert into nhsNumbers values(1, '123123')");
        patientDao.executeSQLQueryForSource("insert into nhsNumbers values(1, '444444')");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('michael', 'gregorski', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientNames values('micha', 'gregor', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address1', 'cb4 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientAddresses values('address2', 'cb1 2za', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('213123', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientPhoneNumbers values('0778', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientCarers values('Richard', 'Jackson', 1)");
        patientDao.executeSQLQueryForSource("insert into tblPatientCarers values('Ismail', 'Kartoglu', 1)");
        
    }

    @After
    public void dropDb() {
        patientDao.executeSQLQueryForSource("drop table tblPatient");
        patientDao.executeSQLQueryForSource("drop table tblPatientNames");
        patientDao.executeSQLQueryForSource("drop table tblPatientAddresses");
        patientDao.executeSQLQueryForSource("drop table tblPatientPhoneNumbers");
        patientDao.executeSQLQueryForSource("drop table tblPatientCarers");
        patientDao.executeSQLQueryForSource("drop table dateOfBirths");
        patientDao.executeSQLQueryForSource("drop table nhsNumbers");
    }
    
    @Test
    public void testTest(){
        
    }

    private void insertBinaries(Session session) {        
        try {
            String sql = "INSERT INTO testDocs (patient_id, doc_id, doc) VALUES(?,?,?)";
            SQLQuery query = session.createSQLQuery(sql);
            InputStream stream = getClass().getClassLoader().getResourceAsStream("docexample.doc");
            byte[] binaryData = IOUtils.toByteArray(stream);
            for(int i =1; i<=20;i++){
                query.setInteger(0, 1);
                query.setInteger(1, i);
                query.setBinary(2, binaryData);
            }
        } catch (IOException ex) {
            Logger.getLogger(FullPipelineIntegrationTests.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }


}
