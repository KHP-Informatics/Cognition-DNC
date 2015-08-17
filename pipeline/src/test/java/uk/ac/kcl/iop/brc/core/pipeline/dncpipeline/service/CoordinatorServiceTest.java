package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import uk.ac.kcl.iop.brc.core.pipeline.common.model.DNCWorkCoordinate;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CoordinatorServiceTest {

    @Test
    public void shouldGetChunkOfList() {
        CoordinatorService service = new CoordinatorService();

        List<DNCWorkCoordinate> coordinateList = new ArrayList<>();
        DNCWorkCoordinate coordinate1 = new DNCWorkCoordinate();
        coordinate1.setPatientId(1);
        DNCWorkCoordinate coordinate2 = new DNCWorkCoordinate();
        coordinate2.setPatientId(12);
        DNCWorkCoordinate coordinate3 = new DNCWorkCoordinate();
        coordinate3.setPatientId(13);
        DNCWorkCoordinate coordinate4 = new DNCWorkCoordinate();
        coordinate4.setPatientId(14);
        DNCWorkCoordinate coordinate5 = new DNCWorkCoordinate();
        coordinate5.setPatientId(15);
        DNCWorkCoordinate coordinate6 = new DNCWorkCoordinate();
        coordinate6.setPatientId(16);

        coordinateList.add(coordinate1);
        coordinateList.add(coordinate2);
        coordinateList.add(coordinate3);
        coordinateList.add(coordinate4);
        coordinateList.add(coordinate5);
        coordinateList.add(coordinate6);

        List<DNCWorkCoordinate> list = service.getChunkOfList(coordinateList, 1, 3);

        assertThat(list.get(0).getPatientId(), equalTo(12L));
        assertThat(list.get(1).getPatientId(), equalTo(13L));
        assertThat(list.get(2).getPatientId(), equalTo(14L));
    }

    @Test
    public void shouldGetTheRestIfChunkIsTooBig() {
        CoordinatorService service = new CoordinatorService();

        List<DNCWorkCoordinate> coordinateList = new ArrayList<>();
        DNCWorkCoordinate coordinate1 = new DNCWorkCoordinate();
        coordinate1.setPatientId(1);
        DNCWorkCoordinate coordinate2 = new DNCWorkCoordinate();
        coordinate2.setPatientId(12);
        DNCWorkCoordinate coordinate3 = new DNCWorkCoordinate();
        coordinate3.setPatientId(13);
        DNCWorkCoordinate coordinate4 = new DNCWorkCoordinate();
        coordinate4.setPatientId(14);
        DNCWorkCoordinate coordinate5 = new DNCWorkCoordinate();
        coordinate5.setPatientId(15);
        DNCWorkCoordinate coordinate6 = new DNCWorkCoordinate();
        coordinate6.setPatientId(16);

        coordinateList.add(coordinate1);
        coordinateList.add(coordinate2);
        coordinateList.add(coordinate3);
        coordinateList.add(coordinate4);
        coordinateList.add(coordinate5);
        coordinateList.add(coordinate6);

        List<DNCWorkCoordinate> list = service.getChunkOfList(coordinateList, 4, 4);

        assertThat(list.size(), equalTo(2));
        assertThat(list.get(0).getPatientId(), equalTo(15L));
        assertThat(list.get(1).getPatientId(), equalTo(16L));
    }

    @Test
    public void shouldReturnEmptyListIfRequestedStartPositionIsBeyondTheSizeOfTheList() {
        CoordinatorService service = new CoordinatorService();

        List<DNCWorkCoordinate> coordinateList = new ArrayList<>();
        DNCWorkCoordinate coordinate1 = new DNCWorkCoordinate();
        coordinate1.setPatientId(1);
        DNCWorkCoordinate coordinate2 = new DNCWorkCoordinate();
        coordinate2.setPatientId(12);
        DNCWorkCoordinate coordinate3 = new DNCWorkCoordinate();
        coordinate3.setPatientId(13);
        DNCWorkCoordinate coordinate4 = new DNCWorkCoordinate();
        coordinate4.setPatientId(14);
        DNCWorkCoordinate coordinate5 = new DNCWorkCoordinate();
        coordinate5.setPatientId(15);
        DNCWorkCoordinate coordinate6 = new DNCWorkCoordinate();
        coordinate6.setPatientId(16);

        coordinateList.add(coordinate1);
        coordinateList.add(coordinate2);
        coordinateList.add(coordinate3);
        coordinateList.add(coordinate4);
        coordinateList.add(coordinate5);
        coordinateList.add(coordinate6);

        List<DNCWorkCoordinate> list = service.getChunkOfList(coordinateList, 7, 4);

        assertThat(CollectionUtils.isEmpty(list), equalTo(true));
    }
}