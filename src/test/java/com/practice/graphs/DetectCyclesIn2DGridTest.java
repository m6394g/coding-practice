package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;

/** Runs the shared cycle-detection contract against the recursive DFS solution. */
@DisplayName("DetectCyclesIn2DGrid (recursive DFS)")
class DetectCyclesIn2DGridTest extends AbstractDetectCyclesIn2DGridTest {

    private final DetectCyclesIn2DGrid solution = new DetectCyclesIn2DGrid();

    @Override
    protected boolean solve(char[][] grid) {
        return solution.containsCycle(grid);
    }
}
