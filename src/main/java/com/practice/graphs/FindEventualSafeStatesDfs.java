package com.practice.graphs;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 802. Find Eventual Safe States (DFS)
 *
 * Directed graph: {@code graph[i]} lists nodes reachable from i.
 * A terminal node has no outgoing edges. A node is safe if every path
 * from it leads to a terminal node (equivalently: it is not on or does
 * not reach a cycle).
 *
 * Return all safe nodes sorted ascending.
 *
 * Approach: DFS with visited / pathVisited / check marking to detect nodes
 * that participate in or reach a cycle; remaining nodes are safe.
 */
public class FindEventualSafeStatesDfs {

    public List<Integer> eventualSafeNodes(int[][] graph) {
        int V = graph.length;
        int[] visited = new int[V];
        int[] pathVisited = new int[V];
        int[] check = new int[V];
        for (int i = 0; i < V; i++) {
            if (visited[i] == 0) {
                dfs(graph, i, visited, pathVisited, check);
            }
        }
        List<Integer> eventualSafeNodes = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (check[i] == 1) {
                eventualSafeNodes.add(i);
            }
        }
        return eventualSafeNodes;
    }

    private boolean dfs(int[][] graph, int node, int[] visited, int[] pathVisited, int[] check) {
        visited[node] = 1;
        pathVisited[node] = 1;

        for (int adj : graph[node]) {
            if (visited[adj] == 0) {
                if (!dfs(graph, adj, visited, pathVisited, check)) {
                    pathVisited[node] = 0;
                    return false;
                }
            } else if (pathVisited[adj] == 1 || check[adj] == 0) {
                pathVisited[node] = 0;
                return false;
            }
        }

        check[node] = 1;
        pathVisited[node] = 0;
        return true;
    }
}
