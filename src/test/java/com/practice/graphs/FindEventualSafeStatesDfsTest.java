package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;

import java.util.List;

/** Runs the shared eventual-safe-states contract against the DFS solution. */
@DisplayName("FindEventualSafeStatesDfs")
class FindEventualSafeStatesDfsTest extends AbstractFindEventualSafeStatesTest {

    private final FindEventualSafeStatesDfs solution = new FindEventualSafeStatesDfs();

    @Override
    protected List<Integer> solve(int[][] graph) {
        return solution.eventualSafeNodes(graph);
    }
}
