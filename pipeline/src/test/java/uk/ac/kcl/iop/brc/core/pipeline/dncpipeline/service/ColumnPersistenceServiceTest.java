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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.TableColumnModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ColumnPersistenceServiceTest {

    private ColumnPersistenceService service;

    @Before
    public void init() {
        service = new ColumnPersistenceService();
    }

    @Test
    public void shouldSaveModels() {
        List<TableColumnModel> models = new ArrayList<>();
        models.add(TableColumnModel.newTableColumnModel("table1", "oldColName1", "newColName1"));
        models.add(TableColumnModel.newTableColumnModel("table1", "oldColName2", "newColName2"));
        models.add(TableColumnModel.newTableColumnModel("table2", "oldColName1", "newColName1"));

        service.createJsonMapFromTableColumnModelList(models);

        List<TableColumnModel> tableColumnModelsFromFile = service.getTableColumnModelsFromFile();

        assertThat(tableColumnModelsFromFile.size(), equalTo(3));
        assertThat(tableColumnModelsFromFile.get(1).getNewColumnName(), equalTo("newColName2"));
    }

}
