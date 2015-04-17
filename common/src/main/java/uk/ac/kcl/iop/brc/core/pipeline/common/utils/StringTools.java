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
     * @param maxDistance Levenshtein distance.
     * @return A list of substrings from the @sourceString each of which approximately matches @search.
     */
    public static List<String> getApproximatelyMatchingStringList(String sourceString, String search, int maxDistance) {
        List<String> matches = new ArrayList<>();
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
                matches.add(getCompletingString(sourceString, i, endIndex));
                i = endIndex;
            }
        }
        return matches;
    }

    public static String getCompletingString(String string, int begin, int end) {
        while ( begin > 0 && ! (string.substring(begin, begin+1).equalsIgnoreCase(" ")
                || string.substring(begin, begin+1).equalsIgnoreCase(".")
                || string.substring(begin, begin+1).equalsIgnoreCase("\n"))){
            begin -= 1;
        }

        while ( end < string.length() - 1 &&
                ! (string.substring(end, end+1).equalsIgnoreCase(" ")
                        || string.substring(end, end+1).equalsIgnoreCase(".")
                        || string.substring(end, end+1).equalsIgnoreCase("\n"))){
            end += 1;
        }

        return string.substring(begin, end).trim();
    }
}
