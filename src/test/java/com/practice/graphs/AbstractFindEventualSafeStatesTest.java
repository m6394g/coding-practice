package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Shared behavioral coverage for LeetCode 802. Find Eventual Safe States.
 * Concrete subclasses supply DFS / BFS via {@link #solve(int[][])}.
 *
 * <p>A node is safe iff every path from it reaches a terminal (no cycle on any path).
 * Answers must be sorted ascending. Graphs may contain self-loops; adjacency lists
 * are strictly increasing with no parallel edges.
 */
abstract class AbstractFindEventualSafeStatesTest {

    protected abstract List<Integer> solve(int[][] graph);

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_mixedCycleAndTerminals() {
            // Cycle 0-1-3; 2/4 drain to terminals 5,6
            int[][] graph = {
                    {1, 2},
                    {2, 3},
                    {5},
                    {0},
                    {5},
                    {},
                    {}
            };
            assertEquals(List.of(2, 4, 5, 6), solve(graph));
        }

        @Test
        void example2_onlyTerminalSafe() {
            // Cycle involving 0,1,2,3; only 4 is terminal
            int[][] graph = {
                    {1, 2, 3, 4},
                    {1, 2},
                    {3, 4},
                    {0, 4},
                    {}
            };
            assertEquals(List.of(4), solve(graph));
        }
    }

    @Nested
    @DisplayName("Trivial graphs")
    class Trivial {

        @Test
        void singleTerminal() {
            assertEquals(List.of(0), solve(new int[][]{{}}));
        }

        @Test
        void singleSelfLoop_unsafe() {
            assertEquals(List.of(), solve(new int[][]{{0}}));
        }

        @Test
        void allIsolatedTerminals() {
            assertEquals(List.of(0, 1, 2), solve(new int[][]{{}, {}, {}}));
        }

        @Test
        void twoNodeChain_bothSafe() {
            // 0 -> 1 -> (terminal)
            assertEquals(List.of(0, 1), solve(new int[][]{{1}, {}}));
        }
    }

    @Nested
    @DisplayName("DAGs (all nodes safe)")
    class Dags {

        @Test
        void linearChain() {
            // 0 -> 1 -> 2 -> 3
            int[][] graph = {
                    {1},
                    {2},
                    {3},
                    {}
            };
            assertEquals(List.of(0, 1, 2, 3), solve(graph));
        }

        @Test
        void diamond() {
            // 0 -> 1,2; 1 -> 3; 2 -> 3; 3 terminal
            int[][] graph = {
                    {1, 2},
                    {3},
                    {3},
                    {}
            };
            assertEquals(List.of(0, 1, 2, 3), solve(graph));
        }

        @Test
        void starIntoTerminal() {
            // 1,2,3 -> 0; 0 terminal
            int[][] graph = {
                    {},
                    {0},
                    {0},
                    {0}
            };
            assertEquals(List.of(0, 1, 2, 3), solve(graph));
        }

        @Test
        void branchingThenMerge() {
            // 0 -> 1,2; 1 -> 2; 2 terminal
            int[][] graph = {
                    {1, 2},
                    {2},
                    {}
            };
            assertEquals(List.of(0, 1, 2), solve(graph));
        }
    }

    @Nested
    @DisplayName("Cycles (nodes on or reaching a cycle are unsafe)")
    class Cycles {

        @Test
        void mutualCycle_bothUnsafe() {
            // 0 <-> 1
            assertEquals(List.of(), solve(new int[][]{{1}, {0}}));
        }

        @Test
        void triangleCycle_allUnsafe() {
            // 0 -> 1 -> 2 -> 0
            int[][] graph = {
                    {1},
                    {2},
                    {0}
            };
            assertEquals(List.of(), solve(graph));
        }

        @Test
        void selfLoopAmongTerminals() {
            // 0 self-loop; 1,2 terminals
            int[][] graph = {
                    {0},
                    {},
                    {}
            };
            assertEquals(List.of(1, 2), solve(graph));
        }

        @Test
        void chainIntoCycle_allUnsafe() {
            // 0 -> 1 -> 2 <-> 3
            int[][] graph = {
                    {1},
                    {2},
                    {3},
                    {2}
            };
            assertEquals(List.of(), solve(graph));
        }

        @Test
        void nodePointsIntoCycle_unsafe() {
            // 0 -> 1; 1 <-> 2
            int[][] graph = {
                    {1},
                    {2},
                    {1}
            };
            assertEquals(List.of(), solve(graph));
        }
    }

    @Nested
    @DisplayName("Mixed safe and unsafe")
    class Mixed {

        @Test
        void edgeToSafeAndUnsafe_sourceUnsafe() {
            // 0 -> 1 (cycle), 0 -> 2 (terminal) => 0 unsafe; 2 safe
            int[][] graph = {
                    {1, 2},
                    {0},
                    {}
            };
            assertEquals(List.of(2), solve(graph));
        }

        @Test
        void disconnected_cycleAndTerminal() {
            // Comp A: 0 <-> 1; Comp B: 2 terminal
            int[][] graph = {
                    {1},
                    {0},
                    {}
            };
            assertEquals(List.of(2), solve(graph));
        }

        @Test
        void safeDrainBesideCycle() {
            // Cycle 0 -> 1 -> 0; 2 -> 3 terminal
            int[][] graph = {
                    {1},
                    {0},
                    {3},
                    {}
            };
            assertEquals(List.of(2, 3), solve(graph));
        }

        @Test
        void terminalFirstThenCycleLater() {
            // 0 terminal; cycle 1 -> 2 -> 1
            int[][] graph = {
                    {},
                    {2},
                    {1}
            };
            assertEquals(List.of(0), solve(graph));
        }

        @Test
        void multipleParentsOfTerminal_allSafe() {
            // 0,1,2 all point only to terminal 3
            int[][] graph = {
                    {3},
                    {3},
                    {3},
                    {}
            };
            assertEquals(List.of(0, 1, 2, 3), solve(graph));
        }

        @Test
        void onlySomeParentsSafe_whenSiblingReachesCycle() {
            // 0 -> 3 (safe); 1 -> 2; 2 -> 1 (cycle); 3 terminal
            int[][] graph = {
                    {3},
                    {2},
                    {1},
                    {}
            };
            assertEquals(List.of(0, 3), solve(graph));
        }

        @Test
        void longSafeChainAndSeparateSelfLoop() {
            // 0 -> 1 -> 2 -> 3; 4 self-loop
            int[][] graph = {
                    {1},
                    {2},
                    {3},
                    {},
                    {4}
            };
            assertEquals(List.of(0, 1, 2, 3), solve(graph));
        }
    }

    @Nested
    @DisplayName("Ordering and reachability edge cases")
    class OrderingAndReachability {

        @Test
        void resultIsAscendingEvenWhenSafeNodesScattered() {
            // Safe: 1, 3, 5; unsafe cycle 0 <-> 2; 4 -> 0
            int[][] graph = {
                    {2},
                    {},
                    {0},
                    {},
                    {0},
                    {}
            };
            assertEquals(List.of(1, 3, 5), solve(graph));
        }

        @Test
        void reachesAlreadyProcessedUnsafeNeighbor() {
            // Process cycle 1 <-> 2 first via start at 1; then 0 -> 1 must be unsafe
            int[][] graph = {
                    {1},
                    {2},
                    {1}
            };
            assertEquals(List.of(), solve(graph));
        }

        @Test
        void reachesAlreadyProcessedSafeNeighbor() {
            // 2 terminal processed or finished first; 0 -> 1 -> 2 all safe
            int[][] graph = {
                    {1},
                    {2},
                    {}
            };
            assertEquals(List.of(0, 1, 2), solve(graph));
        }

        @Test
        void nodeWithEmptyAdjListInMiddleOfIndexRange() {
            // 0 -> 2; 1 terminal; 2 terminal
            int[][] graph = {
                    {2},
                    {},
                    {}
            };
            assertEquals(List.of(0, 1, 2), solve(graph));
        }

        @Test
        void selfLoopDoesNotPoisonUnrelatedSafeComponent() {
            int[][] graph = {
                    {0},
                    {2},
                    {}
            };
            assertEquals(List.of(1, 2), solve(graph));
        }
    }
}
