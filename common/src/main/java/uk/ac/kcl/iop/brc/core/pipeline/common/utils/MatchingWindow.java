package uk.ac.kcl.iop.brc.core.pipeline.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MatchingWindow implements Comparable<MatchingWindow> {

    private float score = 0;
    private int begin;
    private int end;
    private String matchingText;
    private List<String> wordSet = new ArrayList<>();

    public MatchingWindow(){}

    public String getMatchingText() {
        return matchingText;
    }

    public void addWord(String word) {
        wordSet.add(word);
    }
    public List<String> getWordSet() {
        return wordSet;
    }

    public void setWordSet(List<String> wordSet) {
        this.wordSet = wordSet;
    }

    public void setScoreAccordingTo(String[] addressWords) {
        int match = 0;
        for (String word : wordSet) {
            for (String addressWord : addressWords) {
                if (StringTools.getLevenshteinDistance(word, addressWord) <= 2) {
                    match += 1;
                    break;
                }
            }
        }
        score = (float) match/addressWords.length;
    }

    @Override
    public int compareTo(MatchingWindow o) {
        if (score < o.score) {
            return 1;
        }
        if (score > o.score) {
            return -1;
        }
        return 0;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isScoreAboveThreshold(double threshold) {
        return score >= threshold;
    }

    public void setMatchingText(String matchingText) {
        this.matchingText = matchingText;
    }

}
