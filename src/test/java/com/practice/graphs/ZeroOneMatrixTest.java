package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Behavioral coverage for LeetCode 542. 01 Matrix.
 *
 * Constraints: 1 &lt;= m, n &lt;= 10^4; 1 &lt;= m * n &lt;= 10^4; mat[i][j] ∈ {0, 1};
 * at least one 0 is present.
 */
class ZeroOneMatrixTest {

    private final ZeroOneMatrix solution = new ZeroOneMatrix();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_singleOneSurroundedByZeros() {
            int[][] mat = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
            };
            int[][] expected = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void example2_bottomRowHasIncreasingDistances() {
            int[][] mat = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {1, 1, 1}
            };
            int[][] expected = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {1, 2, 1}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }
    }

    @Nested
    @DisplayName("Minimal grids")
    class Minimal {

        @Test
        void singleZero() {
            assertArrayEquals(new int[][]{{0}}, solution.updateMatrix(new int[][]{{0}}));
        }

        @Test
        void oneByTwo_zeroThenOne() {
            assertArrayEquals(
                    new int[][]{{0, 1}},
                    solution.updateMatrix(new int[][]{{0, 1}})
            );
        }

        @Test
        void twoByOne_oneAboveZero() {
            assertArrayEquals(
                    new int[][]{{1}, {0}},
                    solution.updateMatrix(new int[][]{{1}, {0}})
            );
        }
    }

    @Nested
    @DisplayName("All zeros")
    class AllZeros {

        @Test
        void row() {
            assertArrayEquals(
                    new int[][]{{0, 0, 0, 0}},
                    solution.updateMatrix(new int[][]{{0, 0, 0, 0}})
            );
        }

        @Test
        void column() {
            assertArrayEquals(
                    new int[][]{{0}, {0}, {0}},
                    solution.updateMatrix(new int[][]{{0}, {0}, {0}})
            );
        }

        @Test
        void square() {
            assertArrayEquals(
                    new int[][]{
                            {0, 0},
                            {0, 0}
                    },
                    solution.updateMatrix(new int[][]{
                            {0, 0},
                            {0, 0}
                    })
            );
        }
    }

    @Nested
    @DisplayName("Distance propagation")
    class DistancePropagation {

        @Test
        void onesFormChainAwayFromSingleZero() {
            int[][] mat = {
                    {0, 1, 1, 1}
            };
            int[][] expected = {
                    {0, 1, 2, 3}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void verticalChainAwayFromSingleZero() {
            int[][] mat = {
                    {1},
                    {1},
                    {1},
                    {0}
            };
            int[][] expected = {
                    {3},
                    {2},
                    {1},
                    {0}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void manhattanDistanceFromCornerZero() {
            int[][] mat = {
                    {0, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            };
            int[][] expected = {
                    {0, 1, 2},
                    {1, 2, 3},
                    {2, 3, 4}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void nearestOfTwoZerosWins() {
            int[][] mat = {
                    {0, 1, 1, 1, 0}
            };
            int[][] expected = {
                    {0, 1, 2, 1, 0}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }
    }

    @Nested
    @DisplayName("Multi-source BFS")
    class MultiSource {

        @Test
        void zerosOnOppositeCorners() {
            int[][] mat = {
                    {0, 1, 1},
                    {1, 1, 1},
                    {1, 1, 0}
            };
            int[][] expected = {
                    {0, 1, 2},
                    {1, 2, 1},
                    {2, 1, 0}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void checkerboardZeros() {
            int[][] mat = {
                    {0, 1, 0},
                    {1, 0, 1},
                    {0, 1, 0}
            };
            int[][] expected = {
                    {0, 1, 0},
                    {1, 0, 1},
                    {0, 1, 0}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void singleOneWithMultipleAdjacentZeros() {
            int[][] mat = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
            };
            int[][] expected = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }
    }

    @Nested
    @DisplayName("Larger / constraint-bound shapes")
    class LargerCases {

        @Test
        void wideRow_zeroAtLeft() {
            int cols = 100;
            int[][] mat = new int[1][cols];
            Arrays.fill(mat[0], 1);
            mat[0][0] = 0;

            int[][] expected = new int[1][cols];
            for (int c = 0; c < cols; c++) {
                expected[0][c] = c;
            }
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void tallColumn_zeroAtBottom() {
            int rows = 100;
            int[][] mat = new int[rows][1];
            for (int r = 0; r < rows; r++) {
                mat[r][0] = 1;
            }
            mat[rows - 1][0] = 0;

            int[][] expected = new int[rows][1];
            for (int r = 0; r < rows; r++) {
                expected[r][0] = rows - 1 - r;
            }
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void square_zeroOnlyAtCenter() {
            int[][] mat = {
                    {1, 1, 1},
                    {1, 0, 1},
                    {1, 1, 1}
            };
            int[][] expected = {
                    {2, 1, 2},
                    {1, 0, 1},
                    {2, 1, 2}
            };
            assertArrayEquals(expected, solution.updateMatrix(mat));
        }

        @Test
        void maxCells_sparseZeros() {
            // 100 x 100 would exceed m*n <= 10^4; use 100x100 is 10^4 exactly.
            int n = 100;
            int[][] mat = filled(n, n, 1);
            mat[0][0] = 0;
            mat[n - 1][n - 1] = 0;

            int[][] result = solution.updateMatrix(mat);
            assertEquals(0, result[0][0]);
            assertEquals(0, result[n - 1][n - 1]);
            // Corners opposite a zero, and a mid cell equidistant to both zeros.
            assertEquals(99, result[0][n - 1]);
            assertEquals(99, result[n - 1][0]);
            assertEquals(99, result[49][50]);
        }
    }

    @Nested
    @DisplayName("Does not mutate input matrix")
    class InputIntegrity {

        @Test
        void matrixUnchangedAfterCall() {
            int[][] mat = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {1, 1, 1}
            };
            int[][] copy = deepCopy(mat);
            solution.updateMatrix(mat);
            assertArrayEquals(copy, mat);
        }
    }

    private static int[][] filled(int rows, int cols, int value) {
        int[][] grid = new int[rows][cols];
        for (int[] row : grid) {
            Arrays.fill(row, value);
        }
        return grid;
    }

    private static int[][] deepCopy(int[][] grid) {
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        return copy;
    }
}
