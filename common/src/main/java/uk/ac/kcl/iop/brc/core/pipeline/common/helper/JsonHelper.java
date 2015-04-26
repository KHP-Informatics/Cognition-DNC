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

package uk.ac.kcl.iop.brc.core.pipeline.common.helper;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonHelper<T> {

    private Class clazz;

    public JsonHelper(Class clazz) {
        this.clazz = clazz;
    }


    /**
     * Given a File object, this method converts the jSON in the file to a list of Java objects.
     * @param file The File whose contents to be converted to a List of Java objects.
     * @return The list of Java objects with type T.
     */
    public List<T> loadListFromFile(File file) {
        try {
            return loadFromReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<T> loadFromReader(Reader reader) {
        Gson gson = new Gson();

        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(reader);
        JsonReader jsonReader = new JsonReader(bufferedReader);
        jsonReader.setLenient(true);
        if (! clazz.isArray()) {
            Object data = gson.fromJson(jsonReader, clazz);
            T object = (T) data;
            return Arrays.asList(object);
        }

        Object[] data = gson.fromJson(jsonReader, clazz);
        return Arrays.asList((T[]) data);
    }

    public List<T> loadListFromString(String jsonRules) {
        return loadFromReader(new StringReader(jsonRules));
    }

}
