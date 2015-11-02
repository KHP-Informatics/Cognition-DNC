/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.iop.brc.core.pipeline.common.testutils.IntegrationTest;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class CoordinatesDaoTest extends IntegrationTest {

    @Autowired
    private CoordinatesDao dao;

    @Test
    public void shouldLoadCoordinatesFromView() {
        dao.executeSQLQueryForSource("create table vwTestCoordinates(patientId int, sourceTable varchar(100), sourceColumn varchar(100), idInSourceTable int, pkColumnName varchar(100), type varchar(100), updateTime varchar(100))");
        dao.executeSQLQueryForSource("insert into vwTestCoordinates values(5, 'patientDocuments', 'binaryData', 13, 'id', 'binary', '2015-10-10')");
        
        List<DNCWorkCoordinate> result = dao.getCoordinates();
        
        assertThat(result.size(), equalTo(1));

        dao.executeSQLQueryForSource("drop table vwTestCoordinates");
    }
    
}
