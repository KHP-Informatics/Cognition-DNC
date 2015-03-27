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


    public List<T> loadFromFile(String path) {
        return loadListFromFile(new File(path));
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

    public List<T> loadListFromInputStream(InputStream input) {
        return loadFromReader(new InputStreamReader(input));
    }

    public List<T> loadListFromString(String jsonRules) {
        return loadFromReader(new StringReader(jsonRules));
    }

}
