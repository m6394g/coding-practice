package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behavioral coverage for LeetCode 210. Course Schedule II
 *
 * Constraints: 1 &lt;= numCourses &lt;= 2000;
 * 0 &lt;= prerequisites.length &lt;= numCourses * (numCourses - 1);
 * pairs unique; ai != bi; 0 &lt;= ai, bi &lt; numCourses.
 */
class CourseScheduleIITest {

    private final CourseScheduleII solution = new CourseScheduleII();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_singlePrerequisite() {
            int[] order = solution.findOrder(2, new int[][]{{1, 0}});
            assertValidOrder(2, new int[][]{{1, 0}}, order);
            assertArrayEquals(new int[]{0, 1}, order);
        }

        @Test
        void example2_diamond_anyValidOrder() {
            int[][] prerequisites = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
            int[] order = solution.findOrder(4, prerequisites);
            assertValidOrder(4, prerequisites, order);
        }

        @Test
        void example3_singleCourse() {
            assertArrayEquals(new int[]{0}, solution.findOrder(1, new int[][]{}));
        }
    }

    @Nested
    @DisplayName("Trivial graphs")
    class Trivial {

        @Test
        void multipleCourses_noPrerequisites() {
            int[] order = solution.findOrder(3, new int[][]{});
            assertValidOrder(3, new int[][]{}, order);
        }

        @Test
        void twoCourses_oneEdge() {
            int[][] prerequisites = {{0, 1}};
            int[] order = solution.findOrder(2, prerequisites);
            assertValidOrder(2, prerequisites, order);
            assertArrayEquals(new int[]{1, 0}, order);
        }
    }

    @Nested
    @DisplayName("Acyclic graphs")
    class Acyclic {

        @Test
        void linearChain() {
            // take 2, then 1, then 0
            int[][] prerequisites = {{0, 1}, {1, 2}};
            int[] order = solution.findOrder(3, prerequisites);
            assertValidOrder(3, prerequisites, order);
            assertArrayEquals(new int[]{2, 1, 0}, order);
        }

        @Test
        void disconnected_twoIndependentChains() {
            int[][] prerequisites = {{0, 1}, {2, 3}};
            int[] order = solution.findOrder(4, prerequisites);
            assertValidOrder(4, prerequisites, order);
        }

        @Test
        void star_manyDependOnOne() {
            int[][] prerequisites = {{1, 0}, {2, 0}, {3, 0}};
            int[] order = solution.findOrder(4, prerequisites);
            assertValidOrder(4, prerequisites, order);
            assertEquals(0, order[0]);
        }
    }

    @Nested
    @DisplayName("Cyclic graphs")
    class Cyclic {

        @Test
        void twoCycle_returnsEmpty() {
            assertArrayEquals(new int[0], solution.findOrder(2, new int[][]{{1, 0}, {0, 1}}));
        }

        @Test
        void threeCycle_returnsEmpty() {
            assertArrayEquals(new int[0],
                    solution.findOrder(3, new int[][]{{1, 0}, {2, 1}, {0, 2}}));
        }

        @Test
        void disconnected_oneChainOneCycle_returnsEmpty() {
            assertArrayEquals(new int[0],
                    solution.findOrder(4, new int[][]{{0, 1}, {2, 3}, {3, 2}}));
        }

        @Test
        void cycleNotInvolvingAllCourses_returnsEmpty() {
            assertArrayEquals(new int[0],
                    solution.findOrder(3, new int[][]{{0, 1}, {1, 0}}));
        }
    }

    /**
     * Asserts {@code order} is a permutation of [0, numCourses) that respects
     * every prerequisite (bi appears before ai).
     */
    private static void assertValidOrder(int numCourses, int[][] prerequisites, int[] order) {
        assertEquals(numCourses, order.length, "order length");

        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < order.length; i++) {
            int course = order[i];
            assertTrue(course >= 0 && course < numCourses, "course out of range: " + course);
            assertTrue(position.put(course, i) == null, "duplicate course: " + course);
        }
        assertEquals(numCourses, position.size(), "missing courses");

        for (int[] edge : prerequisites) {
            int ai = edge[0];
            int bi = edge[1];
            assertTrue(position.get(bi) < position.get(ai),
                    "prerequisite violated: " + bi + " must come before " + ai);
        }
    }
}
