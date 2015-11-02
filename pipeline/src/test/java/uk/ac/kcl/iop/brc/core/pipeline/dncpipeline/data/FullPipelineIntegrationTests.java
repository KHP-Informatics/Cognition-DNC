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
        //patientDao.executeSQLQueryForSource("create table vwTestCoordinates(patientId int, sourceTable varchar(100), sourceColumn varchar(100), idInSourceTable int, pkColumnName varchar(100), type varchar(100), updateTime varchar(100))");
        patientDao.executeSQLQueryForSource("CREATE VIEW vwTestCoordinates AS SELECT patient_id AS patientId, 'tblTestDocs' AS sourceTable, 'doc' AS sourceColumn , doc_id AS idInSourceTable, 'doc_id' AS pkColumnName,  'binary' AS type , null AS updatetime FROM tblTestDocs");
        patientDao.executeSQLQueryForSource("CREATE TABLE tblTestOutputCoordinate ( sourceTable VARCHAR(100), sourceColumn VARCHAR(100) , idInSourceTable INT , processedText VARCHAR(1500000) )");
        
        insertBinaries(patientDao.createSourceSession()); 
        
//         patientDao.executeSQLQueryForSource("INSERT INTO vwTestCoordinates " +
//        "(      patientId,      sourceTable,    sourceColumn,   idInSourceTable,    pkColumnName,   type ) " +
//        "SELECT patient_id ,    'tblTestDocs' , 'doc' ,         doc_id ,            'doc_id' ,      'binary' AS type  FROM tblTestDocs");
     
        //patient 1
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatient values(1, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientNames values('Bart', 'Davidson', 1)");       
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('61 Basildon Way', 'AL64 9HT', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('East Croyhurst', 'AL64 9HT', 1)");        
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('Angelton', 'AL64 9HT', 1)");            
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientCarers values('Pauline', 'Smith', 1)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientCarers values('Paul', 'Wayne', 1)");        
        
        //patient 2
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatient values(2, '123123', '1990-05-09')");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientNames values('David', 'Harleyson', 2)");       
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('82a SaltFarm Crescent', 'HF93 9HS', 2)");
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('Ditherington', null , 2)");        
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientAddresses values('Sussex', null, 2)");            
        patientDao.executeSQLQueryForSource("INSERT INTO tblPatientCarers values('Simon', 'Presley', 2)");
        
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
        //patientDao.executeSQLQueryForSource("DROP TABLE vwTestCoordinates");                
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
            InputStream stream = getClass().getClassLoader().getResourceAsStream("pat_id_1.pdf");
            byte[] binaryData = IOUtils.toByteArray(stream);            
            for(int i =1; i<=5;i++){
                SQLQuery query = session.createSQLQuery(sql);                
                query.setInteger(0, 1);
                query.setInteger(1, i);
                query.setBinary(2, binaryData);
                query.executeUpdate();
            }
            stream = getClass().getClassLoader().getResourceAsStream("pat_id_1.doc");
            binaryData = IOUtils.toByteArray(stream);            
            for(int i =1; i<=5;i++){
                SQLQuery query = session.createSQLQuery(sql);                
                query.setInteger(0, 1);
                query.setInteger(1, i);
                query.setBinary(2, binaryData);
                query.executeUpdate();
            }
            stream = getClass().getClassLoader().getResourceAsStream("pat_id_1.docx");
            binaryData = IOUtils.toByteArray(stream);            
            for(int i =1; i<=5;i++){
                SQLQuery query = session.createSQLQuery(sql);                
                query.setInteger(0, 1);
                query.setInteger(1, i);
                query.setBinary(2, binaryData);
                query.executeUpdate();
            }            
            stream = getClass().getClassLoader().getResourceAsStream("pat_id_2.pdf");
            binaryData = IOUtils.toByteArray(stream);            
            for(int i =1; i<=5;i++){
                SQLQuery query = session.createSQLQuery(sql);                
                query.setInteger(0, 1);
                query.setInteger(1, i);
                query.setBinary(2, binaryData);
                query.executeUpdate();
            }
            stream = getClass().getClassLoader().getResourceAsStream("pat_id_2.doc");
            binaryData = IOUtils.toByteArray(stream);            
            for(int i =1; i<=5;i++){
                SQLQuery query = session.createSQLQuery(sql);                
                query.setInteger(0, 1);
                query.setInteger(1, i);
                query.setBinary(2, binaryData);
                query.executeUpdate();
            }
            stream = getClass().getClassLoader().getResourceAsStream("pat_id_2.docx");
            binaryData = IOUtils.toByteArray(stream);            
            for(int i =1; i<=5;i++){
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
