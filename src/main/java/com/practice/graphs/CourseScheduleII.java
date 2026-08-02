package com.practice.graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 210. Course Schedule II
 *
 * There are a total of numCourses courses labeled from 0 to numCourses - 1.
 * prerequisites[i] = [ai, bi] means you must take course bi before course ai.
 * Return any valid ordering of courses to finish all of them, or an empty array
 * if it is impossible (cycle in the prerequisite graph).
 */
public class CourseScheduleII {

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
        for (int i=0; i<numCourses; i++) {
            adjList.add(new ArrayList<>());
        }
        for (int[] prerequisite : prerequisites) {
            int v = prerequisite[0];
            int u = prerequisite[1];
            adjList.get(u).add(v);
        }

        // count indegrees
        int[] indegrees = new int[numCourses];
        for (int i=0; i<numCourses; i++) {
            ArrayList<Integer> adj = adjList.get(i);
            for (int v : adj) {
                indegrees[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i=0; i<numCourses; i++) {
            if(indegrees[i] == 0) {
                queue.add(i);
            }
        }

        int[] topo = new int[numCourses];
        int cnt=0;
        while(!queue.isEmpty()) {
            int node = queue.poll();
            topo[cnt++] = node;
            for (int adj : adjList.get(node)) {
                indegrees[adj]--;
                if (indegrees[adj] == 0) {
                    queue.add(adj);
                }
            }
        }
        if (cnt != numCourses) {
            return new int[0];
        }
        return topo;
    }
}
