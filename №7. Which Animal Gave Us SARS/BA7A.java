// Compute Distances Between Leaves
// --------------------------------
//
// In this chapter, we define the length of a path in a tree as the sum of the lengths of its edges
// (rather than the number of edges on the path). As a result, the evolutionary distance between two
// present-day species corresponding to leaves i and j in a tree T is equal to the length of the
// unique path connecting i and j, denoted di, j(T).
//
// Distance Between Leaves Problem
//
// Compute the distances between leaves in a weighted tree.
//
// Given: An integer n followed by the adjacency list of a weighted tree with n leaves.
//
// Return: A space-separated n x n (di, j), where di, j is the length of the path between leaves i
// and j.
//
// Sample Dataset
// --------------
// 4
// 0->4:11
// 1->4:2
// 2->5:6
// 3->5:7
// 4->0:11
// 4->1:2
// 4->5:4
// 5->4:4
// 5->3:7
// 5->2:6
// --------------
//
// Sample Output
//
// 0   13  21  22
// 13  0   12  13
// 21  12  0   13
// 22  13  13  0

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BA7A {

    private static void
    dfs(int i, int node, Map<Integer, List<Map.Entry<Integer, Integer>>> adjList,
        boolean[] visited, int distance, List<List<Integer>> distanceMatrix) {
        visited[node] = true;
        if (adjList.get(node).size() == 1 && i != node) {
            distanceMatrix.get(i).set(node, distance);
            distanceMatrix.get(node).set(i, distance);
            return;
        }

        for (Map.Entry<Integer, Integer> toAndWeight : adjList.get(node)) {
            if (!visited[toAndWeight.getKey()]) {
                dfs(i, toAndWeight.getKey(), adjList, visited,
                        distance + toAndWeight.getValue(), distanceMatrix
                );
            }
        }
    }

    private static List<List<Integer>> computeDistanceBetweenLeavesMachinery(
            int n, Map<Integer, List<Map.Entry<Integer, Integer>>> adjList
    ) {
        int numNodes = adjList.size();
        List<List<Integer>> distanceMatrix = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            distanceMatrix.add(new ArrayList<>(Collections.nCopies(n, 0)));
        }

        for (int i = 0; i < n; ++i) {
            dfs(i, i, adjList, new boolean[numNodes], 0, distanceMatrix);
        }

        return distanceMatrix;
    }

    public static List<List<Integer>>
    computeDistanceBetweenLeaves(int n, Map<Integer, List<Map.Entry<Integer, Integer>>> adjList) {
        return computeDistanceBetweenLeavesMachinery(n, adjList);
    }

    public static List<List<Integer>> computeDistanceBetweenLeaves(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        Map<Integer, List<Map.Entry<Integer, Integer>>> adjList =
                BA7UTIL.parseAdjList(strDataset.stream().skip(1).toList());

        return computeDistanceBetweenLeavesMachinery(
                Integer.parseInt(strDataset.getFirst()),
                adjList
        );
    }

    private void run() {
        List<List<Integer>> distanceMatrix = computeDistanceBetweenLeaves(
                Path.of("C:\\Users\\sgnot\\Downloads\\rosalind_ba7a.txt")
        );

        int distanceRowSize = distanceMatrix.size();
        try (FileWriter fileWriter = new FileWriter("ba7a_out.txt")) {
            for (int i = 0; i < distanceRowSize; ++i) {
                for (int j = 0; j < distanceRowSize; ++j) {
                    fileWriter.write("%d%c".formatted(
                            distanceMatrix.get(i).get(j),
                            (j == distanceRowSize - 1) ? '\n' : ' '
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA7A().run();
    }
}
