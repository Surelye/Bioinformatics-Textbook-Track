// Find All Occurrences of a Pattern in a String
// ---------------------------------------------
//
// In this problem, we ask a simple question: how many times can one string occur as a substring of
// another? Recall from “Find the Most Frequent Words in a String” that different occurrences of a
// substring can overlap with each other. For example, ATA occurs three times in CGATATATCCATAG.
//
// ------------------------
// Pattern Matching Problem
//
// Find all occurrences of a pattern in a string.
//
// Given: Strings Pattern and Genome.
//
// Return: All starting positions in Genome where Pattern appears as a substring. Use 0-based
// indexing.
//
// Sample Dataset
// --------------
// ATAT
// GATATATGCATATACTT
// --------------
//
// Sample Output
// -------------
// 1 3 9
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA1D {

    private static List<Integer> patternMatchingMachinery(String pattern, String genome) {
        int patternLength = pattern.length(),
                genomeLength = genome.length();
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < genomeLength - patternLength + 1; ++i) {
            if (genome.substring(i, i + patternLength).equals(pattern)) {
                positions.add(i);
            }
        }

        UTIL.writeToFile(positions);

        return positions;
    }

    public static List<Integer> patternMatching(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String pattern = sampleDataset.getFirst();
        String genome = sampleDataset.getLast();

        return patternMatchingMachinery(pattern, genome);
    }

    public static List<Integer> patternMatching(String pattern, String genome) {
        return patternMatchingMachinery(pattern, genome);
    }
}
