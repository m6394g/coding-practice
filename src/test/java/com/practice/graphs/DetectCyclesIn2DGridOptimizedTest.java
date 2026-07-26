package com.practice.graphs;

import org.junit.jupiter.api.DisplayName;

/** Runs the shared cycle-detection contract against the iterative DFS solution. */
@DisplayName("DetectCyclesIn2DGridOptimized (iterative DFS, in-place marking)")
class DetectCyclesIn2DGridOptimizedTest extends AbstractDetectCyclesIn2DGridTest {

    private final DetectCyclesIn2DGridOptimized solution = new DetectCyclesIn2DGridOptimized();

    @Override
    protected boolean solve(char[][] grid) {
        return solution.containsCycle(grid);
    }
}
