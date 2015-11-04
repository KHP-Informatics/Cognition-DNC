/*
        Cognition-DNC (Dynamic Name Concealer)         Developed by Ismail Kartoglu (https://github.com/iemre)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Biomedical Research Centre for Mental Health

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
