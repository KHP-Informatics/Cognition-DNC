/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.DNCWorkCoordinate;


public class CoordinatesDaoTest extends IntegrationTest {

    @Autowired
    private CoordinatesDao dao;

    @Before
    public void setUp() {
        dao.executeSQLQueryForSource("create table testCoordinateView(patientId int, sourceTable varchar(100), sourceColumn varchar(100), idInSourceTable int, pkColumnName varchar(100), type varchar(100), updateTime varchar(100)"); 
    }
    
    @After
    public void tearDown() {
         dao.executeSQLQueryForSource("drop table testCoordinateView"); 
    }

    @Test
    public void shouldLoadCoordinatesFromView() {  
        dao.executeSQLQueryForSource("insert into testCoordinateView values(5, 'patientDocuments', 'binaryData', 13, 'id', 'binary', '2015-10-10')");
        
        List<DNCWorkCoordinate> result = dao.list("testCoordinateView");
        
        assertThat(result.size(), equalTo(1));
    }
    
}
