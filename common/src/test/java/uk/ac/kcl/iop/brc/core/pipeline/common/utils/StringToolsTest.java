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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
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

        Set<String> strings = StringTools.getApproximatelyMatchingStringList(string, "Ismail");

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

    @Test
    public void shouldNotIncludeParenthesesWhenCompletingPartialString() {
        String string = "This is (a dummy) sentence.";

        String result = StringTools.getCompletingString(string, 9, 11);

        assertThat(result, equalTo("a dummy"));
        assertThat(result, not(equalTo("(a dummy")));
    }

    @Test
    public void shouldCheckIfStringIsAlphaNumeric() {
        assertThat(StringUtils.isAlphanumeric("(hello"), equalTo(false));
        assertThat(StringUtils.isAlphanumeric("123hello"), equalTo(true));
    }

    @Test
    public void shouldReturnEmptyCollectionIfSearchWordIsBlank() {
        String string = "Ismail Emre Kartoglu. Ismai Emre. Ismal. My name is Is mail.";

        Set<String> strings = StringTools.getApproximatelyMatchingStringList(string, "");

        assertThat(strings.size(), equalTo(0));
    }

    @Test
    public void shouldAvoidOpeningParenthesisAsTheBeginningCharacter() {
        String string = "Ismail (Emre Kartoglu. Ismai Emre. Ismal. My name is Is mail.";

        String result = StringTools.getCompletingString(string, 6, 11);

        assertThat(result, equalTo("Emre"));
    }

    @Test
    public void shouldGetMaxLevenshteinDistanceAsFifteenPercentOfWordLength() {
        int dist = StringTools.getMaxDistance("Ismail");
        assertThat(dist, equalTo(1));

        dist = StringTools.getMaxDistance("Bob");
        assertThat(dist, equalTo(0));

        dist = StringTools.getMaxDistance("Craig");
        assertThat(dist, equalTo(1));

        dist = StringTools.getMaxDistance("07881618299");
        assertThat(dist, equalTo(2));
    }

    @Test
    public void shouldGetPostCode() {
        String string = "Ismail Emre Kartoglu. Ismai Emre. My post code is SE22 0RX.";

        Set<String> strings = StringTools.getApproximatelyMatchingStringList(string, "SE220RX");

        assertThat(strings.contains("se22 0rx"), equalTo(true));
    }

    @Test
    public void shouldIncludeMiddleParanthesis() {
        String string = "Ismail (07881) 618299. Ismai Emre. Ismal. My name is Is mail.";

        String result = StringTools.getCompletingString(string, 8, 16);

        assertThat(result, equalTo("07881) 618299"));
    }

    @Test
    public void shouldFindWindowOfGivenText() {
        String string = "I am Ismail Emre Kartoglu. My address changes. It is now 33 Marmora Road, SE22 0RX, London, UK." +
                " This is some extra text.";

        String address = "33, London, Marmora Road, SE22 0RX";

        MatchingWindow window = StringTools.getMatchingWindowsAboveThreshold(string, address, 0.5f).get(0);

        assertThat(window.getMatchingText(), equalTo("33 Marmora Road, SE22 0RX, London,"));

        assertThat(string.substring(window.getBegin(), window.getEnd()), equalTo("33 Marmora Road, SE22 0RX, London,"));
    }

    @Test
    public void shouldFindWindowOfGivenTextWhenWindowIsAtTheBorder() {
        String string = "I am Ismail Emre Kartoglu. My address changes. It is now 33 Marmora Road, SE22 0RX, London";

        String address = "33, London, Marmora Road, SE22 0RX";

        MatchingWindow window = StringTools.getMatchingWindowsAboveThreshold(string, address, 0.5f).get(0);

        assertThat(window.getMatchingText(), equalTo("33 Marmora Road, SE22 0RX, London"));

        assertThat(string.substring(window.getBegin(), window.getEnd()), equalTo("33 Marmora Road, SE22 0RX, London"));

        assertThat(window.isScoreAboveThreshold(0.6f), equalTo(true));
    }



}
