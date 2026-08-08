package com.practice.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * LeetCode 269. Alien Dictionary (Hard, Premium)
 * <p>
 * There is a new alien language that uses the English alphabet, but the order
 * of the letters is unknown. You are given a list of strings {@code words}
 * claimed to be sorted lexicographically by the alien language's rules.
 * <p>
 * Return a string of the unique letters sorted in lexicographically increasing
 * order by those rules. If the claim is incorrect (no valid order exists),
 * return {@code ""}. If multiple valid orders exist, return any of them.
 *
 * <p>Contrast with LeetCode 953 (Verifying an Alien Dictionary): there the
 * order is given and you verify the words; here the words are given as sorted
 * and you recover the order (graph + topological sort).
 */
public class AlienDictionary {

    private static final int ALPHABET = 26;

    public String alienOrder(String[] words) {
        boolean[] present = new boolean[ALPHABET];
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                present[word.charAt(i) - 'a'] = true;
            }
        }

        List<Set<Integer>> adjList = new ArrayList<>(ALPHABET);
        for (int i = 0; i < ALPHABET; i++) {
            adjList.add(new HashSet<>());
        }
        int[] indegrees = new int[ALPHABET];

        if (!addPrecedenceEdges(words, adjList, indegrees)) {
            return "";
        }
        return topologicalOrder(present, adjList, indegrees);
    }

    /**
     * Compares consecutive words and adds edges {@code prev → next} for the first
     * differing letters. Returns false if a longer word appears before its prefix.
     */
    private boolean addPrecedenceEdges(
            String[] words, List<Set<Integer>> adjList, int[] indegrees) {
        for (int i = 0; i < words.length - 1; i++) {
            String prev = words[i];
            String next = words[i + 1];
            int min = Math.min(prev.length(), next.length());
            boolean diffFound = false;
            for (int j = 0; j < min; j++) {
                int u = prev.charAt(j) - 'a';
                int v = next.charAt(j) - 'a';
                if (u != v) {
                    if (adjList.get(u).add(v)) {
                        indegrees[v]++;
                    }
                    diffFound = true;
                    break;
                }
            }
            if (!diffFound && prev.length() > next.length()) {
                return false;
            }
        }
        return true;
    }

    /** Kahn's algorithm over letters that appear in {@code words}. */
    private String topologicalOrder(
            boolean[] present, List<Set<Integer>> adjList, int[] indegrees) {
        int numChars = 0;
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < ALPHABET; i++) {
            if (!present[i]) {
                continue;
            }
            numChars++;
            if (indegrees[i] == 0) {
                queue.add(i);
            }
        }

        StringBuilder order = new StringBuilder();
        int processed = 0;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            processed++;
            order.append((char) ('a' + node));
            for (int neighbor : adjList.get(node)) {
                indegrees[neighbor]--;
                if (indegrees[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }
        return processed == numChars ? order.toString() : "";
    }
}
