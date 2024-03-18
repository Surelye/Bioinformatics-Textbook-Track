// Implement 2-BreakOnGenome
//
// We can extend the pseudocode in “Implement 2-BreakOnGenomeGraph” to a 2-break defined on genome
// P.
//
// --------------------------------
// 2-BreakOnGenome(P, i, i′, j, j′)
//  GenomeGraph ← BlackEdges(P) and ColoredEdges(P)
//  GenomeGraph ← 2-BreakOnGenomeGraph(GenomeGraph, i, i′, j, j′)
//  P ← GraphToGenome(GenomeGraph)
//  return P
// --------------------------------
//
// Implement 2-BreakOnGenome
//
// Solve the 2-Break On Genome Graph Problem.
//
// Given: A genome P, followed by indices i, i', j, and j'.
//
// Return: The genome P' resulting from applying the 2-break operation.
//
// Sample Dataset
// --------------
// (+1 -2 -4 +3)
// 1, 6, 3, 8
// --------------
//
// Sample Output
// -------------
// (+2 -1) (-3 +4)
// -------------

import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class BA6K {

    private static List<List<Integer>> edgesToCycles(List<Map.Entry<Integer, Integer>> edges) {
        int nodesNum = edges.size() << 1, from, to;
        Map<Integer, Integer> edgeMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> edge : edges) {
            from = edge.getKey();
            to = edge.getValue();
            edgeMap.put(from, to);
            edgeMap.put(to, from);
        }
        Set<Integer> seen = new HashSet<>();
        List<Integer> cycle;
        List<List<Integer>> cycles = new ArrayList<>();

        for (int i = 1; i <= nodesNum; i += 2) {
            cycle = new ArrayList<>();
            while (!seen.contains(i)) {
                seen.add(i);
                cycle.add(i);
                if (edgeMap.containsKey(i) && !seen.contains(edgeMap.get(i))) {
                    i = edgeMap.get(i);
                } else {
                    i = i + ((i % 2 == 1) ? 1 : -1);
                }
            }
            if (!cycle.isEmpty()) {
                cycles.add(cycle);
            }
        }

        return cycles;
    }

    private static List<List<Integer>> cyclesToGenome(List<List<Integer>> cycles) {
        int first, second;
        List<List<Integer>> genome = new ArrayList<>();
        for (List<Integer> cycle : cycles) {
            first = cycle.getFirst();
            second = cycle.get(1);
            if ((first != second - 1) || (first != second + 1)) {
                cycle.removeFirst();
                cycle.add(first);
            }
            genome.add(BA6G.cycleToChromosome(cycle));
        }

        return genome;
    }

    private static List<List<Integer>>
    twoBreakOnGenomeMachinery(List<List<Integer>> genome, int i, int iPrime, int j, int jPrime) {
        List<Map.Entry<Integer, Integer>> edges = BA6H.coloredEdges(genome);
        BA6J.twoBreakOnGenomeGraph(edges, i, iPrime, j, jPrime);

        return cyclesToGenome(edgesToCycles(edges));
    }

    public static List<List<Integer>>
    twoBreakOnGenome(List<List<Integer>> genome, int i, int iPrime, int j, int jPrime) {
        return twoBreakOnGenomeMachinery(genome, i, iPrime, j, jPrime);
    }

    public static List<List<Integer>> twoBreakOnGenome(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> indices = BA6UTIL.parseIntArray(strDataset.getLast(), ", ");

        return twoBreakOnGenomeMachinery(
                BA6UTIL.parseGenome(strDataset.getFirst()),
                indices.getFirst(),
                indices.get(1),
                indices.get(2),
                indices.getLast()
        );
    }

    private void run() {
        List<List<Integer>> genome = twoBreakOnGenome(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6k.txt"
                )
        );
        BA6UTIL.writeGenomeToFile("ba6k_out.txt", genome, " ");
    }

    public static void main(String[] args) {
        new BA6K().run();
    }
}
