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
