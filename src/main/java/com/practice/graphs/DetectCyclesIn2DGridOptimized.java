package com.practice.graphs;

/**
 * Optimized cycle detection on a 2D grid of equal-valued cells.
 * <ul>
 *   <li>Iterative DFS (no call-stack overflow on large grids)</li>
 *   <li>Primitive int stack (no per-node objects)</li>
 *   <li>In-place visited marking via case toggle (no boolean[][])</li>
 * </ul>
 * Note: mutates {@code grid} (lowercase → uppercase for visited cells).
 */
public class DetectCyclesIn2DGridOptimized {

    private static final int[] D_ROW = {-1, 0, 1, 0};
    private static final int[] D_COL = {0, 1, 0, -1};

    public boolean containsCycle(char[][] grid) {
        int rows = grid.length;
        if (rows == 0) {
            return false;
        }
        int cols = grid[0].length;
        // Each frame: row, col, parentRow, parentCol
        int[] stack = new int[rows * cols * 4];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Character.isLowerCase(grid[i][j])
                        && dfs(grid, stack, i, j, rows, cols)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] grid, int[] stack, int startRow, int startCol, int rows, int cols) {
        char val = grid[startRow][startCol];
        int top = 0;
        top = push(stack, top, startRow, startCol, -1, -1);
        grid[startRow][startCol] = Character.toUpperCase(val);

        while (top > 0) {
            int parentCol = stack[--top];
            int parentRow = stack[--top];
            int col = stack[--top];
            int row = stack[--top];

            for (int i = 0; i < 4; i++) {
                int nextRow = row + D_ROW[i];
                int nextCol = col + D_COL[i];

                if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                    continue;
                }
                if (Character.toLowerCase(grid[nextRow][nextCol]) != val) {
                    continue;
                }

                if (Character.isLowerCase(grid[nextRow][nextCol])) {
                    grid[nextRow][nextCol] = Character.toUpperCase(grid[nextRow][nextCol]);
                    top = push(stack, top, nextRow, nextCol, row, col);
                } else if (nextRow != parentRow || nextCol != parentCol) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int push(int[] stack, int top, int row, int col, int parentRow, int parentCol) {
        stack[top++] = row;
        stack[top++] = col;
        stack[top++] = parentRow;
        stack[top++] = parentCol;
        return top;
    }
}
