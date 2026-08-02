package com.practice.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 785. Is Graph Bipartite?
 *
 * There is an undirected graph with n nodes numbered 0 to n - 1. graph[u] lists
 * the neighbors of u. Return true if and only if the graph is bipartite — i.e.
 * the nodes can be partitioned into two independent sets such that every edge
 * connects a node in one set to a node in the other.
 */
public class IsGraphBipartite {

    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];

        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                if (hasConflict(graph, color, i)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** BFS 2-coloring from {@code start}; returns true if a same-color edge is found. */
    private boolean hasConflict(int[][] graph, int[] color, int start) {
        color[start] = 1;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neighbor : graph[current]) {
                if (color[neighbor] == 0) {
                    color[neighbor] = color[current] % 2 == 0 ? 1 : 2;
                    queue.add(neighbor);
                } else if (color[neighbor] == color[current]) {
                    return true;
                }
            }
        }
        return false;
    }
}
