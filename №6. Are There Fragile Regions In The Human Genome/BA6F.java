// Implement ChromosomeToCycle
// ---------------------------
//
// The following pseudocode bypasses the intermediate step of assigning “head” and “tail” nodes in
// order to transform a single circular chromosome Chromosome = (Chromosome1, . . . , Chromosomen)
// into a cycle represented as a sequence of integers Nodes = (Nodes1, . . . , Nodes2n).
//
// -----------------------------
// ChromosomeToCycle(Chromosome)
//  for j ← 1 to |Chromosome|
//       i ← Chromosomej
//       if i > 0
//            Node2j−1 ←2i−1
//            Node2j ← 2i
//       else
//            Node2j−1 ← -2i
//            Node2j ←-2i−1
//  return Nodes
// -----------------------------
//
// Chromosome To Cycle Problem
//
// Solve the Chromosome To Cycle Problem.
//
// Given: A chromosome Chromosome containing n synteny blocks.
//
// Return: The sequence Nodes of integers between 1 and 2n resulting from applying ChromosomeToCycle
// to Chromosome.
//
// Sample Dataset
// --------------
// (+1 -2 -3 +4)
// --------------
//
// Sample Output
// -------------
// (1 2 4 3 6 5 7 8)
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA6F {

    public static List<Integer> chromosomeToCycle(List<Integer> chromosome) {
        int i;
        List<Integer> cycle = new ArrayList<>();

        for (Integer blockNum : chromosome) {
            i = blockNum << 1;
            if (i > 0) {
                cycle.add(i - 1);
                cycle.add(i);
            } else {
                cycle.add(-i);
                cycle.add(-i - 1);
            }
        }

        return cycle;
    }

    public static List<Integer> chromosomeToCycle(Path path) {
        List<Integer> chromosome = BA6UTIL.parsePermutation(path);

        return chromosomeToCycle(chromosome);
    }

    private void run() {
        List<Integer> cycle = chromosomeToCycle(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6f.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba6f_out.txt")) {
            int cycleSize = cycle.size();
            fileWriter.write('(');
            for (int i = 0; i < cycleSize; ++i) {
                fileWriter.write("%d%c".formatted(
                        cycle.get(i), (i == cycleSize - 1) ? ')' : ' '
                ));
            }
            fileWriter.write('\n');
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA6F().run();
    }
}
