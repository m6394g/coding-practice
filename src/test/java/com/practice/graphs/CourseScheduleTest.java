package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behavioral coverage for LeetCode 207. Course Schedule
 *
 * Constraints: 1 &lt;= numCourses &lt;= 2000; 0 &lt;= prerequisites.length &lt;= 5000;
 * pairs unique; 0 &lt;= ai, bi &lt; numCourses.
 */
class CourseScheduleTest {

    private final CourseSchedule solution = new CourseSchedule();

    @Nested
    @DisplayName("Problem examples")
    class Examples {

        @Test
        void example1_singlePrerequisite_canFinish() {
            assertTrue(solution.canFinish(2, new int[][]{{1, 0}}));
        }

        @Test
        void example2_twoCycle_cannotFinish() {
            assertFalse(solution.canFinish(2, new int[][]{{1, 0}, {0, 1}}));
        }
    }

    @Nested
    @DisplayName("Trivial graphs")
    class Trivial {

        @Test
        void singleCourse_noPrerequisites() {
            assertTrue(solution.canFinish(1, new int[][]{}));
        }

        @Test
        void multipleCourses_noPrerequisites() {
            assertTrue(solution.canFinish(3, new int[][]{}));
        }

        @Test
        void twoCourses_oneEdge() {
            assertTrue(solution.canFinish(2, new int[][]{{0, 1}}));
        }
    }

    @Nested
    @DisplayName("Acyclic graphs")
    class Acyclic {

        @Test
        void linearChain() {
            // 0 <- 1 <- 2  (take 2, then 1, then 0)
            assertTrue(solution.canFinish(3, new int[][]{{0, 1}, {1, 2}}));
        }

        @Test
        void diamond_sharedPrerequisite() {
            // 1 and 2 both need 0; 3 needs 1 and 2
            assertTrue(solution.canFinish(4, new int[][]{
                    {1, 0}, {2, 0}, {3, 1}, {3, 2}
            }));
        }

        @Test
        void disconnected_twoIndependentChains() {
            // 0 needs 1; 2 needs 3
            assertTrue(solution.canFinish(4, new int[][]{{0, 1}, {2, 3}}));
        }

        @Test
        void star_manyDependOnOne() {
            // courses 1,2,3 all require 0
            assertTrue(solution.canFinish(4, new int[][]{{1, 0}, {2, 0}, {3, 0}}));
        }
    }

    @Nested
    @DisplayName("Cyclic graphs")
    class Cyclic {

        @Test
        void selfLoop() {
            assertFalse(solution.canFinish(1, new int[][]{{0, 0}}));
        }

        @Test
        void threeCycle() {
            // 0 -> 1 -> 2 -> 0
            assertFalse(solution.canFinish(3, new int[][]{{1, 0}, {2, 1}, {0, 2}}));
        }

        @Test
        void disconnected_oneChainOneCycle() {
            // 0 needs 1 (ok); 2 <-> 3 (cycle)
            assertFalse(solution.canFinish(4, new int[][]{{0, 1}, {2, 3}, {3, 2}}));
        }

        @Test
        void cycleNotInvolvingAllCourses() {
            // courses 0,1 cycle; course 2 free
            assertFalse(solution.canFinish(3, new int[][]{{0, 1}, {1, 0}}));
        }
    }
}
