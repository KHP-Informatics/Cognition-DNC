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

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class StringToolsTest {

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
        int dist = StringTools.getMaxAllowedLevenshteinDistanceFor("Ismail");
        assertThat(dist, equalTo(1));

        dist = StringTools.getMaxAllowedLevenshteinDistanceFor("Bob");
        assertThat(dist, equalTo(0));

        dist = StringTools.getMaxAllowedLevenshteinDistanceFor("Craig");
        assertThat(dist, equalTo(1));

        dist = StringTools.getMaxAllowedLevenshteinDistanceFor("07881618299");
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

    @Test
    public void shouldReturnZeroLevenshteinDistanceWhenWordIsNull() {
        assertThat(StringTools.getMaxAllowedLevenshteinDistanceFor(null), equalTo(0));
    }

    @Test
    public void shouldRoundLevenshteinDistanceUpWhenEqualToOrAboveHalf() {
        assertThat(StringTools.getMaxAllowedLevenshteinDistanceFor("department"), equalTo(2));
    }

    @Test
    public void shouldSplitIntoWordsWithLengthHigherThan() {
        Set<String> set = StringTools.splitIntoWordsWithLengthHigherThan("hello world 2 aaaaa", 3);

        assertThat(set.contains("hello"), equalTo(true));
        assertThat(set.contains("world"), equalTo(true));
        assertThat(set.contains("2"), equalTo(false));
        assertThat(set.contains("aaaaa"), equalTo(true));
    }

    @Test
    public void shouldReturnTrueIfThereIsNoTextContentInHtml() {
        String test = "<html><head><title>Test</title></head><body><div id=\"test\"><p /></div>" +
                "<div id=\"test\"><p />" +
                "</div>" +
                "<div id=\"test\"><p /></div>" +
                "<div id=\"test\"><p /></div></body></html>";

        assertThat(StringTools.noContentInHtml(test), equalTo(true));
    }

    @Test
    public void shouldReturnFalseIfThereIsNoHtmlTag() {
        String test = "this is plain text";

        assertThat(StringTools.noContentInHtml(test), equalTo(false));
    }

    @Test
    public void shouldReturnTrueIfStringIsBlank() {
        String test = "";

        assertThat(StringTools.noContentInHtml(test), equalTo(true));

    }

    @Test
    public void shouldReturnFalseIfThereIsTextContentInHtml() {
        String test = "<html><head><title>Test</title></head><body><div id=\"test\">Some content</div></body></html>";

        assertThat(StringTools.noContentInHtml(test), equalTo(false));
    }

    @Test
    public void shouldNotIncludeIgnoreWordsWhenSplittingString() {
        String test = "Some area of London. Testing ignore words.";

        Set<String> strings = StringTools.splitIntoWordsWithLengthHigherThan(test, 3, "ignore", "area", "of", "some");

        assertThat(strings.contains("London."), equalTo(true));
        assertThat(strings.contains("Testing"), equalTo(true));
        assertThat(strings.contains("some"), equalTo(false));
        assertThat(strings.contains("of"), equalTo(false));
        assertThat(strings.contains("area"), equalTo(false));
        assertThat(strings.contains("ignore"), equalTo(false));
    }

    @Test
    public void shouldReturnEmptySetWhenStringIsBlank() {
        String test = " ";

        Set<String> strings = StringTools.splitIntoWordsWithLengthHigherThan(test, 3, "ignore", "area", "of", "some");

        assertThat(strings.size(), equalTo(0));
    }

    @Test
    public void shouldGetRegexResults() {
        String test = "Some area of London. (07881 934) 43903. 34";

        List<String> strings = StringTools.getRegexMatchesWithMinLength(test, "[()0-9]+\\s*[()0-9\\s]*", 3);

        assertThat(strings.contains("(07881 934) 43903"), equalTo(true));
        assertThat(strings.contains("34"), equalTo(false));
    }

    @Test
    public void shouldGetRandomName() {
        String name1 = StringTools.getRandomForeName();
        String name2 = StringTools.getRandomForeName();

        assertThat(name1.equals(name2), equalTo(false));
    }

    @Test
    public void shouldGetTheFirstHtmlWithContent() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.DefaultParser\" />\n" +
                "<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.ocr.TesseractOCRParser\" />\n" +
                "<meta name=\"Content-Type\" content=\"image/tiff\" />\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<body><div>Replace this ﬁle with prentcsmacro . sty for your meeting,\n" +
                "or with entcsinacro.st for your meeting. Both can be\n" +
                "found at the LN l‘CS l\\= acro Home Page.\n" +
                "\n" +
                "An Example Paper\n" +
                "\n" +
                "My Name 1'2\n" +
                "\n" +
                "My Department\n" +
                "My Um‘ver.s‘-ity\n" +
                "My City, My Country\n" +
                "\n" +
                "My Co—author 3\n" +
                "\n" +
                "My Co-author's Department\n" +
                "M y C 0- auth or \"5 Umvve rs-2' ty\n" +
                "My Co—a.uthor'.s‘ City, My Co—aut/z0r's Country\n" +
                "\n" +
                "Abstract\n" +
                "\n" +
                "This is a short example to show the basics of using the TSNTCS style macro ﬁles.\n" +
                "Ample examples of how ﬁles should look may be found among the published volumes\n" +
                "of the series at the ENTCS Home Page http://www.elsevier.n1/locate/entcs.\n" +
                "\n" +
                "Key words: Please list keywords from your paper here, separated\n" +
                "by commas.\n" +
                "\n" +
                "1 Introduction\n" +
                "\n" +
                "This short note provides a guide to using the ENTCS macro package for\n" +
                "preparing papers for publication in your conference Proceedings. The Pro-\n" +
                "ceedings may be printed and hard copies distributed to participants at the\n" +
                "meeting; this is an option to Conference Organizers may choose to exercise.\n" +
                "The Proceedings also will be par of a volume in the series Electronic Notes\n" +
                "272. Theoretical Computer .S'cz'en.ce (ILNTCS), which is published under the aus-\n" +
                "pices of Elsevier Science B. V., the publishers of ’1'heoretz'cal Computer Science.\n" +
                "It’s home page is http://www.e1sevier.nl/locate/entcs\n" +
                "The l£l\\'TCS macro package consists of two ﬁles:\n" +
                "\n" +
                "entcs.c1s, the basic style ﬁle, and\n" +
                "\n" +
                "</div>\n" +
                "</body></html><html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta name=\"Compression\" content=\"Uncompressed\" />\n" +
                "<meta name=\"tiff:ImageLength\" content=\"3508\" />\n" +
                "<meta name=\"Samples Per Pixel\" content=\"4 samples/pixel\" />\n" +
                "<meta name=\"tiff:ImageWidth\" content=\"2480\" />\n" +
                "<meta name=\"tiff:YResolution\" content=\"300.0\" />\n" +
                "<meta name=\"Bits Per Sample\" content=\"8 8 8 8 bits/component/pixel\" />\n" +
                "<meta name=\"Y Resolution\" content=\"300 dots per (no unit)\" />\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<body /></html>";

        String firstHtmlWithContent = StringTools.getFirstHtmlWithContent(html);

        assertThat(firstHtmlWithContent.contains("short note provides"), equalTo(true));
        assertThat(firstHtmlWithContent.contains("Y Resolution"), equalTo(false));
    }

    @Test
    public void shouldGetMetaDataFromHTML() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.DefaultParser\" />\n" +
                "<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.ocr.TesseractOCRParser\" />\n" +
                "<meta name=\"Content-Type\" content=\"image/tiff\" />\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<body><div>Replace this ﬁle with prentcsmacro . sty for your meeting,\n" +
                "or with entcsinacro.st for your meeting. Both can be\n" +
                "found at the LN l‘CS l\\= acro Home Page.\n" +
                "\n" +
                "An Example Paper\n" +
                "</div>\n" +
                "</body></html>";

        String result = StringTools.getMetaDataFromHTML(html);

        assertThat(result, equalTo("<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.DefaultParser\"> \n<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.ocr.TesseractOCRParser\"> \n<meta name=\"Content-Type\" content=\"image/tiff\"> \n<title></title>"));
    }

    @Test
    public void shouldAppendToHtmlMeta() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.DefaultParser\" />\n" +
                "<meta name=\"X-Parsed-By\" content=\"org.apache.tika.parser.ocr.TesseractOCRParser\" />\n" +
                "<meta name=\"Content-Type\" content=\"image/tiff\" />\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<body><div>Replace this ﬁle with prentcsmacro . sty for your meeting,\n" +
                "or with entcsinacro.st for your meeting. Both can be\n" +
                "found at the LN l‘CS l\\= acro Home Page.\n" +
                "\n" +
                "An Example Paper\n" +
                "</div>\n" +
                "</body></html>";

        String result = StringTools.addMetaDataToHtml(html, "<meta name=\"Extra-Data\" content=\"this is extra stuff\" />\n");

        assertThat(result.contains("<meta name=\"Extra-Data\" content=\"this is extra stuff\">"), equalTo(true));
        assertThat(result.contains("TesseractOCRParser"), equalTo(true));
    }

}
