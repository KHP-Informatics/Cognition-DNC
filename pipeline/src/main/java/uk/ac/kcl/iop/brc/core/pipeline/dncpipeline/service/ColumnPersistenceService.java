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

import com.google.gson.Gson;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.TableColumnModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
