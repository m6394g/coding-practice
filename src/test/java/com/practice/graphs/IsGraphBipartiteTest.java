package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behavioral coverage for LeetCode 785. Is Graph Bipartite?
 *
 * Constraints: 1 &lt;= n &lt;= 100; undirected, no self/parallel edges.
 */
class IsGraphBipartiteTest {

    private final IsGraphBipartite solution = new IsGraphBipartite();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_trianglePlusExtra_notBipartite() {
            // 0 connected to 1,2,3; edges 1-2 and 0-2 form odd cycles
            int[][] graph = {
                    {1, 2, 3},
                    {0, 2},
                    {0, 1, 3},
                    {0, 2}
            };
            assertFalse(solution.isBipartite(graph));
        }

        @Test
        void example2_evenCycle_bipartite() {
            // Square: {0,2} and {1,3}
            int[][] graph = {
                    {1, 3},
                    {0, 2},
                    {1, 3},
                    {0, 2}
            };
            assertTrue(solution.isBipartite(graph));
        }
    }

    @Nested
    @DisplayName("Trivial graphs")
    class Trivial {

        @Test
        void singleNode_noEdges() {
            assertTrue(solution.isBipartite(new int[][]{{}}));
        }

        @Test
        void twoNodes_oneEdge() {
            assertTrue(solution.isBipartite(new int[][]{{1}, {0}}));
        }

        @Test
        void threeIsolatedNodes() {
            assertTrue(solution.isBipartite(new int[][]{{}, {}, {}}));
        }
    }

    @Nested
    @DisplayName("Bipartite shapes")
    class Bipartite {

        @Test
        void pathOfThree() {
            // 0-1-2
            int[][] graph = {
                    {1},
                    {0, 2},
                    {1}
            };
            assertTrue(solution.isBipartite(graph));
        }

        @Test
        void star_centerConnectedToLeaves() {
            // 0 hub to 1,2,3
            int[][] graph = {
                    {1, 2, 3},
                    {0},
                    {0},
                    {0}
            };
            assertTrue(solution.isBipartite(graph));
        }

        @Test
        void completeBipartite_K2_3() {
            // Parts {0,1} and {2,3,4}
            int[][] graph = {
                    {2, 3, 4},
                    {2, 3, 4},
                    {0, 1},
                    {0, 1},
                    {0, 1}
            };
            assertTrue(solution.isBipartite(graph));
        }

        @Test
        void disconnected_twoEvenCycles() {
            // Component A: 0-1-2-3-0; Component B: 4-5-4
            int[][] graph = {
                    {1, 3},
                    {0, 2},
                    {1, 3},
                    {0, 2},
                    {5},
                    {4}
            };
            assertTrue(solution.isBipartite(graph));
        }
    }

    @Nested
    @DisplayName("Non-bipartite shapes")
    class NonBipartite {

        @Test
        void triangle_oddCycle() {
            int[][] graph = {
                    {1, 2},
                    {0, 2},
                    {0, 1}
            };
            assertFalse(solution.isBipartite(graph));
        }

        @Test
        void pentagon_oddCycle() {
            // 0-1-2-3-4-0
            int[][] graph = {
                    {1, 4},
                    {0, 2},
                    {1, 3},
                    {2, 4},
                    {3, 0}
            };
            assertFalse(solution.isBipartite(graph));
        }

        @Test
        void disconnected_oneOddOneEven() {
            // Triangle on 0-1-2; edge 3-4
            int[][] graph = {
                    {1, 2},
                    {0, 2},
                    {0, 1},
                    {4},
                    {3}
            };
            assertFalse(solution.isBipartite(graph));
        }

        @Test
        void oddCycleNotAtNodeZero() {
            // Isolated 0; triangle on 1-2-3
            int[][] graph = {
                    {},
                    {2, 3},
                    {1, 3},
                    {1, 2}
            };
            assertFalse(solution.isBipartite(graph));
        }
    }
}
