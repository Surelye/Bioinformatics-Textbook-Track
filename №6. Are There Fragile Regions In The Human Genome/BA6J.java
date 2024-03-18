// Implement 2-BreakOnGenomeGraph
// ------------------------------
//
// We will use 2-Break(1, 6, 3, 8) two denote the 2-break replacing colored edges (1, 6) and (3, 8)
// in a genome graph with two new colored edges (1, 3) and (6, 8). Note that the order of the nodes
// in this function matter, since the operation 2-Break(1, 6, 8, 3) would represent a different
// 2-break that replaces (1, 6) and (3, 8) with (1, 8) and (6, 3).
//
// The following pseudocode describes how 2-Break(i, i′, j, j′) transforms a genome graph.
//
// -----------------------------------------------
// 2-BreakOnGenomeGraph(GenomeGraph, i, i′, j, j′)
//  remove colored edges (i, i') and (j, j′) from GenomeGraph
//  add colored edges (i, j) and (i′, j') to GenomeGraph
//  return GenomeGraph
// -----------------------------------------------
//
// 2-Break On Genome Graph Problem
//
// Solve the 2-Break On Genome Graph Problem.
//
// Given: The colored edges of a genome graph GenomeGraph, followed by indices i, i', j, and j'.
//
// Return: The colored edges of the genome graph resulting from applying the 2-break operation.
//
// Sample Dataset
// --------------
// (2, 4), (3, 8), (7, 5), (6, 1)
// 1, 6, 3, 8
// --------------
//
// Sample Output
// -------------
// (2, 4), (3, 1), (7, 5), (6, 8)
// -------------

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class BA6J {

    private static void twoBreakOnGenomeGraphMachinery(
            List<Map.Entry<Integer, Integer>> edges, int i, int iPrime, int j, int jPrime
    ) {
        int from, to, edgeIndex;
        Map.Entry<Integer, Integer> newEdge;

        for (Map.Entry<Integer, Integer> edge : edges) {
            from = edge.getKey();
            to = edge.getValue();
            if ((from == i && to == iPrime) || (to == i && from == iPrime)) {
                edgeIndex = edges.indexOf(edge);
                edges.set(
                        edgeIndex,
                        (from == i) ? Map.entry(from, j) : Map.entry(j, to)
                );
            }
            if ((from == j && to == jPrime) || (to == j && from == jPrime)) {
                edgeIndex = edges.indexOf(edge);
                edges.set(
                        edgeIndex,
                        (from == j) ? Map.entry(iPrime, to) : Map.entry(from, iPrime)
                );
            }
        }
    }

    public static void twoBreakOnGenomeGraph(
            List<Map.Entry<Integer, Integer>> edges, int i, int iPrime, int j, int jPrime
    ) {
        twoBreakOnGenomeGraphMachinery(edges, i, iPrime, j, jPrime);
    }

    public static List<Map.Entry<Integer, Integer>> twoBreakOnGenomeGraph(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Map.Entry<Integer, Integer>> edges = BA6UTIL.parseEdges(strDataset.getFirst());
        List<Integer> indices = BA6UTIL.parseIntArray(strDataset.getLast(), ", ");
        twoBreakOnGenomeGraphMachinery(
                edges,
                indices.getFirst(),
                indices.get(1),
                indices.get(2),
                indices.getLast()
        );

        return edges;
    }

    private void run() {
        List<Map.Entry<Integer, Integer>> edgesAfterTwoBreak = twoBreakOnGenomeGraph(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6j.txt"
                )
        );
        BA6UTIL.writeEdgesToFile("ba6j_out.txt", edgesAfterTwoBreak);
    }

    public static void main(String[] args) {
        new BA6J().run();
    }
}
