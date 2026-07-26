package com.practice.graphs;

import java.util.LinkedList;
import java.util.Queue;

public class RottingOranges {

    private static class Pair {
        int r;
        int c;
        int tm;

        public Pair (int r, int c, int tm) {
            this.r = r;
            this.c = c;
            this.tm = tm;
        }
    }

    public int orangesRotting(int[][] grid) {
        int n = grid.length;
        if (n == 0) {
            return 0;
        }
        int m = grid[0].length;
        Queue<Pair> q = new LinkedList<>();
        int[][] vis = new int[n][m];
        int cntFresh = initQueueAndVisAndReturnCntFresh(q, vis, n, m, grid);
        if (cntFresh < 0) {
            // Invalid input
            return -1;
        }

        int tmRes = 0;
        int[] dRow = {-1, 0, 1, 0};
        int[] dCol = {0, 1, 0, -1};
        int cntRotten = 0;
        while(!q.isEmpty()) {
            Pair p = q.peek();
            int r = p.r;
            int c = p.c;
            int tm = p.tm;

            tmRes = Math.max(tmRes, tm);
            for (int i=0; i<4; i++) {
                int nr = r + dRow[i];
                int nc = c + dCol[i];

                if (nr>=0 && nr<n && nc>=0 && nc<m && vis[nr][nc]==0 && grid[nr][nc]==1) {
                    q.add(new Pair(nr, nc, tm + 1));
                    vis[nr][nc] = 2;
                    cntRotten++;
                }
            }
            q.remove();
        }
        if (cntRotten != cntFresh) {
            return -1;
        }
        return tmRes;
    }

    private int initQueueAndVisAndReturnCntFresh(Queue<Pair> q, int[][] vis, int n, int m, int[][] grid) {
        int cntFresh = 0;
        for (int i=0; i<n; i++) {
            for (int j=0; j<m; j++) {
                if (grid[i].length == 0 || grid[i].length < m || grid[i][j] < 0 || grid[i][j] > 2) {
                    return -1;
                }
                if (grid[i][j] == 2) {
                    q.add(new Pair(i, j, 0));
                    vis[i][j] = 2;
                } else {
                    vis[i][j] = 0;
                }
                if (grid[i][j] == 1) {
                    cntFresh++;
                }
            }
        }
        return cntFresh;
    }
}
