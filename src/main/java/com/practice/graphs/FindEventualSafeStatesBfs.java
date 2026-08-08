package com.practice.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * LeetCode 802. Find Eventual Safe States (BFS)
 * <p>
 * Directed graph: {@code graph[i]} lists nodes reachable from i.
 * A terminal node has no outgoing edges. A node is safe if every path
 * from it leads to a terminal node (equivalently: it is not on or does
 * not reach a cycle).
 * <p>
 * Return all safe nodes sorted ascending.
 * <p>
 * Approach: reverse the edges and run Kahn's algorithm from terminals
 * (outdegree-0 in the original graph). Nodes that get processed are safe.
 */
public class FindEventualSafeStatesBfs {

    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> reverseGraph = new ArrayList<>(n);
        int[] indegree = new int[n]; // outdegree in the original graph
        for (int i = 0; i < n; i++) {
            reverseGraph.add(new ArrayList<>());
            indegree[i] = graph[i].length;
        }
        for (int u = 0; u < n; u++) {
            for (int v : graph[u]) {
                reverseGraph.get(v).add(u);
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        boolean[] safe = new boolean[n];
        while (!queue.isEmpty()) {
            int node = queue.poll();
            safe[node] = true;
            for (int adj : reverseGraph.get(node)) {
                indegree[adj]--;
                if (indegree[adj] == 0) {
                    queue.add(adj);
                }
            }
        }

        List<Integer> eventualSafeNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (safe[i]) {
                eventualSafeNodes.add(i);
            }
        }
        return eventualSafeNodes;
    }
}
