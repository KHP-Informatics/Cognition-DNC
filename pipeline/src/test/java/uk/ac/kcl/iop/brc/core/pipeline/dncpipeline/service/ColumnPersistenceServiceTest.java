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
