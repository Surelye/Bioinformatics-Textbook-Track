// Find a Topological Ordering of a DAG
// ------------------------------------
//
// Topological Ordering Problem
//
// Find a topological ordering of a directed acyclic graph.
//
// Given: The adjacency list of a graph (with nodes represented by integers).
//
// Return: A topological ordering of this graph.
//
// Sample Dataset
// --------------
// 1 -> 2
// 2 -> 3
// 4 -> 2
// 5 -> 3
// --------------
//
// Sample Output
// -------------
// 1, 4, 5, 2, 3
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BA5N {

    private Map<Integer, List<Integer>> makeGraph(List<String> strDataset) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (String nodeAdjList : strDataset) {
            String[] data = nodeAdjList.split(" -> ");
            int keyNode = Integer.parseInt(data[0]);
            List<Integer> adjacents = Arrays.stream(data[1]
                            .split(","))
                    .map(Integer::parseInt)
                    .toList();
            graph.put(keyNode, adjacents);
            for (int adjacent : adjacents) {
                if (!graph.containsKey(adjacent)) {
                    graph.put(adjacent, new ArrayList<>());
                }
            }
        }

        return graph;
    }

    private void
    dfs(int keyNode, Map<Integer, List<Integer>> graph, Map<Integer, Boolean> visited,
        List<Integer> topologicalSorting) {
        if (visited.get(keyNode)) {
            return;
        }
        visited.put(keyNode, true);
        for (int adjNode : graph.get(keyNode)) {
            dfs(adjNode, graph, visited, topologicalSorting);
        }
        topologicalSorting.add(keyNode);
    }

    private List<Integer> findTopologicalSortingMachinery(Map<Integer, List<Integer>> graph) {
        Set<Integer> keyNodes = graph.keySet();
        Map<Integer, Boolean> visited = new HashMap<>();
        for (int keyNode : keyNodes) {
            visited.put(keyNode, false);
        }
        List<Integer> topologicalSorting = new ArrayList<>();

        for (int keyNode : keyNodes) {
            dfs(keyNode, graph, visited, topologicalSorting);
        }

        return topologicalSorting.reversed();
    }

    public List<Integer> findTopologicalSorting(Map<Integer, List<Integer>> graph) {
        return findTopologicalSortingMachinery(graph);
    }

    public List<Integer> findTopologicalSorting(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        Map<Integer, List<Integer>> graph = makeGraph(strDataset);

        return findTopologicalSortingMachinery(graph);
    }

    private void run() {
        List<Integer> topologicalSorting = findTopologicalSorting(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5n.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            int i = 1, sortSize = topologicalSorting.size();
            for (int node : topologicalSorting) {
                fileWriter.write("%d%s".formatted(node, (i == sortSize) ? "" : ", "));
                i += 1;
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA5N().run();
    }
}
