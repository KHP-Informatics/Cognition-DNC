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

import com.google.gson.Gson;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.TableColumnModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
@Service
public class ColumnPersistenceService {

    private static Logger logger = Logger.getLogger(ColumnPersistenceService.class);

    public void createJsonMapFromTableColumnModelList(List<TableColumnModel> tableColumnModels) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(tableColumnModels);

        try {
            PrintWriter writer = new PrintWriter(new File("column_mapping.json"));
            writer.write(jsonString);
            writer.close();
        } catch (FileNotFoundException e) {
            logger.error("Error occurred while column mapping to json file.");
            e.printStackTrace();
        }
    }

    public List<TableColumnModel> getTableColumnModelsFromFile() {
        Gson gson = new Gson();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("column_mapping.json"));
            TableColumnModel[] tableColumnModels = gson.fromJson(reader, TableColumnModel[].class);
            return Arrays.asList(tableColumnModels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
