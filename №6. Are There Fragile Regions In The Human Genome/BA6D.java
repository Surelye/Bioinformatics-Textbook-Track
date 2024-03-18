// Find a Shortest Transformation of One Genome into Another by 2-Breaks
// ---------------------------------------------------------------------
//
// 2-Break Sorting Problem
//
// Find a shortest transformation of one genome into another by 2-breaks.
//
// Given: Two genomes with circular chromosomes on the same set of synteny blocks.
//
// Return: The sequence of genomes resulting from applying a shortest sequence of 2-breaks
// transforming one genome into the other.
//
// Sample Dataset
// --------------
// (+1 -2 -3 +4)
// (+1 +2 -4 -3)
// --------------
//
// Sample Output
// -------------
// (+1 -2 -3 +4)
// (+1 -2 -3)(+4)
// (+1 -2 -4 -3)
// (+1 +2 -4 -3)
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class BA6D {

    private static Map<Integer, List<Integer>>
    makeAdjLists(List<Map.Entry<Integer, Integer>> edges) {
        int from, to;
        Map<Integer, List<Integer>> adjLists = new HashMap<>();
        for (Map.Entry<Integer, Integer> edge : edges) {
            from = edge.getKey();
            to = edge.getValue();
            if (adjLists.containsKey(from)) {
                adjLists.get(from).add(to);
            } else {
                adjLists.put(from, new ArrayList<>(List.of(to)));
            }
            if (adjLists.containsKey(to)) {
                adjLists.get(to).add(from);
            } else {
                adjLists.put(to, new ArrayList<>(List.of(from)));
            }
        }

        return adjLists;
    }

    private static List<List<Integer>> edgesToCycles(List<Map.Entry<Integer, Integer>> edges) {
        int numNodes = edges.size(), cur, next;
        Map<Integer, List<Integer>> adjLists = makeAdjLists(edges);
        Set<Integer> seen = new HashSet<>();
        List<Integer> cycle;
        List<List<Integer>> cycles = new ArrayList<>();

        for (int i = 1; i <= numNodes; ++i) {
            cycle = new ArrayList<>();
            while (!seen.contains(i)) {
                cycle.add(i);
                if (adjLists.containsKey(i) && !seen.contains(adjLists.get(i).getLast())) {
                    cur = i;
                    next = adjLists.get(i).getLast();
                    adjLists.get(i).removeLast();
                    if (adjLists.get(i).isEmpty()) {
                        seen.add(i);
                    }
                    i = next;
                    adjLists.get(i).remove((Integer) cur);
                    if (adjLists.get(i).isEmpty()) {
                        seen.add(i);
                    }
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

    private static Map.Entry<Integer, Integer>
    pickNonTrivialBlueEdge(List<Map.Entry<Integer, Integer>> blueEdges, List<Integer> cycle) {
        int cycleSize = cycle.size(), cur, next = cycle.getFirst();
        Map.Entry<Integer, Integer> currentEdge;

        for (int i = 0; i < cycleSize - 1; ++i) {
            cur = next;
            next = cycle.get(i + 1);
            currentEdge = Map.entry(cur, next);
            if (blueEdges.contains(currentEdge)) {
                return currentEdge;
            }
            currentEdge = Map.entry(next, cur);
            if (blueEdges.contains(currentEdge)) {
                return currentEdge;
            }
        }

        throw new RuntimeException("Proper blue edge not found");
    }

    private static Map.Entry<Integer, Integer>
    pickEdgeOriginatingAt(List<Map.Entry<Integer, Integer>> edges, int origin) {
        int from, to;

        for (Map.Entry<Integer, Integer> edge : edges) {
            from = edge.getKey();
            to = edge.getValue();
            if (from == origin || to == origin) {
                return edge;
            }
        }

        throw new RuntimeException("Proper red edge not found");
    }

    private static List<Map.Entry<Integer, Integer>> mergeEdges(
            List<Map.Entry<Integer, Integer>> blueEdges,
            List<Map.Entry<Integer, Integer>> redEdges) {
        List<Map.Entry<Integer, Integer>> breakpointGraphEdges = new ArrayList<>(blueEdges);
        breakpointGraphEdges.addAll(redEdges);

        return breakpointGraphEdges;
    }

    private static List<List<List<Integer>>> shortestRearrangementScenarioMachinery(
            List<List<Integer>> genomeP, List<List<Integer>> genomeQ) {
        int i, iPrime, j, jPrime;
        List<List<Integer>> sorting;
        List<List<List<Integer>>> twoBreakSorting = new ArrayList<>(List.of(genomeP));
        List<Map.Entry<Integer, Integer>> redEdges = BA6H.coloredEdges(genomeP);
        List<Map.Entry<Integer, Integer>> blueEdges = BA6H.coloredEdges(genomeQ);
        List<Map.Entry<Integer, Integer>> breakpointGraphEdges = mergeEdges(redEdges, blueEdges);
        List<List<Integer>> nonTrivialCycles;
        Map.Entry<Integer, Integer> nonTrivialBlueEdge, leftRedOrigin, rightRedOrigin;

        while (true) {
            nonTrivialCycles = edgesToCycles(breakpointGraphEdges)
                    .stream().filter(cycle -> cycle.size() != 2)
                    .toList();
            if (nonTrivialCycles.isEmpty()) {
                break;
            }
            nonTrivialBlueEdge = pickNonTrivialBlueEdge(blueEdges, nonTrivialCycles.getFirst());
            leftRedOrigin = pickEdgeOriginatingAt(redEdges, nonTrivialBlueEdge.getKey());
            rightRedOrigin = pickEdgeOriginatingAt(redEdges, nonTrivialBlueEdge.getValue());
            redEdges.removeAll(List.of(leftRedOrigin, rightRedOrigin));
            redEdges.add(nonTrivialBlueEdge);
            j = nonTrivialBlueEdge.getKey(); iPrime = nonTrivialBlueEdge.getValue();
            i = (leftRedOrigin.getValue() == j) ? leftRedOrigin.getKey() : leftRedOrigin.getValue();
            jPrime = (iPrime == rightRedOrigin.getKey()) ? rightRedOrigin.getValue() : rightRedOrigin.getKey();
            redEdges.add(Map.entry(jPrime, i));
            breakpointGraphEdges = mergeEdges(redEdges, blueEdges);
            sorting = BA6K.twoBreakOnGenome(
                    twoBreakSorting.getLast(),
                    (leftRedOrigin.getValue() == j) ? j : leftRedOrigin.getKey(),
                    i,
                    (iPrime == rightRedOrigin.getKey()) ? iPrime : rightRedOrigin.getValue(),
                    jPrime
            );
            twoBreakSorting.add(sorting);
        }

        return twoBreakSorting;
    }

    public static List<List<List<Integer>>>
    shortestRearrangementScenario(List<List<Integer>> genomeP, List<List<Integer>> genomeQ) {
        return shortestRearrangementScenarioMachinery(genomeP, genomeQ);
    }

    public static List<List<List<Integer>>> shortestRearrangementScenario(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return shortestRearrangementScenarioMachinery(
                BA6UTIL.parseGenome(strDataset.getFirst()),
                BA6UTIL.parseGenome(strDataset.getLast())
        );
    }

    private void run() {
        List<List<List<Integer>>> twoBreakSorting = shortestRearrangementScenario(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6d.txt"
                )
        );

        int sortingSize = twoBreakSorting.size(), genomeSize, chromosomeSize;
        List<List<Integer>> genome;
        List<Integer> chromosome;
        int syntenyBlock;
        try (FileWriter fileWriter = new FileWriter("ba6d_out.txt")) {
            for (int i = 0; i < sortingSize; ++i) {
                genome = twoBreakSorting.get(i);
                genomeSize = genome.size();
                for (int j = 0; j < genomeSize; ++j) {
                    chromosome = genome.get(j);
                    chromosomeSize = chromosome.size();
                    fileWriter.write('(');
                    for (int k = 0; k < chromosomeSize; ++k) {
                        syntenyBlock = chromosome.get(k);
                        fileWriter.write("%s%d%c".formatted(
                                (syntenyBlock > 0) ? "+" : "",
                                syntenyBlock,
                                (k == chromosomeSize - 1) ? ')' : ' '
                        ));
                    }
                    fileWriter.write((j == genomeSize - 1) ? "\n" : "");
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA6D().run();
    }
}
