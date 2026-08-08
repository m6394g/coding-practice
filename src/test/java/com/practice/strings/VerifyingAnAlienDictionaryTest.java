package com.practice.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behavioral coverage for LeetCode 953. Verifying an Alien Dictionary.
 *
 * Constraints: 1 &lt;= words.length &lt;= 100; 1 &lt;= words[i].length &lt;= 20;
 * order.length == 26; lowercase English letters only.
 */
class VerifyingAnAlienDictionaryTest {

    private static final String ENGLISH = "abcdefghijklmnopqrstuvwxyz";
    private static final String REVERSE = "zyxwvutsrqponmlkjihgfedcba";

    private final VerifyingAnAlienDictionary solution = new VerifyingAnAlienDictionary();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_helloBeforeLeetcode_sorted() {
            String[] words = {"hello", "leetcode"};
            String order = "hlabcdefgijkmnopqrstuvwxyz";
            assertTrue(solution.isAlienSorted(words, order));
        }

        @Test
        void example2_wordAfterWorld_unsorted() {
            String[] words = {"word", "world", "row"};
            String order = "worldabcefghijkmnpqstuvxyz";
            assertFalse(solution.isAlienSorted(words, order));
        }

        @Test
        void example3_longerWordBeforePrefix_unsorted() {
            String[] words = {"apple", "app"};
            assertFalse(solution.isAlienSorted(words, ENGLISH));
        }
    }

    @Nested
    @DisplayName("Trivial inputs")
    class Trivial {

        @Test
        void singleWord_alwaysSorted() {
            assertTrue(solution.isAlienSorted(new String[]{"anything"}, ENGLISH));
        }

        @Test
        void twoIdenticalWords() {
            assertTrue(solution.isAlienSorted(new String[]{"hi", "hi"}, ENGLISH));
        }

        @Test
        void allIdenticalWords() {
            assertTrue(solution.isAlienSorted(new String[]{"aa", "aa", "aa"}, ENGLISH));
        }

        @Test
        void singleLetterWords_sorted() {
            assertTrue(solution.isAlienSorted(new String[]{"a", "b", "c"}, ENGLISH));
        }

        @Test
        void singleLetterWords_unsorted() {
            assertFalse(solution.isAlienSorted(new String[]{"c", "a"}, ENGLISH));
        }
    }

    @Nested
    @DisplayName("Prefix relationships")
    class Prefix {

        @Test
        void shorterThenLongerExtension_sorted() {
            assertTrue(solution.isAlienSorted(new String[]{"app", "apple"}, ENGLISH));
        }

        @Test
        void longerThenShorterPrefix_unsorted() {
            assertFalse(solution.isAlienSorted(new String[]{"apple", "app"}, ENGLISH));
        }

        @Test
        void chain_prefixThenExtensions_sorted() {
            assertTrue(solution.isAlienSorted(new String[]{"a", "ab", "abc"}, ENGLISH));
        }

        @Test
        void chain_breaksOnLaterPrefixViolation() {
            // first pair OK; "abcd" then "abc" violates
            assertFalse(solution.isAlienSorted(new String[]{"a", "ab", "abcd", "abc"}, ENGLISH));
        }
    }

    @Nested
    @DisplayName("Mismatch position")
    class MismatchPosition {

        @Test
        void decidedOnFirstCharacter_sorted() {
            assertTrue(solution.isAlienSorted(new String[]{"abc", "bbc"}, ENGLISH));
        }

        @Test
        void decidedOnFirstCharacter_unsorted() {
            assertFalse(solution.isAlienSorted(new String[]{"bbc", "abc"}, ENGLISH));
        }

        @Test
        void decidedMidWord_sorted() {
            // shared "hel"; 'l' < 'p'
            assertTrue(solution.isAlienSorted(new String[]{"hello", "helpo"}, ENGLISH));
        }

        @Test
        void decidedMidWord_unsorted() {
            assertFalse(solution.isAlienSorted(new String[]{"helpo", "hello"}, ENGLISH));
        }

        @Test
        void laterCharsMustNotOverrideEarlierDecision() {
            // first char decides a < b; trailing junk must not matter
            assertTrue(solution.isAlienSorted(new String[]{"azzzz", "baaaa"}, ENGLISH));
        }
    }

    @Nested
    @DisplayName("Multi-word sequences")
    class MultiWord {

        @Test
        void fullySortedSequence() {
            assertTrue(solution.isAlienSorted(new String[]{"hello", "leetcode", "world"}, ENGLISH));
        }

        @Test
        void unsortedOnlyInLastPair() {
            // hello ≤ leetcode OK; leetcode > hello fails
            assertFalse(solution.isAlienSorted(new String[]{"hello", "leetcode", "hello"}, ENGLISH));
        }

        @Test
        void unsortedOnlyInFirstPair_laterWouldBeOk() {
            assertFalse(solution.isAlienSorted(new String[]{"world", "hello", "leetcode"}, ENGLISH));
        }
    }

    @Nested
    @DisplayName("Alien order permutations")
    class AlienOrder {

        @Test
        void reverseAlphabet_sortedUnderReverse() {
            assertTrue(solution.isAlienSorted(new String[]{"c", "b", "a"}, REVERSE));
        }

        @Test
        void reverseAlphabet_englishOrderFails() {
            assertFalse(solution.isAlienSorted(new String[]{"c", "b", "a"}, ENGLISH));
        }

        @Test
        void customOrder_hBeforeL() {
            String order = "hlabcdefgijkmnopqrstuvwxyz";
            assertTrue(solution.isAlienSorted(new String[]{"hab", "lab"}, order));
            assertFalse(solution.isAlienSorted(new String[]{"lab", "hab"}, order));
        }

        @Test
        void orderWhereZComesFirst() {
            String order = "zabcdefghijklmnopqrstuvwxy";
            assertTrue(solution.isAlienSorted(new String[]{"z", "a", "b"}, order));
            assertFalse(solution.isAlienSorted(new String[]{"a", "z"}, order));
        }
    }

    @Nested
    @DisplayName("Length boundaries")
    class LengthBoundaries {

        @Test
        void maxLengthWords_equalThenDiffer() {
            String a = "aaaaaaaaaaaaaaaaaaaa"; // 20
            String b = "aaaaaaaaaaaaaaaaaaab";
            assertTrue(solution.isAlienSorted(new String[]{a, b}, ENGLISH));
            assertFalse(solution.isAlienSorted(new String[]{b, a}, ENGLISH));
        }

        @Test
        void maxLength_identical() {
            String w = "abcdefghijklmnopqrst"; // 20
            assertTrue(solution.isAlienSorted(new String[]{w, w}, ENGLISH));
        }
    }
}
