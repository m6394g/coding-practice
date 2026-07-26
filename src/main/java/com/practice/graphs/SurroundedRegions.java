package com.practice.graphs;

/**
 * LeetCode 130. Surrounded Regions
 *
 * Given an m x n matrix board containing 'X' and 'O', capture all regions that
 * are surrounded by 'X'. A region is surrounded if none of its 'O' cells are on
 * the edge of the board. Capture by flipping all 'O's in such regions to 'X'
 * in-place.
 */
public class SurroundedRegions {

    private static final int[] D_ROW = {-1, 0, 1, 0};
    private static final int[] D_COL = {0, 1, 0, -1};

    private static class Cell {
        final int row;
        final int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public void solve(char[][] board) {
        int rows = board.length;
        if (rows == 0) {
            return;
        }
        int cols = board[0].length;
        boolean[][] visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            markBorderRegion(board, visited, i, 0, rows, cols);
            markBorderRegion(board, visited, i, cols - 1, rows, cols);
        }
        for (int j = 0; j < cols; j++) {
            markBorderRegion(board, visited, 0, j, rows, cols);
            markBorderRegion(board, visited, rows - 1, j, rows, cols);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'O' && !visited[i][j]) {
                    board[i][j] = 'X';
                }
            }
        }
    }

    private void markBorderRegion(
            char[][] board, boolean[][] visited, int row, int col, int rows, int cols) {
        if (board[row][col] != 'O' || visited[row][col]) {
            return;
        }
        visited[row][col] = true;
        dfs(board, visited, new Cell(row, col), rows, cols);
    }

    private void dfs(char[][] board, boolean[][] visited, Cell cell, int rows, int cols) {
        for (int i = 0; i < 4; i++) {
            int nextRow = cell.row + D_ROW[i];
            int nextCol = cell.col + D_COL[i];

            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                continue;
            }
            if (board[nextRow][nextCol] != 'O' || visited[nextRow][nextCol]) {
                continue;
            }

            visited[nextRow][nextCol] = true;
            dfs(board, visited, new Cell(nextRow, nextCol), rows, cols);
        }
    }
}
