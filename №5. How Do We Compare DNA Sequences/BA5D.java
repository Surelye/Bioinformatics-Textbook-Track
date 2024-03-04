// Find the Longest Path in a DAG
// ------------------------------
// Longest Path in a DAG Problem
//
// Find a longest path between two nodes in an edge-weighted DAG.
//
// Given: An integer representing the source node of a graph, followed by an integer representing
// the sink node of the graph, followed by an edge-weighted graph. The graph is represented by a
// modified adjacency list in which the notation "0->1:7" indicates that an edge connects node 0 to
// node 1 with weight 7.
//
// Return: The length of a longest path in the graph, followed by a longest path. (If multiple
// longest paths exist, you may return any one.)
//
// Sample Dataset
// --------------
// 0
// 4
// 0->1:7
// 0->2:4
// 2->3:2
// 1->4:1
// 3->4:3
// --------------
//
// Sample Output
// -------------
// 9
// 0->2->3->4
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA5D {

    private static Map<Integer, List<Integer>>
    buildWeightlessGraph(Map<Integer, List<Map.Entry<Integer, Integer>>> graph) {
        Map<Integer, List<Integer>> weightlessGraph = new HashMap<>();

        for (int from : graph.keySet()) {
            weightlessGraph.put(from, new ArrayList<>());
            for (Map.Entry<Integer, Integer> toAndWeight : graph.get(from)) {
                int to = toAndWeight.getKey();
                weightlessGraph.get(from).add(to);
                if (!weightlessGraph.containsKey(to)) {
                    weightlessGraph.put(to, new ArrayList<>());
                }
            }
        }

        return weightlessGraph;
    }

    private static Map.Entry<Integer, List<Integer>>
    findLongestPathInDAGMachinery(int source, int sink,
                                  Map<Integer, List<Map.Entry<Integer, Integer>>> graph) {
        Map<Integer, List<Integer>> weightlessGraph = buildWeightlessGraph(graph);
        List<Integer> topologicalSorting = new BA5N().findTopologicalSorting(weightlessGraph);
        int indexOfSource = topologicalSorting.indexOf(source);
        Map<Integer, Map.Entry<Integer, Integer>> longestPathAndPredecessor = new HashMap<>();
        for (int node : weightlessGraph.keySet()) {
            longestPathAndPredecessor.put(node, Map.entry(0, -1));
        }

        int node, to, weight, pathLength, predecessor;
        for (int i = indexOfSource; i < topologicalSorting.size(); ++i) {
            node = topologicalSorting.get(i);
            predecessor = longestPathAndPredecessor.get(node).getValue();
            if (predecessor == -1 && node != source) {
                continue;
            }
            pathLength = longestPathAndPredecessor.get(node).getKey();
            if (graph.containsKey(node)) {
                for (Map.Entry<Integer, Integer> toAndWeight : graph.get(node)) {
                    to = toAndWeight.getKey();
                    weight = toAndWeight.getValue();
                    if (pathLength + weight > longestPathAndPredecessor.get(to).getKey()) {
                        longestPathAndPredecessor.put(to, Map.entry(pathLength + weight, node));
                    }
                }
            }
        }
        int longestPathLength = longestPathAndPredecessor.get(sink).getKey();
        List<Integer> longestPath = new ArrayList<>();
        for (int cur = sink; cur != -1; cur = longestPathAndPredecessor.get(cur).getValue()) {
            longestPath.add(cur);
        }

        return Map.entry(longestPathLength, longestPath.reversed());
    }

    public static Map.Entry<Integer, List<Integer>>
    findLongestPathInDAG(int source, int sink,
                         Map<Integer, List<Map.Entry<Integer, Integer>>> graph) {
        return findLongestPathInDAGMachinery(source, sink, graph);
    }

    public static Map.Entry<Integer, List<Integer>> findLongestPathInDAG(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int source = Integer.parseInt(strDataset.getFirst()),
                sink = Integer.parseInt(strDataset.get(1)),
                datasetSize = strDataset.size();
        Map<Integer, List<Map.Entry<Integer, Integer>>> graph = new HashMap<>();

        int from, to, weight;
        for (int i = 2; i < datasetSize; ++i) {
            String[] edge = strDataset.get(i).split("->");
            from = Integer.parseInt(edge[0]);
            String[] toAndWeight = edge[1].split(":");
            to = Integer.parseInt(toAndWeight[0]);
            weight = Integer.parseInt(toAndWeight[1]);

            if (graph.containsKey(from)) {
                graph.get(from).add(Map.entry(to, weight));
            } else {
                graph.put(from, new ArrayList<>(List.of(Map.entry(to, weight))));
            }
        }

        return findLongestPathInDAGMachinery(source, sink, graph);
    }

    private void run() {
        Map.Entry<Integer, List<Integer>> pathLengthAndPath = findLongestPathInDAG(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5d.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            fileWriter.write("%d%n".formatted(pathLengthAndPath.getKey()));
            int longestPathSize = pathLengthAndPath.getValue().size();
            int i = 1;
            for (int pathNode : pathLengthAndPath.getValue()) {
                fileWriter.write("%d%s".formatted(pathNode, (i == longestPathSize) ? "" : "->"));
                i += 1;
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA5D().run();
    }
}
