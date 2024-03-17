// Implement CycleToChromosome
// ---------------------------
// The process described in “Implement ChromosomeToCycle” is in fact invertible, as described by
// the following pseudocode.
//
// ------------------------
// CycleToChromosome(Nodes)
//  for j ← 1 to |Nodes|/2
//       if Node2j−1 < Node2j
//            Chromosomej ← Node2j /2
//       else
//            Chromosomej ← −Node2j−1/2
//  return Chromosome
// ------------------------
//
// Cycle To Chromosome Problem
//
// Solve the Cycle to Chromosome Problem.
//
// Given: A sequence Nodes of integers between 1 and 2n.
//
// Return: The chromosome Chromosome containing n synteny blocks resulting from applying
// CycleToChromosome to Nodes.
//
// Sample Dataset
// --------------
// (1 2 4 3 6 5 7 8)
// --------------
//
// Sample Output
// -------------
// (+1 -2 -3 +4)
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA6G {

    public static List<Integer> cycleToChromosome(List<Integer> cycle) {
        int cycleSize = cycle.size(), curNode, nextNode;
        List<Integer> chromosome = new ArrayList<>(cycleSize >> 1);

        for (int i = 0; i < cycleSize / 2; ++i) {
            curNode = cycle.get(i << 1);
            nextNode = cycle.get((i << 1) + 1);
            chromosome.add(
                    ((curNode < nextNode) ? nextNode : -curNode) / 2
            );
        }

        return chromosome;
    }

    public static List<Integer> cycleToChromosome(Path path) {
        List<Integer> cycle = BA6UTIL.parsePermutation(path);

        return cycleToChromosome(cycle);
    }

    private void run() {
        List<Integer> chromosome = cycleToChromosome(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6g.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba6g_out.txt")) {
            int chromosomeSize = chromosome.size(), blockNum;
            fileWriter.write('(');
            for (int i = 0; i < chromosomeSize; ++i) {
                blockNum = chromosome.get(i);
                fileWriter.write("%s%d%c".formatted(
                        (blockNum > 0) ? '+' : "",
                        blockNum,
                        (i == chromosomeSize - 1) ? ')' : ' '
                ));
            }
            fileWriter.write('\n');
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA6G().run();
    }
}
