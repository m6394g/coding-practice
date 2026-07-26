package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Behavioral coverage for LeetCode 130. Surrounded Regions.
 *
 * Constraints: 1 &lt;= m, n &lt;= 200; board[i][j] ∈ {'X', 'O'}.
 * {@link SurroundedRegions#solve} mutates the board in-place.
 */
class SurroundedRegionsTest {

    private final SurroundedRegions solution = new SurroundedRegions();

    private void assertSolve(char[][] board, char[][] expected) {
        solution.solve(board);
        assertArrayEquals(expected, board);
    }

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_capturesInteriorButKeepsEdgeRegion() {
            char[][] board = {
                    {'X', 'X', 'X', 'X'},
                    {'X', 'O', 'O', 'X'},
                    {'X', 'X', 'O', 'X'},
                    {'X', 'O', 'X', 'X'}
            };
            char[][] expected = {
                    {'X', 'X', 'X', 'X'},
                    {'X', 'X', 'X', 'X'},
                    {'X', 'X', 'X', 'X'},
                    {'X', 'O', 'X', 'X'}
            };
            assertSolve(board, expected);
        }

        @Test
        void example2_singleXUnchanged() {
            assertSolve(new char[][]{{'X'}}, new char[][]{{'X'}});
        }
    }

    @Nested
    @DisplayName("Minimal grids")
    class Minimal {

        @Test
        void singleO_onEdgeStays() {
            assertSolve(new char[][]{{'O'}}, new char[][]{{'O'}});
        }

        @Test
        void oneByThree_allO_stay() {
            assertSolve(
                    new char[][]{{'O', 'O', 'O'}},
                    new char[][]{{'O', 'O', 'O'}}
            );
        }

        @Test
        void threeByOne_allO_stay() {
            assertSolve(
                    new char[][]{{'O'}, {'O'}, {'O'}},
                    new char[][]{{'O'}, {'O'}, {'O'}}
            );
        }

        @Test
        void oneByThree_mixed() {
            assertSolve(
                    new char[][]{{'X', 'O', 'X'}},
                    new char[][]{{'X', 'O', 'X'}}
            );
        }
    }

    @Nested
    @DisplayName("No capture")
    class NoCapture {

        @Test
        void allX() {
            assertSolve(
                    new char[][]{
                            {'X', 'X'},
                            {'X', 'X'}
                    },
                    new char[][]{
                            {'X', 'X'},
                            {'X', 'X'}
                    }
            );
        }

        @Test
        void allO_connectedToBorder() {
            assertSolve(
                    new char[][]{
                            {'O', 'O', 'O'},
                            {'O', 'O', 'O'},
                            {'O', 'O', 'O'}
                    },
                    new char[][]{
                            {'O', 'O', 'O'},
                            {'O', 'O', 'O'},
                            {'O', 'O', 'O'}
                    }
            );
        }

        @Test
        void everyOTouchesEdge() {
            assertSolve(
                    new char[][]{
                            {'X', 'O', 'X'},
                            {'O', 'X', 'O'},
                            {'X', 'O', 'X'}
                    },
                    new char[][]{
                            {'X', 'O', 'X'},
                            {'O', 'X', 'O'},
                            {'X', 'O', 'X'}
                    }
            );
        }
    }

    @Nested
    @DisplayName("Full capture")
    class FullCapture {

        @Test
        void singleInteriorO() {
            assertSolve(
                    new char[][]{
                            {'X', 'X', 'X'},
                            {'X', 'O', 'X'},
                            {'X', 'X', 'X'}
                    },
                    new char[][]{
                            {'X', 'X', 'X'},
                            {'X', 'X', 'X'},
                            {'X', 'X', 'X'}
                    }
            );
        }

        @Test
        void multipleDisconnectedInteriorRegions() {
            assertSolve(
                    new char[][]{
                            {'X', 'X', 'X', 'X', 'X'},
                            {'X', 'O', 'X', 'O', 'X'},
                            {'X', 'X', 'X', 'X', 'X'}
                    },
                    new char[][]{
                            {'X', 'X', 'X', 'X', 'X'},
                            {'X', 'X', 'X', 'X', 'X'},
                            {'X', 'X', 'X', 'X', 'X'}
                    }
            );
        }

        @Test
        void largeInteriorBlob() {
            assertSolve(
                    new char[][]{
                            {'X', 'X', 'X', 'X'},
                            {'X', 'O', 'O', 'X'},
                            {'X', 'O', 'O', 'X'},
                            {'X', 'X', 'X', 'X'}
                    },
                    new char[][]{
                            {'X', 'X', 'X', 'X'},
                            {'X', 'X', 'X', 'X'},
                            {'X', 'X', 'X', 'X'},
                            {'X', 'X', 'X', 'X'}
                    }
            );
        }
    }

    @Nested
    @DisplayName("Partial capture")
    class PartialCapture {

        @Test
        void borderChainProtectsConnectedInterior() {
            assertSolve(
                    new char[][]{
                            {'O', 'O', 'X'},
                            {'X', 'O', 'X'},
                            {'X', 'X', 'X'}
                    },
                    new char[][]{
                            {'O', 'O', 'X'},
                            {'X', 'O', 'X'},
                            {'X', 'X', 'X'}
                    }
            );
        }

        @Test
        void capturesSurroundedWhileKeepingSeparateEdgeRegion() {
            assertSolve(
                    new char[][]{
                            {'X', 'X', 'X', 'X'},
                            {'X', 'O', 'X', 'O'},
                            {'X', 'X', 'X', 'X'}
                    },
                    new char[][]{
                            {'X', 'X', 'X', 'X'},
                            {'X', 'X', 'X', 'O'},
                            {'X', 'X', 'X', 'X'}
                    }
            );
        }

        @Test
        void cornerOProtectsNothingDiagonally() {
            // Diagonal adjacency does not connect; center O is surrounded.
            assertSolve(
                    new char[][]{
                            {'O', 'X', 'X'},
                            {'X', 'O', 'X'},
                            {'X', 'X', 'X'}
                    },
                    new char[][]{
                            {'O', 'X', 'X'},
                            {'X', 'X', 'X'},
                            {'X', 'X', 'X'}
                    }
            );
        }
    }

    @Nested
    @DisplayName("Edges and corners")
    class EdgesAndCorners {

        @Test
        void fourCornerOs_stay() {
            assertSolve(
                    new char[][]{
                            {'O', 'X', 'O'},
                            {'X', 'X', 'X'},
                            {'O', 'X', 'O'}
                    },
                    new char[][]{
                            {'O', 'X', 'O'},
                            {'X', 'X', 'X'},
                            {'O', 'X', 'O'}
                    }
            );
        }

        @Test
        void twoByTwo_allCellsAreEdge() {
            assertSolve(
                    new char[][]{
                            {'O', 'X'},
                            {'X', 'O'}
                    },
                    new char[][]{
                            {'O', 'X'},
                            {'X', 'O'}
                    }
            );
        }
    }
}
