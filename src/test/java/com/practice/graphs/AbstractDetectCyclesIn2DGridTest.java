package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Shared, implementation-agnostic behavioral coverage for the "Detect Cycles in a
 * 2D Grid" problem. Concrete subclasses supply the implementation under test via
 * {@link #solve(char[][])}, guaranteeing every implementation runs the exact same
 * cases.
 *
 * <p>A cycle requires a path of length >= 4 of equal-valued, 4-directionally
 * adjacent cells that returns to its start without immediately reusing the
 * previous cell. The smallest possible cycle is therefore a 2x2 block of one
 * value.
 *
 * <p>Note: some implementations mutate the input grid; each test builds a fresh
 * grid, so no invariance on the input is asserted here.
 */
abstract class AbstractDetectCyclesIn2DGridTest {

    /** Runs the implementation under test. */
    protected abstract boolean solve(char[][] grid);

    private boolean hasCycle(String... rows) {
        return solve(grid(rows));
    }

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        @DisplayName("Example 1: two 'a' rings around inner 'b' block -> true")
        void example1() {
            assertTrue(hasCycle(
                    "aaaa",
                    "abba",
                    "abba",
                    "aaaa"));
        }

        @Test
        @DisplayName("Example 2: single 'c' cycle -> true")
        void example2() {
            assertTrue(hasCycle(
                    "ccca",
                    "cdcc",
                    "ccec",
                    "fccc"));
        }

        @Test
        @DisplayName("Example 3: no closed loop of equal values -> false")
        void example3() {
            assertFalse(hasCycle(
                    "abb",
                    "bzb",
                    "bba"));
        }
    }

    @Nested
    @DisplayName("Degenerate shapes that can never contain a cycle")
    class NoCyclePossibleByShape {

        @Test
        @DisplayName("1x1 single cell")
        void singleCell() {
            assertFalse(hasCycle("a"));
        }

        @Test
        @DisplayName("1xN row of one value")
        void singleRowUniform() {
            assertFalse(hasCycle("aaaa"));
        }

        @Test
        @DisplayName("Nx1 column of one value")
        void singleColumnUniform() {
            assertFalse(hasCycle("a", "a", "a", "a"));
        }

        @Test
        @DisplayName("1x2 two equal cells (path length only 2)")
        void twoEqualCellsInARow() {
            assertFalse(hasCycle("aa"));
        }

        @Test
        @DisplayName("2x1 two equal cells (path length only 2)")
        void twoEqualCellsInAColumn() {
            assertFalse(hasCycle("a", "a"));
        }
    }

    @Nested
    @DisplayName("Smallest cycles (2x2 blocks)")
    class SmallestCycles {

        @Test
        @DisplayName("2x2 all equal -> true (minimal cycle of length 4)")
        void fullBlock() {
            assertTrue(hasCycle(
                    "aa",
                    "aa"));
        }

        @Test
        @DisplayName("2x2 with only three equal (L-shape) -> false")
        void lShape() {
            assertFalse(hasCycle(
                    "aa",
                    "ab"));
        }

        @Test
        @DisplayName("2x2 all distinct -> false")
        void allDistinct() {
            assertFalse(hasCycle(
                    "ab",
                    "cd"));
        }

        @Test
        @DisplayName("2x3 block all equal (contains a 2x2) -> true")
        void wideBlock() {
            assertTrue(hasCycle(
                    "aaa",
                    "aaa"));
        }
    }

    @Nested
    @DisplayName("Rings and larger cycles -> true")
    class Rings {

        @Test
        @DisplayName("3x3 ring around a different center")
        void ringAroundCenter() {
            assertTrue(hasCycle(
                    "aaa",
                    "aba",
                    "aaa"));
        }

        @Test
        @DisplayName("4x4 ring around a 2x2 hole of another value")
        void largeRectangularRing() {
            assertTrue(hasCycle(
                    "aaaa",
                    "axxa",
                    "axxa",
                    "aaaa"));
        }

        @Test
        @DisplayName("Uniform 5x5 grid is full of cycles")
        void uniformLargeGrid() {
            char[][] grid = filled(5, 5, 'a');
            assertTrue(solve(grid));
        }

        @Test
        @DisplayName("Cycle exists in one component amid other values")
        void cycleInOneOfManyComponents() {
            assertTrue(hasCycle(
                    "aab",
                    "aab",
                    "ccc"));
        }
    }

    @Nested
    @DisplayName("Connected but acyclic shapes -> false")
    class AcyclicShapes {

        @Test
        @DisplayName("U-shape (open loop)")
        void uShape() {
            assertFalse(hasCycle(
                    "aba",
                    "aba",
                    "aaa"));
        }

        @Test
        @DisplayName("S/snake shape that never closes")
        void snakeShape() {
            assertFalse(hasCycle(
                    "aaaa",
                    "xxxa",
                    "aaaa"));
        }

        @Test
        @DisplayName("Plus/star shape (tree)")
        void plusShape() {
            assertFalse(hasCycle(
                    "xax",
                    "aaa",
                    "xax"));
        }

        @Test
        @DisplayName("T-shape (tree)")
        void tShape() {
            assertFalse(hasCycle(
                    "aaa",
                    "xax",
                    "xax"));
        }

        @Test
        @DisplayName("Multiple 2-cell components, none closes")
        void thinComponents() {
            assertFalse(hasCycle(
                    "abc",
                    "abc"));
        }
    }

    @Nested
    @DisplayName("Adjacency rules: diagonals do not connect")
    class AdjacencyRules {

        @Test
        @DisplayName("Equal values touching only diagonally -> false")
        void diagonalOnly() {
            assertFalse(hasCycle(
                    "ab",
                    "ba"));
        }

        @Test
        @DisplayName("Checkerboard: no two equal neighbors -> false")
        void checkerboard() {
            assertFalse(hasCycle(
                    "abab",
                    "baba",
                    "abab",
                    "baba"));
        }
    }

    @Nested
    @DisplayName("Uniform vs all-distinct grids")
    class UniformAndDistinct {

        @Test
        @DisplayName("All distinct values -> false")
        void allDistinct3x3() {
            assertFalse(hasCycle(
                    "abc",
                    "def",
                    "ghi"));
        }

        @Test
        @DisplayName("Large uniform grid -> true")
        void largeUniform() {
            assertTrue(solve(filled(8, 8, 'z')));
        }

        @Test
        @DisplayName("Tall thin uniform grid (2 columns) with cycle -> true")
        void tallThinUniform() {
            assertTrue(solve(filled(50, 2, 'a')));
        }

        @Test
        @DisplayName("Single wide row cannot cycle regardless of length -> false")
        void longSingleRow() {
            assertFalse(solve(filled(1, 50, 'a')));
        }
    }

    @ParameterizedTest(name = "[{index}] expected={1}")
    @MethodSource("smallGridCases")
    @DisplayName("Assorted small grids")
    void assortedSmallGrids(String[] rows, boolean expected) {
        if (expected) {
            assertTrue(solve(grid(rows)));
        } else {
            assertFalse(solve(grid(rows)));
        }
    }

    static Stream<Arguments> smallGridCases() {
        return Stream.of(
                Arguments.of(new String[]{"a"}, false),
                Arguments.of(new String[]{"ab"}, false),
                Arguments.of(new String[]{"aa", "aa"}, true),
                Arguments.of(new String[]{"ab", "ba"}, false),
                Arguments.of(new String[]{"aaa", "aba", "aaa"}, true),
                Arguments.of(new String[]{"aba", "aba", "aaa"}, false),
                Arguments.of(new String[]{"aaaa", "abba", "abba", "aaaa"}, true),
                Arguments.of(new String[]{"abcd", "efgh", "ijkl"}, false),
                Arguments.of(new String[]{"aaaa", "axxa", "aaaa"}, true),
                Arguments.of(new String[]{"xax", "aaa", "xax"}, false));
    }

    private static char[][] grid(String... rows) {
        char[][] g = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            g[i] = rows[i].toCharArray();
        }
        return g;
    }

    private static char[][] filled(int rows, int cols, char value) {
        char[][] g = new char[rows][cols];
        for (char[] row : g) {
            java.util.Arrays.fill(row, value);
        }
        return g;
    }
}
