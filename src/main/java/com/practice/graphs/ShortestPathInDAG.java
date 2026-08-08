package com.practice.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * GFG: Shortest path in Directed Acyclic Graph (Medium)
 * <p>
 * Given a DAG with {@code V} vertices (0..V-1) and weighted directed edges
 * {@code edges[i] = [u, v, wt]}, return the shortest distance from source
 * vertex {@code 0} to every vertex. Unreachable vertices are {@code -1}.
 * <p>
 * Approach: Kahn BFS — when a node is dequeued its {@code dist} is final, so
 * relax outgoing edges in the same pass (topo + relax combined).
 * <p>
 * Examples:
 * <ul>
 *   <li>V=4, edges=[[0,1,2],[0,2,1]] → [0, 2, 1, -1]</li>
 *   <li>V=6, edges=[[0,1,2],[0,4,1],[4,5,4],[4,2,2],[1,2,3],[2,3,6],[5,3,1]]
 *       → [0, 2, 3, 6, 1, 5]</li>
 * </ul>
 */
public class ShortestPathInDAG {

    private static final int INF = 1_000_000_000;

    /**
     * @param V     number of vertices
     * @param E     number of edges
     * @param edges directed weighted edges {@code [u, v, wt]}
     * @return distances from 0 as an {@link ArrayList}; {@code -1} if unreachable
     */
    public ArrayList<Integer> shortestPath(int V, int E, int[][] edges) {
        List<List<int[]>> adjList = new ArrayList<>(V);
        int[] indegrees = new int[V];
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];
            adjList.get(u).add(new int[]{v, wt});
            indegrees[v]++;
        }

        int[] dist = new int[V];
        Arrays.fill(dist, INF);
        dist[0] = 0;

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (indegrees[i] == 0) {
                queue.add(i);
            }
        }

        // Kahn order: all preds of u are done ⇒ dist[u] final ⇒ safe to relax u→v
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int[] edge : adjList.get(u)) {
                int v = edge[0];
                int wt = edge[1];
                if (dist[u] != INF && dist[u] + wt < dist[v]) {
                    dist[v] = dist[u] + wt;
                }
                indegrees[v]--;
                if (indegrees[v] == 0) {
                    queue.add(v);
                }
            }
        }

        ArrayList<Integer> result = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            result.add(dist[i] == INF ? -1 : dist[i]);
        }
        return result;
    }
}
