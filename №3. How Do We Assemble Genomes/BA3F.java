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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BA3F {

    private static List<Integer> findEulerianCycleMachinery(Map<Integer, List<Integer>> graph) {
        int[] edgeCount = new int[graph.keySet().size()];
        for (int vertex : graph.keySet()) {
            edgeCount[vertex] = graph.get(vertex).size();
        }
        Deque<Integer> currentPath = new ArrayDeque<>(List.of(0));
        List<Integer> circuit = new ArrayList<>();
        int v = 0, nextV;

        while (!currentPath.isEmpty()) {
            // If there's remaining edge
            if (edgeCount[v] != 0) {
                // Push the vertex
                currentPath.push(v);
                //Find the next vertex using an edge
                nextV = graph.get(v).getLast();
                // and remove that edge
                --edgeCount[v];
                graph.get(v).removeLast();
                // Move to next vertex
                v = nextV;
            } else {
                circuit.add(v);
                v = currentPath.pop();
            }
        }
        circuit = circuit.reversed();
        int circuitLength = circuit.size(), i = 1;

        for (int vertex : circuit) {
            System.out.printf("%s%s", vertex, (i == circuitLength) ? "\n" : "->");
            ++i;
        }

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            i = 1;
            for (int vertex : circuit) {
                fileWriter.write("%s%s".formatted(vertex, (i == circuitLength) ? "\n" : "->"));
                ++i;
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }

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

    public static List<Integer> findEulerianCycle(Map<Integer, List<Integer>> graph) {
        return findEulerianCycleMachinery(graph);
    }
}
