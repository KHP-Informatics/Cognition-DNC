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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringTools {

    public static int getLevenshteinDistance(String str1, String str2) {
        return StringUtils.getLevenshteinDistance(str1, str2);
    }


    public static Set<String> getApproximatelyMatchingStringList(String sourceString, String search) {
        return getApproximatelyMatchingStringList(sourceString, search, getMaxAllowedLevenshteinDistanceFor(search));
    }

    public boolean isNotTooShort(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }

        return string.trim().length() > 3;
    }

    /**
     * @param sourceString Source string to search for approximately matching segments.
     * @param search String to search in {@code sourceString}.
     * @param maxDistance Maximum edit distance that should be satisfied.
     * @return A list of substrings from the @sourceString each of which approximately matches {@code search}.
     */
    public static Set<String> getApproximatelyMatchingStringList(String sourceString, String search, int maxDistance) {
        Set<String> matches = new HashSet<>();
        if (StringUtils.isBlank(search)) {
            return matches;
        }
        search = search.trim();

        int searchLength = search.length();
        if (searchLength <= 1) {
            return matches;
        }
        if (searchLength <= 3) {
            matches.add(search);
            return matches;
        }
        sourceString = sourceString.toLowerCase().trim();
        search = search.toLowerCase().trim();
        for (int i = 0; i < sourceString.length(); i++) {
            int endIndex = i + searchLength;
            if (endIndex >= sourceString.length()) {
                endIndex = sourceString.length();
            }
            String completingString = getCompletingString(sourceString, i, endIndex);
            if (matches.contains(completingString)) {
                continue;
            }
            if (getLevenshteinDistance(completingString, search) <= maxDistance) {
                matches.add(completingString.replace("\"", "\\\""));
                i = endIndex;
            }
        }
        return matches;
    }

    /**
     * @param word
     * @return Approximate Levenshtein distance for {@code word}.
     */
    protected static int getMaxAllowedLevenshteinDistanceFor(String word) {
        if (StringUtils.isBlank(word)) {
            return 0;
        }
        return Math.round((float)word.length()*15/100);
    }

    public static String getCompletingString(String string, int begin, int end) {
        while ( begin > 0 && StringUtils.isAlphanumeric(string.substring(begin, begin+1)) ){
            begin -= 1;
        }
        if (begin != 0)
            begin += 1;

        while ( end < string.length() - 1 && StringUtils.isAlphanumeric(string.substring(end, end + 1)) ){
            end += 1;
        }

        String regex = "\\w+(\\(?\\)?\\s+\\w+)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string.substring(begin, end));

        if (matcher.find()) {
            return matcher.group();
        }

        return StringUtils.EMPTY;
    }

    /**
     *
     * @param text The source text.
     * @param search The text to be searched in {@code text}.
     * @param threshold The threshold value between 0.0 - 1.0.
     * @return A list of MatchingWindow objects.
     */
    public static List<MatchingWindow> getMatchingWindowsAboveThreshold(String text, String search, double threshold) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        if (StringUtils.isBlank(search)) {
            return new ArrayList<>();
        }
        String[] addressWords = search.split(" ");
        int bagSize = addressWords.length;
        String[] textWords = text.split(" ");
        int textWordCount = textWords.length;
        List<MatchingWindow> windows = new ArrayList<>();
        for (int i = 0; i < textWordCount; i++) {
            MatchingWindow window = takeBag(textWords, i, bagSize);
            window.setScoreAccordingTo(addressWords);
            window.setMatchingText(text.substring(window.getBegin(), window.getEnd()));
            windows.add(window);
        }

        Collections.sort(windows);
        windows = windows.stream().filter(window -> window.isScoreAboveThreshold(threshold)).collect(Collectors.toList());

        return windows;
    }

    private static MatchingWindow takeBag(String[] textWords, int startWordIndex, int bagSize) {
        MatchingWindow window = new MatchingWindow();
        int offset = 0;
        for (int i = startWordIndex; i < startWordIndex+bagSize; i++) {
            if (i >= textWords.length) {
                break;
            }
            offset += textWords[i].length() + 1;
            window.addWord(textWords[i]);
        }
        offset -= 1;
        int begin = 0;
        for (int i = 0; i < startWordIndex; i++) {
            begin += textWords[i].length() + 1;
        }
        window.setBegin(begin);
        window.setEnd(begin + offset);

        return window;
    }

    public static Set<String> splitIntoWordsWithLengthHigherThan(String string, int minLength) {
        Set<String> strings = new HashSet<>();

        if (StringUtils.isBlank(string)) {
            return strings;
        }

        String[] splitArray = string.split(" ");
        for (String word : splitArray) {
            if (word.length() > minLength) {
                strings.add(word);
            }
        }
        return strings;
    }

    public static Set<String> splitIntoWordsWithLengthHigherThan(String string, int minLength, String... ignoreWords) {
        if (StringUtils.isBlank(string)) {
            return new HashSet<>();
        }
        for (String ignoreWord : ignoreWords) {
            string = string.replaceAll("(?i)"+ignoreWord, "");
        }
        return splitIntoWordsWithLengthHigherThan(string, minLength);
    }

    public static boolean noContentInHtml(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        try {
            Document doc = Jsoup.parse(text);

            String bodyText = doc.body().text();

            return StringUtils.isBlank(bodyText);
        } catch (Exception ex) {
            return false;
        }
    }
}
