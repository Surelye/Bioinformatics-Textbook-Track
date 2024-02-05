// Find an Eulerian Cycle in a Graph
// ---------------------------------
//
// A cycle that traverses each edge of a graph exactly once is called an Eulerian cycle, and we
// say that a graph containing such a cycle is Eulerian. The following algorithm constructs an
// Eulerian cycle in an arbitrary directed graph.
//
// --------------------
// EULERIANCYCLE(Graph)
//     form a cycle Cycle by randomly walking in Graph (don't visit the same edge twice!)
//     while there are unexplored edges in Graph
//         select a node newStart in Cycle with still unexplored edges form Cycle’ by traversing
//         Cycle (starting at newStart) and then randomly walking
//         Cycle ← Cycle'
//     return Cycle
// --------------------
//
// Eulerian Cycle Problem
//
// Find an Eulerian cycle in a graph.
//
// Given: An Eulerian directed graph, in the form of an adjacency list.
//
// Return: An Eulerian cycle in this graph.
//
// Sample Dataset
// --------------
// 0 -> 3
// 1 -> 0
// 2 -> 1,6
// 3 -> 2
// 4 -> 2
// 5 -> 4
// 6 -> 5,8
// 7 -> 9
// 8 -> 7
// 9 -> 6
// --------------
//
// Sample Output
// -------------
// 6->8->7->9->6->5->4->2->1->0->3->2->6
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA3F {

    private static<T> List<T> findEulerianCycleMachinery(Map<T, List<T>> graph) {
        T v = graph.keySet().stream().toList().getFirst();
        Deque<T> currentPath = new ArrayDeque<>(List.of(v));
        List<T> circuit = new ArrayList<>();

        while (!currentPath.isEmpty()) {
            // If there's remaining edge
            if (graph.get(v).isEmpty()) {
                circuit.add(v);
                v = currentPath.pop();
            } else {
                // Push the vertex
                currentPath.push(v);
                // Find the next vertex using an edge and remove that edge
                // amd move to next vertex
                v = graph.get(v).removeLast();
                // Move to next vertex
            }
        }
        circuit = circuit.reversed();
        BA3UTIL.writePathToFile(circuit);

        return circuit;
    }

    public static List<Integer> findEulerianCycle(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (String adjList : strDataset) {
            String[] splitAdjList = adjList.split(" -> ");
            graph.put(Integer.parseInt(splitAdjList[0]),
                    new ArrayList<>(Arrays
                            .stream(splitAdjList[1].split(","))
                            .map(Integer::parseInt)
                            .toList()));
        }

        return findEulerianCycleMachinery(graph);
    }

    public static<T> List<T> findEulerianCycle(Map<T, List<T>> graph) {
        return findEulerianCycleMachinery(graph);
    }
}
