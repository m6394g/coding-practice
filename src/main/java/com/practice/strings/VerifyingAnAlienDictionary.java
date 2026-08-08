package com.practice.strings;

/**
 * LeetCode 953. Verifying an Alien Dictionary
 *
 * In an alien language, they use English lowercase letters with a possibly
 * different alphabet order. Given {@code words} and the alien {@code order},
 * return true if and only if the words are sorted lexicographically in that
 * language.
 *
 * Lexicographic rules match English: compare character by character using
 * alien order; if one word is a proper prefix of the other, the shorter word
 * comes first.
 */
public class VerifyingAnAlienDictionary {

    public boolean isAlienSorted(String[] words, String order) {
        int[] rank = new int[26];
        for (int i = 0; i < order.length(); i++) {
            rank[order.charAt(i) - 'a'] = i;
        }

        for (int i = 0; i < words.length - 1; i++) {
            if (!inOrder(words[i], words[i + 1], rank)) {
                return false;
            }
        }
        return true;
    }

    /** Returns true if {@code s1} is lexicographically ≤ {@code s2} under {@code rank}. */
    private boolean inOrder(String s1, String s2, int[] rank) {
        int min = Math.min(s1.length(), s2.length());
        for (int j = 0; j < min; j++) {
            char c1 = s1.charAt(j);
            char c2 = s2.charAt(j);
            if (c1 != c2) {
                return rank[c1 - 'a'] < rank[c2 - 'a'];
            }
        }
        return s1.length() <= s2.length();
    }
}
