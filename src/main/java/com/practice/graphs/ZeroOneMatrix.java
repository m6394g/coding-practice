package com.practice.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 542. 01 Matrix
 *
 * Given an m x n binary matrix mat, return the distance of the nearest 0 for
 * each cell. The distance between two cells sharing a common edge is 1.
 */
public class ZeroOneMatrix {

    private static final int UNVISITED = -1;
    private static final int[] D_ROW = {-1, 0, 1, 0};
    private static final int[] D_COL = {0, 1, 0, -1};

    private static class Cell {
        final int row;
        final int col;
        final int distance;

        Cell(int row, int col, int distance) {
            this.row = row;
            this.col = col;
            this.distance = distance;
        }
    }

    public int[][] updateMatrix(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[][] distances = new int[rows][cols];
        Queue<Cell> queue = new LinkedList<>();
        seedZeros(mat, distances, queue, rows, cols);
        multiSourceBfs(distances, queue, rows, cols);
        return distances;
    }

    private void seedZeros(int[][] mat, int[][] distances, Queue<Cell> queue, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (mat[row][col] == 0) {
                    distances[row][col] = 0;
                    queue.add(new Cell(row, col, 0));
                } else {
                    distances[row][col] = UNVISITED;
                }
            }
        }
    }

    private void multiSourceBfs(int[][] distances, Queue<Cell> queue, int rows, int cols) {
        while (!queue.isEmpty()) {
            Cell cell = queue.poll();

            for (int i = 0; i < 4; i++) {
                int nextRow = cell.row + D_ROW[i];
                int nextCol = cell.col + D_COL[i];

                if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                    continue;
                }
                if (distances[nextRow][nextCol] != UNVISITED) {
                    continue;
                }

                int nextDistance = cell.distance + 1;
                distances[nextRow][nextCol] = nextDistance;
                queue.add(new Cell(nextRow, nextCol, nextDistance));
            }
        }
    }
}
