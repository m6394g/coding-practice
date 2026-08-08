package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;

import java.util.List;

/** Runs the shared eventual-safe-states contract against the BFS / Kahn solution. */
@DisplayName("FindEventualSafeStatesBfs")
class FindEventualSafeStatesBfsTest extends AbstractFindEventualSafeStatesTest {

    private final FindEventualSafeStatesBfs solution = new FindEventualSafeStatesBfs();

    @Override
    protected List<Integer> solve(int[][] graph) {
        return solution.eventualSafeNodes(graph);
    }
}
