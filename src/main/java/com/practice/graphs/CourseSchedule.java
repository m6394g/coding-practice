package com.practice.graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * LeetCode 207. Course Schedule
 *
 * There are a total of numCourses courses labeled from 0 to numCourses - 1.
 * prerequisites[i] = [ai, bi] means you must take course bi before course ai.
 * Return true if you can finish all courses, otherwise false.
 */
public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
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

        List<Integer> topo = new ArrayList<>();
        while(!queue.isEmpty()) {
            int node = queue.poll();
            topo.add(node);
            for (int adj : adjList.get(node)) {
                indegrees[adj]--;
                if (indegrees[adj] == 0) {
                    queue.add(adj);
                }
            }
        }
        return topo.size() == numCourses;
    }
}
