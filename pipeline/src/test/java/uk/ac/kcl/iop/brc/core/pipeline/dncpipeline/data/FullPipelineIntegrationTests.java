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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spark.utils.IOUtils;
import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DNCPipelineService;

/**
 *
 * @author rich
 */
public class FullPipelineIntegrationTests extends IntegrationTest{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FullPipelineIntegrationTests.class);
    @Autowired
    private PatientDao patientDao;
    
    @Autowired
    private DNCPipelineService service;

    @Before
    public void initDb() {
        patientDao.executeSQLQueryForSource("CREATE TABLE tblPatient(ID int, nhs_no varchar(100), dob date, PRIMARY KEY (ID))");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblPatientNames(first_name varchar(100), last_name varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblDateOfBirths(patient_id int, dob date)");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblNhsNumbers(patient_id int, nhs_no varchar(100))");        
        patientDao.executeSQLQueryForSource("CREATE TABLE tblPatientAddresses(Address1 varchar(100), postcode varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblPatientPhoneNumbers(number varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblPatientCarers(first_name varchar(100), last_name varchar(100), patient_id int)");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblTestDocs(patient_id int, doc_id int, doc LONGVARBINARY )");        
        patientDao.executeSQLQueryForSource("CREATE VIEW vwTestCoordinates AS SELECT patient_id AS patientId, 'tblTestDocs' AS sourceTable, 'doc' AS sourceColumn , doc_id AS idInSourceTable, 'doc_id' AS pkColumnName,  'binary' AS type , null AS updatetime FROM tblTestDocs");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblTestOutputCoordinate ( sourceTable VARCHAR(100), sourceColumn VARCHAR(100) , idInSourceTable INT , processedText VARCHAR(1500000) )");
        
        insertBinaries(patientDao.createSourceSession()); 
        
                
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatient values(1, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("INSERT INTO tblDateOfBirths values(1, '1990-05-09')");
        patientDao.executeSQLQueryForSource("INSERT INTO tblDateOfBirths values(1, '1990-05-10')");
        patientDao.executeSQLQueryForSource("INSERT INTO tblNhsNumbers values(1, '123123')");
        patientDao.executeSQLQueryForSource("INSERT INTO tblNhsNumbers values(1, '444444')");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientNames values('michael', 'gregorski', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientNames values('micha', 'gregor', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('address1', 'cb4 2za', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('address2', 'cb1 2za', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientPhoneNumbers values('213123', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientPhoneNumbers values('0778', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientCarers values('Richard', 'Jackson', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientCarers values('Ismail', 'Kartoglu', 1)");
        
    }

    @After
    public void dropDb() {
        patientDao.executeSQLQueryForSource("DROP TABLE tblPatient");
        patientDao.executeSQLQueryForSource("DROP TABLE tblPatientNames");
        patientDao.executeSQLQueryForSource("DROP TABLE tblDateOfBirths");
        patientDao.executeSQLQueryForSource("DROP TABLE tblNhsNumbers");
        patientDao.executeSQLQueryForSource("DROP TABLE tblPatientAddresses");
        patientDao.executeSQLQueryForSource("DROP TABLE tblPatientPhoneNumbers");
        patientDao.executeSQLQueryForSource("DROP TABLE tblPatientCarers");
        patientDao.executeSQLQueryForSource("DROP VIEW vwTestCoordinates");        
        patientDao.executeSQLQueryForSource("DROP TABLE tblTestDocs");
        patientDao.executeSQLQueryForSource("DROP TABLE tblTestOutputCoordinate");
    }
    
    @Test
    public void testTest(){
        
        service.startCreateModeWithDBView();
        
        Session session = patientDao.createSourceSession();        
        Query query = session.createSQLQuery("select * from tblTestOutputCoordinate");
        List result = query.list();        
        for(Object ob : result){
            System.out.println(ob.toString());
        }
    }

    private void insertBinaries(Session session) {        
        try {
            String sql = "INSERT INTO tblTestDocs (patient_id, doc_id, doc) VALUES(?,?,?)";
            InputStream stream = getClass().getClassLoader().getResourceAsStream("docexample.doc");
            byte[] binaryData = IOUtils.toByteArray(stream);            
            for(int i =1; i<=20;i++){
                SQLQuery query = session.createSQLQuery(sql);                
                query.setInteger(0, 1);
                query.setInteger(1, i);
                query.setBinary(2, binaryData);
                query.executeUpdate();
            }
        } catch (IOException ex) {
            Logger.getLogger(FullPipelineIntegrationTests.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }


}
