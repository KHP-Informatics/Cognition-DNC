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

package uk.ac.kcl.iop.brc.core.pipeline.common.utils;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class StringToolsTest {

    @Test
    public void shouldShortenString() {
        String string = "hello world this is some text " +
                "hello world this is some text hello world " +
                "this is some text hello world this is some text hello world this is some text " +
                "hello world this is some text hello world this is some text hello world this is some text" +
                "hello world this is some text hello world this is some text hello world this is some text" +
                "hello world this is some text hello world this is some text hello world this is some text";
        String shortened = StringTools.shortenString(string);

        assertTrue(string.length() > shortened.length());
    }

    @Test
    public void shouldReturnLevenshteinDistance() {
        int distance = StringTools.getLevenshteinDistance("hello", "ello");

        assertTrue(distance == 1);
    }

    @Test
    public void shouldGetApproximatelyMatchingStrings() {
        String string = "Ismail Emre Kartoglu. Ismai Emre. Ismal. My name is Is mail.";

        List<String> strings = StringTools.getApproximatelyMatchingStringList(string, "Ismail", 2);

        assertThat(strings.size(), equalTo(4));
        assertTrue(strings.contains("ismail"));
        assertTrue(strings.contains("ismai"));
        assertTrue(strings.contains("ismal"));
        assertTrue(strings.contains("is mail"));
    }

    @Test
    public void shouldCompletePartialString() {
        String string = "This is a dummy sentence. Hello world. Ismail Emre Kartoglu. Ismai Emre. Ismal. My name is Is mail.";

        String result = StringTools.getCompletingString(string, 8, 11);

        assertThat(result, equalTo("a dummy"));
    }

}
