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

import com.google.gson.annotations.SerializedName;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class JsonHelperTest {

    @Test
    public void shouldLoadListOfJavaObjectsFromJsonString() {
        JsonHelper<TestModel> jsonHelper = new JsonHelper<>(TestModel[].class);
        List<TestModel> testModels = jsonHelper.loadListFromString("[" +
                "{\"testName\":\"hello\"}, " +
                "{\"testName\":\"world\"}" +
                "]");

        assertThat(testModels.size(), equalTo(2));
        assertThat(testModels.get(0).getTestName(), equalTo("hello"));
        assertThat(testModels.get(1).getTestName(), equalTo("world"));
    }

    public static class TestModel {
        @SerializedName("testName")
        private String testName;

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }
    }

}