// Implement GraphToGenome
// -----------------------
//
// The colored edges in the breakpoint graph of P and Q are given by ColoredEdges(P) together with
// ColoredEdges(Q). Note that some edges in these two sets may connect the same two nodes, which
// results in trivial cycles.
//
// We will find it helpful to implement a function converting a genome graph back into a genome.
//
// --------------------------
// GraphToGenome(GenomeGraph)
//  P ← an empty set of chromosomes
//  for each cycle Nodes in GenomeGraph
//       Chromosome ← CycleToChromosome(Nodes)
//       add Chromosome to P
//  return P
// --------------------------
//
// Graph To Genome Problem
//
// Solve the Graph To Genome Problem.
//
// Given: The colored edges of a genome graph.
//
// Return: A genome corresponding to the genome graph.
//
// Sample Dataset
// --------------
// (2, 4), (3, 6), (5, 1), (7, 9), (10, 12), (11, 8)
// --------------
//
// Sample Output
// -------------
// (+1 -2 -3)(-4 +5 -6)
// -------------

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BA6I {

    public static List<List<Integer>> graphToGenome(List<Map.Entry<Integer, Integer>> graph) {
        int from, to;
        List<Integer> chromosome = new ArrayList<>();
        List<List<Integer>> genome = new ArrayList<>();

        for (Map.Entry<Integer, Integer> edge : graph) {
            from = edge.getKey();
            to = edge.getValue();
            if (from < to) {
                chromosome.add(from);
                chromosome.add(to);
            } else {
                chromosome.add(from);
                chromosome.addFirst(to);
                genome.add(BA6G.cycleToChromosome(chromosome));
                chromosome = new ArrayList<>();
            }
        }

        return genome;
    }

    public static List<List<Integer>> graphToGenome(Path path) {
        String strGraph = UTIL.readDataset(path).getFirst();
        int lParenIndex, rParenIndex;
        List<Map.Entry<Integer, Integer>> graph = new ArrayList<>();

        while (true) {
            lParenIndex = strGraph.indexOf('(');
            rParenIndex = strGraph.indexOf(')');
            if (lParenIndex == -1 || rParenIndex == -1) {
                break;
            }
            List<Integer> nodes = Arrays.stream(strGraph.substring(lParenIndex + 1, rParenIndex)
                    .split(", "))
                    .map(Integer::parseInt)
                    .toList();
            graph.add(Map.entry(nodes.getFirst(), nodes.getLast()));
            strGraph = strGraph.substring(rParenIndex + 1);
        }

        return graphToGenome(graph);
    }

    private void run() {
        List<List<Integer>> genome = graphToGenome(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6i.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba6i_out.txt")) {
            for (List<Integer> chromosome : genome) {
                int chromosomeSize = chromosome.size(), syntenyBlock;
                fileWriter.write('(');
                for (int i = 0; i < chromosomeSize; ++i) {
                    syntenyBlock = chromosome.get(i);
                    fileWriter.write("%s%d%c".formatted(
                            (syntenyBlock > 0) ? "+" : "",
                            syntenyBlock,
                            (i == chromosomeSize - 1) ? ')' : ' '
                    ));
                }
            }
            fileWriter.write('\n');
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA6I().run();
    }
}
