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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {

    public static String shortenString(String string) {
        if (string == null) {
            return "";
        }
        if (string.length() < 200) {
            return string;
        }

        return string.substring(0, 100);
    }

    public static int getLevenshteinDistance(String str1, String str2) {
        return StringUtils.getLevenshteinDistance(str1, str2);
    }

    /**
     * @param sourceString Source string to search for approximately matching segments.
     * @param search String to search in @sourceString.
     * @return A list of substrings from the @sourceString each of which approximately matches @search.
     */
    public static List<String> getApproximatelyMatchingStringList(String sourceString, String search) {
        List<String> matches = new ArrayList<>();
        if (StringUtils.isBlank(search)) {
            return matches;
        }
        int maxDistance = getMaxDistance(search);
        int searchLength = search.length();
        sourceString = sourceString.toLowerCase().trim();
        search = search.toLowerCase().trim();
        for (int i = 0; i < sourceString.length(); i++) {
            int endIndex = i + searchLength;
            if (endIndex >= sourceString.length()) {
                endIndex = sourceString.length();
            }
            String substring = sourceString.substring(i, endIndex).trim();
            if (getLevenshteinDistance(substring, search) <= maxDistance) {
                String completingString = getCompletingString(sourceString, i, endIndex);
                matches.add(completingString);
                i = endIndex;
            }
        }
        return matches;
    }

    protected static int getMaxDistance(String word) {
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

        String regex = "\\w+(\\s+\\w+)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string.substring(begin, end));

        if (matcher.find()) {
            return matcher.group();
        }

        return StringUtils.EMPTY;
    }

}
