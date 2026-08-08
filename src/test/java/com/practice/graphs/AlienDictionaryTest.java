package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behavioral coverage for LeetCode 269. Alien Dictionary.
 *
 * Classic constraints: lowercase letters only; return any valid order, or
 * {@code ""} if the dictionary claim is inconsistent.
 */
class AlienDictionaryTest {

    private final AlienDictionary solution = new AlienDictionary();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_classicChain() {
            String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
            String order = solution.alienOrder(words);
            assertValidOrder(words, order);
            assertEquals("wertf", order);
        }

        @Test
        void example2_twoLetters() {
            String[] words = {"z", "x"};
            String order = solution.alienOrder(words);
            assertValidOrder(words, order);
            assertEquals("zx", order);
        }

        @Test
        void example3_cycle_returnsEmpty() {
            assertEquals("", solution.alienOrder(new String[]{"z", "x", "z"}));
        }
    }

    @Nested
    @DisplayName("Trivial inputs")
    class Trivial {

        @Test
        void singleWord_anyPermutationOfItsLetters() {
            String[] words = {"abc"};
            assertValidOrder(words, solution.alienOrder(words));
        }

        @Test
        void singleLetterWords_sameLetter() {
            String[] words = {"a", "a", "a"};
            assertEquals("a", solution.alienOrder(words));
        }

        @Test
        void twoIdenticalWords() {
            String[] words = {"hello", "hello"};
            assertValidOrder(words, solution.alienOrder(words));
        }
    }

    @Nested
    @DisplayName("Valid orders")
    class Valid {

        @Test
        void linearPrecedence() {
            // a before c before b  → edges a→c, c→b
            String[] words = {"ac", "ab", "b"};
            String order = solution.alienOrder(words);
            assertValidOrder(words, order);
        }

        @Test
        void disconnectedLetters_allAppear() {
            // only a→b; c isolated
            String[] words = {"ac", "ab"};
            String order = solution.alienOrder(words);
            assertValidOrder(words, order);
            assertTrue(order.indexOf('a') < order.indexOf('b'));
            assertTrue(order.indexOf('c') >= 0);
        }

        @Test
        void duplicateEdgeFromMultiplePairs() {
            // a→b derived twice
            String[] words = {"za", "zb", "ca", "cb"};
            assertValidOrder(words, solution.alienOrder(words));
        }

        @Test
        void shorterWordBeforeExtension() {
            String[] words = {"ab", "abc"};
            assertValidOrder(words, solution.alienOrder(words));
        }
    }

    @Nested
    @DisplayName("Invalid dictionaries")
    class Invalid {

        @Test
        void longerWordBeforeItsPrefix_returnsEmpty() {
            assertEquals("", solution.alienOrder(new String[]{"abc", "ab"}));
        }

        @Test
        void directCycleTwoLetters_returnsEmpty() {
            assertEquals("", solution.alienOrder(new String[]{"ab", "ba", "a"}));
        }

        @Test
        void threeCycle_returnsEmpty() {
            // a→b, b→c, c→a
            assertEquals("", solution.alienOrder(new String[]{"a", "b", "c", "a"}));
        }
    }

    /**
     * Asserts {@code order} contains each unique letter from {@code words} exactly
     * once and respects every precedence edge implied by consecutive words.
     */
    private static void assertValidOrder(String[] words, String order) {
        Set<Character> expected = new HashSet<>();
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                expected.add(word.charAt(i));
            }
        }
        assertEquals(expected.size(), order.length(), "order length");

        Map<Character, Integer> position = new HashMap<>();
        for (int i = 0; i < order.length(); i++) {
            char c = order.charAt(i);
            assertTrue(expected.contains(c), "unexpected letter: " + c);
            assertTrue(position.put(c, i) == null, "duplicate letter: " + c);
        }
        assertEquals(expected.size(), position.size(), "missing letters");

        for (int i = 0; i < words.length - 1; i++) {
            String prev = words[i];
            String next = words[i + 1];
            int min = Math.min(prev.length(), next.length());
            boolean diffFound = false;
            for (int j = 0; j < min; j++) {
                char c1 = prev.charAt(j);
                char c2 = next.charAt(j);
                if (c1 != c2) {
                    assertTrue(position.get(c1) < position.get(c2),
                            "precedence violated: " + c1 + " must come before " + c2);
                    diffFound = true;
                    break;
                }
            }
            assertTrue(diffFound || prev.length() <= next.length(),
                    "invalid prefix ordering should have returned empty");
        }
    }
}
