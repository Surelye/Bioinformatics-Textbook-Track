// Find a Position in a Genome Minimizing the Skew
// -----------------------------------------------
//
// Define the skew of a DNA string Genome, denoted Skew(Genome), as the difference between the
// total number of occurrences of 'G' and 'C' in Genome. Let Prefix_i (Genome) denote the prefix
// (i.e., initial substring) of Genome of length i. For example, the values of
// Skew(Prefix_i ("CATGGGCATCGGCCATACGCC")) are:
// 0 -1 -1 -1 0 1 2 1 1 1 0 1 2 1 0 0 0 0 -1 0 -1 -2
//
// --------------------
// Minimum Skew Problem
//
// Find a position in a genome minimizing the skew.
//
// Given: A DNA string Genome.
//
// Return: All integer(s) i minimizing Skew(Prefix_i (Text)) over all values of i (from 0 to
// |Genome|).
//
// Sample Dataset
// --------------
// CCTATCGGTGGATTAGCATGTCCCTGTACGTTTCGCCGCGAACTAGTTCACACGGCTTGATGGCAAATGGTTTTTCCGGCGACCGTAATCGTCCACCGAG
// --------------
//
// Sample Output
// -------------
// 53 97
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BA1F {

    private static List<Integer> minimumSkewMachinery(String genome) {
        int genomeLength = genome.length();
        int[] skew = new int[genomeLength + 1];
        List<Integer> minPositions = new ArrayList<>();

        for (int i = 0; i < genomeLength; ++i) {
            skew[i + 1] = skew[i] +
                    switch (genome.charAt(i)) {
                        case 'A', 'T' -> 0;
                        case 'C' -> -1;
                        case 'G' -> 1;
                        default -> throw new RuntimeException("Incorrect nucleotide %c at position %d"
                                .formatted(genome.charAt(i), i));
                    };
        }
        int minSkew = Arrays
                .stream(skew)
                .min()
                .orElseThrow();

        for (int i = 0; i < genomeLength + 1; ++i) {
            if (skew[i] == minSkew) {
                minPositions.add(i);
            }
        }

        UTIL.writeToFile(minPositions);

        return minPositions;
    }

    public static List<Integer> minimumSkew(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String genome = sampleDataset.getFirst();

        return minimumSkewMachinery(genome);
    }

    public static List<Integer> minimumSkew(String genome) {
        return minimumSkewMachinery(genome);
    }
}
