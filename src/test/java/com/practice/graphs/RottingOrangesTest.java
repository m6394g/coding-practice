package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Exhaustive behavioral coverage for Rotting Oranges within constraints:
 * 1 &lt;= m, n &lt;= 10; grid[i][j] ∈ {0, 1, 2}.
 *
 * Note: enumerating every legal grid is infeasible (up to 3^100). These tests
 * cover all problem examples, all 1x1 grids, representative small grids,
 * connectivity/isolation edges, multi-source parallelism, and constraint-bound
 * best/worst cases.
 */
class RottingOrangesTest {

    private final RottingOranges solution = new RottingOranges();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_spreadsInFourMinutes() {
            int[][] grid = {
                    {2, 1, 1},
                    {1, 1, 0},
                    {0, 1, 1}
            };
            assertEquals(4, solution.orangesRotting(grid));
        }

        @Test
        void example2_unreachableFreshReturnsMinusOne() {
            int[][] grid = {
                    {2, 1, 1},
                    {0, 1, 1},
                    {1, 0, 1}
            };
            assertEquals(-1, solution.orangesRotting(grid));
        }

        @Test
        void example3_noFreshAtStartReturnsZero() {
            assertEquals(0, solution.orangesRotting(new int[][]{{0, 2}}));
        }
    }

    @Nested
    @DisplayName("All 1x1 grids (full enumeration under min size)")
    class SingleCell {

        @Test
        void emptyCell() {
            assertEquals(0, solution.orangesRotting(new int[][]{{0}}));
        }

        @Test
        void singleFresh_impossible() {
            assertEquals(-1, solution.orangesRotting(new int[][]{{1}}));
        }

        @Test
        void singleRotten_alreadyDone() {
            assertEquals(0, solution.orangesRotting(new int[][]{{2}}));
        }
    }

    @Nested
    @DisplayName("Best case: answer is 0")
    class BestCaseZeroMinutes {

        @Test
        void allEmpty_1xN() {
            assertEquals(0, solution.orangesRotting(new int[][]{{0, 0, 0, 0}}));
        }

        @Test
        void allEmpty_Nx1() {
            assertEquals(0, solution.orangesRotting(new int[][]{{0}, {0}, {0}}));
        }

        @Test
        void allRotten() {
            assertEquals(0, solution.orangesRotting(new int[][]{
                    {2, 2},
                    {2, 2}
            }));
        }

        @Test
        void mixOfEmptyAndRotten_noFresh() {
            assertEquals(0, solution.orangesRotting(new int[][]{
                    {2, 0, 2},
                    {0, 2, 0}
            }));
        }

        @Test
        void maxGrid_allEmpty() {
            assertEquals(0, solution.orangesRotting(filled(10, 10, 0)));
        }

        @Test
        void maxGrid_allRotten() {
            assertEquals(0, solution.orangesRotting(filled(10, 10, 2)));
        }
    }

    @Nested
    @DisplayName("Impossible: fresh never reached")
    class Impossible {

        @Test
        void allFresh_noRottenSource() {
            assertEquals(-1, solution.orangesRotting(new int[][]{
                    {1, 1},
                    {1, 1}
            }));
        }

        @Test
        void freshSeparatedByEmpty() {
            assertEquals(-1, solution.orangesRotting(new int[][]{{2, 0, 1}}));
        }

        @Test
        void freshInCornerBlockedDiagonallyOnly_notAllowed() {
            // Rotting is 4-directional; diagonal adjacency does not count.
            int[][] grid = {
                    {2, 0},
                    {0, 1}
            };
            assertEquals(-1, solution.orangesRotting(grid));
        }

        @Test
        void bottomLeftIsolatedLikeExample2() {
            int[][] grid = {
                    {2, 1, 1},
                    {0, 1, 1},
                    {1, 0, 1}
            };
            assertEquals(-1, solution.orangesRotting(grid));
        }

        @Test
        void freshSurroundedByEmpty() {
            int[][] grid = {
                    {0, 1, 0},
                    {0, 0, 0},
                    {0, 2, 0}
            };
            assertEquals(-1, solution.orangesRotting(grid));
        }

        @Test
        void maxGrid_allFresh_noRotten() {
            assertEquals(-1, solution.orangesRotting(filled(10, 10, 1)));
        }

        @Test
        void maxGrid_rottenCannotCrossEmptyWall() {
            int[][] grid = filled(10, 10, 1);
            // Vertical empty wall at column 5 splits left (has rotten) from right (fresh only).
            for (int r = 0; r < 10; r++) {
                grid[r][5] = 0;
            }
            grid[0][0] = 2;
            assertEquals(-1, solution.orangesRotting(grid));
        }
    }

    @Nested
    @DisplayName("Minimal positive spreads")
    class MinimalSpreads {

        @Test
        void oneStep_horizontal() {
            assertEquals(1, solution.orangesRotting(new int[][]{{2, 1}}));
        }

        @Test
        void oneStep_vertical() {
            assertEquals(1, solution.orangesRotting(new int[][]{{2}, {1}}));
        }

        @Test
        void oneStep_fourNeighbors() {
            int[][] grid = {
                    {0, 1, 0},
                    {1, 2, 1},
                    {0, 1, 0}
            };
            assertEquals(1, solution.orangesRotting(grid));
        }

        @Test
        void twoSteps_cornerStart() {
            int[][] grid = {
                    {1, 1},
                    {1, 2}
            };
            assertEquals(2, solution.orangesRotting(grid));
        }
    }

    @Nested
    @DisplayName("Multi-source parallelism")
    class MultiSource {

        @Test
        void twoRotten_meetInOneMinute() {
            assertEquals(1, solution.orangesRotting(new int[][]{{2, 1, 1, 2}}));
        }

        @Test
        void twoRotten_farEnds_linear() {
            // Distance between ends is 5 cells of fresh: indices 1..5 on length 7.
            // From both ends, max time = ceil(5/2) = 3? Grid: 2,1,1,1,1,1,2
            // Fresh at col1 from left:1, col2:2, col3: from both ends min(3,3)=3
            assertEquals(3, solution.orangesRotting(new int[][]{{2, 1, 1, 1, 1, 1, 2}}));
        }

        @Test
        void rottenOnAllFourCorners_centerFresh() {
            int[][] grid = {
                    {2, 1, 2},
                    {1, 1, 1},
                    {2, 1, 2}
            };
            // Center is adjacent to edge fresh that rot at t=1, so center at t=2.
            assertEquals(2, solution.orangesRotting(grid));
        }
    }

    @Nested
    @DisplayName("Linear / Manhattan distance timing")
    class DistanceTiming {

        @Test
        void rowChain_length4() {
            assertEquals(3, solution.orangesRotting(new int[][]{{2, 1, 1, 1}}));
        }

        @Test
        void columnChain_length4() {
            assertEquals(3, solution.orangesRotting(new int[][]{
                    {2},
                    {1},
                    {1},
                    {1}
            }));
        }

        @Test
        void snakeAroundEmpty_stillReachable() {
            int[][] grid = {
                    {2, 1, 0},
                    {0, 1, 0},
                    {0, 1, 1}
            };
            assertEquals(4, solution.orangesRotting(grid));
        }
    }

    @Nested
    @DisplayName("Worst case under constraints (m,n <= 10)")
    class WorstCase {

        @Test
        void maxMinutes_10x10_rottenTopLeft_allElseFresh() {
            // Manhattan distance from (0,0) to (9,9) = 18
            int[][] grid = filled(10, 10, 1);
            grid[0][0] = 2;
            assertEquals(18, solution.orangesRotting(grid));
        }

        @Test
        void maxMinutes_10x10_rottenBottomRight_allElseFresh() {
            int[][] grid = filled(10, 10, 1);
            grid[9][9] = 2;
            assertEquals(18, solution.orangesRotting(grid));
        }

        @Test
        void maxMinutes_1x10_chain() {
            int[][] grid = {{2, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
            assertEquals(9, solution.orangesRotting(grid));
        }

        @Test
        void maxMinutes_10x1_chain() {
            int[][] grid = new int[10][1];
            grid[0][0] = 2;
            for (int i = 1; i < 10; i++) {
                grid[i][0] = 1;
            }
            assertEquals(9, solution.orangesRotting(grid));
        }

        @Test
        void maxGrid_almostAllFresh_oneUnreachable() {
            int[][] grid = filled(10, 10, 1);
            grid[0][0] = 2;
            // Isolate bottom-right fresh behind empty cells on the only approach path-ish:
            // Make (9,9) only touch empties by clearing its 4-neighbors and itself stays 1.
            grid[8][9] = 0;
            grid[9][8] = 0;
            assertEquals(-1, solution.orangesRotting(grid));
        }
    }

    @Nested
    @DisplayName("Boundary shapes: 1xn, nx1, square extremes")
    class BoundaryShapes {

        static Stream<Arguments> oneByN() {
            return Stream.of(
                    Arguments.of(new int[][]{{0, 0}}, 0),
                    Arguments.of(new int[][]{{0, 1}}, -1),
                    Arguments.of(new int[][]{{0, 2}}, 0),
                    Arguments.of(new int[][]{{1, 0}}, -1),
                    Arguments.of(new int[][]{{1, 1}}, -1),
                    Arguments.of(new int[][]{{1, 2}}, 1),
                    Arguments.of(new int[][]{{2, 0}}, 0),
                    Arguments.of(new int[][]{{2, 1}}, 1),
                    Arguments.of(new int[][]{{2, 2}}, 0)
            );
        }

        @ParameterizedTest(name = "1x2 grid {0} -> {1}")
        @MethodSource("oneByN")
        void allOneByTwoCombinations(int[][] grid, int expected) {
            assertEquals(expected, solution.orangesRotting(grid));
        }

        @Test
        void minRowsMaxCols_emptyAndRottenOnly() {
            assertEquals(0, solution.orangesRotting(new int[][]{{2, 0, 2, 0, 2, 0, 2, 0, 2, 0}}));
        }

        @Test
        void maxRowsMinCols_freshThenRottenFromBottom() {
            int[][] grid = new int[10][1];
            for (int i = 0; i < 9; i++) {
                grid[i][0] = 1;
            }
            grid[9][0] = 2;
            assertEquals(9, solution.orangesRotting(grid));
        }
    }

    @Nested
    @DisplayName("Does not mutate input grid")
    class InputIntegrity {

        @Test
        void gridUnchangedAfterCall() {
            int[][] grid = {
                    {2, 1, 1},
                    {1, 1, 0},
                    {0, 1, 1}
            };
            int[][] copy = deepCopy(grid);
            solution.orangesRotting(grid);
            assertArrayEquals(copy, grid);
        }
    }

    @Nested
    @DisplayName("Mixed reachable and timing sanity")
    class Mixed {

        @Test
        void freshAlreadyNextToMultipleRotten() {
            int[][] grid = {
                    {2, 1, 2},
                    {2, 1, 2}
            };
            assertEquals(1, solution.orangesRotting(grid));
        }

        @Test
        void onlyEmptiesBetweenTwoRotten() {
            assertEquals(0, solution.orangesRotting(new int[][]{{2, 0, 2}}));
        }

        @Test
        void largeSparse_rottenReachesAllFresh() {
            int[][] grid = {
                    {2, 1, 0, 0, 0},
                    {0, 1, 1, 0, 0},
                    {0, 0, 1, 1, 0},
                    {0, 0, 0, 1, 1},
                    {0, 0, 0, 0, 1}
            };
            assertEquals(8, solution.orangesRotting(grid));
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
