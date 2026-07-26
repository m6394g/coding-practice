package com.practice.graphs;

public class DetectCyclesIn2DGrid {

    private static final int[] D_ROW = {-1, 0, 1, 0};
    private static final int[] D_COL = {0, 1, 0, -1};

    private static class Cell {
        int row;
        int col;
        char val;
        int parentRow;
        int parentCol;

        Cell(int row, int col, char val, int parentRow, int parentCol) {
            this.row = row;
            this.col = col;
            this.val = val;
            this.parentRow = parentRow;
            this.parentCol = parentCol;
        }
    }

    public boolean containsCycle(char[][] grid) {
        int rows = grid.length;
        if (rows == 0) {
            return false;
        }
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    visited[i][j] = true;
                    if (dfs(grid, visited, new Cell(i, j, grid[i][j], -1, -1), rows, cols)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] grid, boolean[][] visited, Cell cell, int rows, int cols) {
        for (int i = 0; i < 4; i++) {
            int nextRow = cell.row + D_ROW[i];
            int nextCol = cell.col + D_COL[i];

            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                continue;
            }
            if (grid[nextRow][nextCol] != cell.val) {
                continue;
            }

            if (!visited[nextRow][nextCol]) {
                visited[nextRow][nextCol] = true;
                Cell next = new Cell(nextRow, nextCol, grid[nextRow][nextCol], cell.row, cell.col);
                if (dfs(grid, visited, next, rows, cols)) {
                    return true;
                }
            } else if (nextRow != cell.parentRow || nextCol != cell.parentCol) {
                return true;
            }
        }
        return false;
    }
}
