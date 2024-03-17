// Compute the 2-Break Distance Between a Pair of Genomes
// ------------------------------------------------------
//
// 2-Break Distance Problem
//
// Find the 2-break distance between two genomes.
//
// Given: Two genomes with circular chromosomes on the same set of synteny blocks.
//
// Return: The 2-break distance between these two genomes.
//
// Sample Dataset
// --------------
// (+1 +2 +3 +4 +5 +6)
// (+1 -3 -6 -5)(+2 -4)
// --------------
//
// Sample Output
// -------------
// 3
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA6C {

    private static void
    addEdges(Map<Integer, List<Integer>> graph, List<Map.Entry<Integer, Integer>> edges) {
        int from, to;
        for (Map.Entry<Integer, Integer> edge : edges) {
            from = edge.getKey();
            to = edge.getValue();
            graph.get(from).add(to);
            graph.get(to).add(from);
        }
    }

    private static Map<Integer, List<Integer>>
    makeGraph(List<Map.Entry<Integer, Integer>> fEdges, List<Map.Entry<Integer, Integer>> sEdges) {
        int numNodes = fEdges.size() << 1;
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 1; i <= numNodes; ++i) {
            graph.put(i, new ArrayList<>());
        }
        addEdges(graph, fEdges);
        addEdges(graph, sEdges);

        return graph;
    }

    private static void
    dfs(Map<Integer, List<Integer>> graph, boolean[] visited, int node) {
        Deque<Integer> nodeStack = new ArrayDeque<>();
        nodeStack.push(node);
        int topNode;

        while (!nodeStack.isEmpty()) {
            topNode = nodeStack.pop();
            if (!visited[topNode - 1]) {
                visited[topNode - 1] = true;
                for (int adj : graph.get(topNode)) {
                    nodeStack.push(adj);
                }
            }
        }
    }

    private static int
    twoBreakDistanceMachinery(List<List<Integer>> fGenome, List<List<Integer>> sGenome) {
        int numBlocks = fGenome.stream().mapToInt(List::size).sum(), numCycles = 0;
        Map<Integer, List<Integer>> graph = makeGraph(
                BA6H.coloredEdges(fGenome),
                BA6H.coloredEdges(sGenome)
        );
        boolean[] visited = new boolean[numBlocks << 1];

        for (int i = 1; i <= (numBlocks << 1); ++i) {
            if (!visited[i - 1]) {
                ++numCycles;
                dfs(graph, visited, i);
            }
        }

        return (numBlocks - numCycles);
    }

    public static int twoBreakDistance(List<List<Integer>> fGenome, List<List<Integer>> sGenome) {
        return twoBreakDistanceMachinery(fGenome, sGenome);
    }

    public static int twoBreakDistance(Path path) {
        List<String> genomes = UTIL.readDataset(path);

        return twoBreakDistanceMachinery(
                BA6UTIL.parseGenome(genomes.getFirst()),
                BA6UTIL.parseGenome(genomes.getLast())
        );
    }

    private void run() {
        int twoBreakDistance = twoBreakDistance(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6c.txt"
                )
        );
        BA6UTIL.writeToFile("ba6c_out.txt", List.of(twoBreakDistance));
    }

    public static void main(String[] args) {
        new BA6C().run();
    }
}
