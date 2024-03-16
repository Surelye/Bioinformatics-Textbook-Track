// Implement ColoredEdges
// ----------------------
//
// The following algorithm constructs ColoredEdges(P) for a genome P. In this pseudocode, we will
// assume that an n-element array (a1, . . . , an) has an invisible (n + 1)-th element that is
// equal to its first element, i.e., an+1 = a1.
//
// ---------------
// ColoredEdges(P)
//  Edges ← an empty set
//  for each chromosome Chromosome in P
//       Nodes ← ChromosomeToCycle(Chromosome)
//       for j ← 1 to |Chromosome|
//            add the edge (Nodes2j, Nodes2j +1) to Edges
//  return Edges
// ---------------
//
// Colored Edges Problem
//
// Find the Colored Edges in a genome.
//
// Given: A genome P.
//
// Return: The collection of colored edges in the genome graph of P in the form (x, y).
//
// Sample Dataset
// --------------
// (+1 -2 -3)(+4 +5 -6)
// --------------
//
// Sample Output
// -------------
// (2, 4), (3, 6), (5, 1), (8, 9), (10, 12), (11, 7)
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BA6H {

    private List<Map.Entry<Integer, Integer>> coloredEdgesMachinery(List<List<Integer>> genome) {
        List<Integer> cycle;
        int chromosomeSize;
        List<Map.Entry<Integer, Integer>> edges = new ArrayList<>();

        for (List<Integer> chromosome : genome) {
            chromosomeSize = chromosome.size();
            cycle = BA6F.chromosomeToCycle(chromosome);
            cycle.add(cycle.getFirst());
            for (int i = 0; i < chromosomeSize; ++i) {
                edges.add(
                        Map.entry(cycle.get((i << 1) + 1), cycle.get((i + 1) << 1))
                );
            }
        }

        return edges;
    }

    public List<Map.Entry<Integer, Integer>> coloredEdges(List<List<Integer>> genome) {
        return coloredEdgesMachinery(genome);
    }

    public List<Map.Entry<Integer, Integer>> coloredEdges(Path path) {
        String strGenome = UTIL.readDataset(path).getFirst();
        int lParenIndex, rParenIndex;
        List<List<Integer>> genome = new ArrayList<>();

        while (true) {
            lParenIndex = strGenome.indexOf('(');
            rParenIndex = strGenome.indexOf(')');
            if (lParenIndex == -1 || rParenIndex == -1) {
                break;
            }
            genome.add(
                    Arrays.stream(strGenome.substring(lParenIndex + 1, rParenIndex)
                                    .split("\\s+"))
                            .map(Integer::parseInt)
                            .toList()
            );
            strGenome = strGenome.substring(rParenIndex + 1);
        }

        return coloredEdgesMachinery(genome);
    }

    private void run() {
        List<Map.Entry<Integer, Integer>> coloredEdges = coloredEdges(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6h.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba6h_out.txt")) {
            int edgesSize = coloredEdges.size();
            Map.Entry<Integer, Integer> edge;
            for (int i = 0; i < edgesSize; ++i) {
                edge = coloredEdges.get(i);
                fileWriter.write("(%d, %d)%s".formatted(
                        edge.getKey(),
                        edge.getValue(),
                        (i == edgesSize - 1) ? '\n' : ", "
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA6H().run();
    }
}
