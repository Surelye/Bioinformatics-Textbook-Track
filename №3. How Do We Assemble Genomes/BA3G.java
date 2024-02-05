// Find an Eulerian Path in a Graph
// --------------------------------
//
// In “Find an Eulerian Cycle in a Graph”, we defined an Eulerian cycle. A path that traverses each
// edge of a graph exactly once (but does not necessarily return to its starting node is called an
// Eulerian path.
//
// ---------------------
// Eulerian Path Problem
//
// Find an Eulerian path in a graph.
//
// Given: A directed graph that contains an Eulerian path, where the graph is given in the form of
// an adjacency list.
//
// Return: An Eulerian path in this graph.
//
// Sample Dataset
// --------------
// 0 -> 2
// 1 -> 3
// 2 -> 1
// 3 -> 0,4
// 6 -> 3,7
// 7 -> 8
// 8 -> 9
// 9 -> 6
// --------------
//
// Sample Output
// -------------
// 6->7->8->9->6->3->0->2->1->3->4
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA3G {

    private static<T> List<T>
    findEulerianPathMachinery(Map<T, List<T>> graph, Map<T, int[]> inOutDegrees) {
        T start = null, end = null;
        for (T node : inOutDegrees.keySet()) {
            int[] degrees = inOutDegrees.get(node);
            if (degrees[0] != degrees[1]) {
                if (degrees[0] + 1 == degrees[1]) {
                    start = node;
                } else if (degrees[1] + 1 == degrees[0]) {
                    end = node;
                }
            }
        }
        if (start == null || end == null) {
            throw new RuntimeException("Provided graph doesn't contain an Eulerian path");
        }
        if (graph.containsKey(end)) {
            graph.get(end).add(start);
        } else {
            graph.put(end, new ArrayList<>(List.of(start)));
        }
        List<T> circuit = BA3F.findEulerianCycle(graph).stream().skip(1).toList();
        int indexOfStart = circuit.indexOf(start), circuitSize = circuit.size();
        List<T> path = new ArrayList<>();
        for (int i = indexOfStart; i < circuitSize + indexOfStart; ++i) {
            path.add(circuit.get(i % circuitSize));
        }
        BA3UTIL.writePathToFile(path);

        return path;
    }

    private static<T> List<T> findEulerianPathWrapper(Map<T, List<T>> graph) {
        Map<T, int[]> inOutDegrees = new HashMap<>();
        List<T> adjList;

        for (T node : graph.keySet()) {
            adjList = graph.get(node);

            if (inOutDegrees.containsKey(node)) {
                inOutDegrees.get(node)[1] = adjList.size();
            } else {
                inOutDegrees.put(node, new int[]{0, adjList.size()});
            }
            for (T adjNode : adjList) {
                if (inOutDegrees.containsKey(adjNode)) {
                    ++inOutDegrees.get(adjNode)[0];
                } else {
                    inOutDegrees.put(adjNode, new int[]{1, 0});
                }
            }
        }

        return findEulerianPathMachinery(graph, inOutDegrees);
    }

    public static List<Integer> findEulerianPath(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (String adjList : strDataset) {
            String[] nodes = adjList.split(" -> ");
            graph.put(Integer.parseInt(nodes[0]), new ArrayList<>(Arrays
                    .stream(nodes[1].split(","))
                    .map(Integer::parseInt)
                    .toList()));
        }

        return findEulerianPathWrapper(graph);
    }

    public static<T> List<T> findEulerianPath(Map<T, List<T>> graph) {
        return findEulerianPathWrapper(graph);
    }
}
