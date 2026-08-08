package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Behavioral coverage for GFG Shortest path in Directed Acyclic Graph.
 *
 * Constraints: 1 &lt;= V &lt;= 100;
 * 1 &lt;= E &lt;= min(V*(V-1)/2, 4000);
 * 0 &lt;= u, v &lt; V; 0 &lt;= wt &lt;= 1e5; graph is a DAG; source is 0.
 */
class ShortestPathInDAGTest {

    private final ShortestPathInDAG solution = new ShortestPathInDAG();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_unreachableVertex() {
            int[][] edges = {{0, 1, 2}, {0, 2, 1}};
            assertEquals(List.of(0, 2, 1, -1), solution.shortestPath(4, 2, edges));
        }

        @Test
        void example2_multiPathDag() {
            int[][] edges = {
                    {0, 1, 2}, {0, 4, 1}, {4, 5, 4}, {4, 2, 2},
                    {1, 2, 3}, {2, 3, 6}, {5, 3, 1}
            };
            assertEquals(List.of(0, 2, 3, 6, 1, 5), solution.shortestPath(6, 7, edges));
        }
    }

    @Nested
    @DisplayName("Trivial graphs")
    class Trivial {

        @Test
        void singleVertex_noEdges() {
            assertEquals(List.of(0), solution.shortestPath(1, 0, new int[][]{}));
        }

        @Test
        void twoVertices_directEdge() {
            int[][] edges = {{0, 1, 5}};
            assertEquals(List.of(0, 5), solution.shortestPath(2, 1, edges));
        }

        @Test
        void twoVertices_noEdgeFromSource() {
            // edge into 0 only — 1 unreachable from 0
            int[][] edges = {{1, 0, 3}};
            assertEquals(List.of(0, -1), solution.shortestPath(2, 1, edges));
        }
    }

    @Nested
    @DisplayName("Path choice")
    class PathChoice {

        @Test
        void prefersShorterOfTwoRoutes() {
            // 0→1→2 weight 10, 0→2 weight 3 → pick direct
            int[][] edges = {{0, 1, 4}, {1, 2, 6}, {0, 2, 3}};
            assertEquals(List.of(0, 4, 3), solution.shortestPath(3, 3, edges));
        }

        @Test
        void prefersIndirectWhenCheaper() {
            // 0→2 weight 10, 0→1→2 weight 1+2=3
            int[][] edges = {{0, 2, 10}, {0, 1, 1}, {1, 2, 2}};
            assertEquals(List.of(0, 1, 3), solution.shortestPath(3, 3, edges));
        }

        @Test
        void linearChain() {
            int[][] edges = {{0, 1, 1}, {1, 2, 2}, {2, 3, 3}};
            assertEquals(List.of(0, 1, 3, 6), solution.shortestPath(4, 3, edges));
        }

        @Test
        void zeroWeightEdges() {
            int[][] edges = {{0, 1, 0}, {1, 2, 0}, {0, 2, 5}};
            assertEquals(List.of(0, 0, 0), solution.shortestPath(3, 3, edges));
        }
    }

    @Nested
    @DisplayName("Reachability")
    class Reachability {

        @Test
        void disconnectedComponents_onlySourceReachable() {
            int[][] edges = {{1, 2, 1}, {2, 3, 1}};
            assertEquals(List.of(0, -1, -1, -1), solution.shortestPath(4, 2, edges));
        }

        @Test
        void sourceReachableSubtree_othersMinusOne() {
            int[][] edges = {{0, 1, 2}, {1, 2, 3}, {3, 4, 1}};
            assertEquals(List.of(0, 2, 5, -1, -1), solution.shortestPath(5, 3, edges));
        }

        @Test
        void diamondWithDeadBranch() {
            // 0→1→3 and 0→2; vertex 4 isolated
            int[][] edges = {{0, 1, 1}, {0, 2, 4}, {1, 3, 2}};

            assertEquals(List.of(0, 1, 4, 3, -1), solution.shortestPath(5, 3, edges));
        }

        @Test
        void sourceDownstreamOnly_upstreamUnreachable() {
            // Edges feed into 0 from 6→5/4; from 0 only 0→1→3 is reachable
            int[][] edges = {
                    {6, 4, 2}, {6, 5, 3}, {5, 4, 1}, {4, 0, 3},
                    {4, 2, 1}, {0, 1, 2}, {2, 3, 3}, {1, 3, 1}
            };
            assertEquals(List.of(0, 2, -1, 3, -1, -1, -1), solution.shortestPath(7, 8, edges));
        }
    }

    @Nested
    @DisplayName("Layered DAG (many paths)")
    class LayeredDag {

        /**
         * 3×3 layered DAG with a sink — many walks, one cheapest prefix.
         * <pre>
         *   nodes: 0
         *          1,2,3   (A)   0→1 wt 1; 0→2,0→3 wt 100
         *          4,5,6   (B)   every A→ every B wt 1
         *          7,8,9   (C)   every B→ every C wt 1
         *          10      (T)   every C→ T wt 1
         * </pre>
         * Best path uses A=1 only: dist T = 1+1+1+1 = 4.
         * Path-enumeration would expand 27 walks to T; topo+relax uses each edge once.
         */
        @Test
        void threeByThreeLayers_prefersCheapFirstHop() {
            int[][] edges = {
                    {0, 1, 1}, {0, 2, 100}, {0, 3, 100},
                    {1, 4, 1}, {1, 5, 1}, {1, 6, 1},
                    {2, 4, 1}, {2, 5, 1}, {2, 6, 1},
                    {3, 4, 1}, {3, 5, 1}, {3, 6, 1},
                    {4, 7, 1}, {4, 8, 1}, {4, 9, 1},
                    {5, 7, 1}, {5, 8, 1}, {5, 9, 1},
                    {6, 7, 1}, {6, 8, 1}, {6, 9, 1},
                    {7, 10, 1}, {8, 10, 1}, {9, 10, 1}
            };
            assertEquals(
                    List.of(0, 1, 100, 100, 2, 2, 2, 3, 3, 3, 4),
                    solution.shortestPath(11, 24, edges));
        }
    }
}
