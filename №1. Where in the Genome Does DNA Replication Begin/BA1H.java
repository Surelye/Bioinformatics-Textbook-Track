// Find All Approximate Occurrences of a Pattern in a String
// ---------------------------------------------------------
//
// We say that a k-mer Pattern appears as a substring of Text with at most d mismatches if there is
// some k-mer substring Pattern' of Text having d or fewer mismatches with Pattern, i.e.,
// HammingDistance(Pattern, Pattern') ≤ d. Our observation that a DnaA box may appear with slight
// variations leads to the following generalization of the Pattern Matching Problem.
//
// ------------------------------------
// Approximate Pattern Matching Problem
//
// Find all approximate occurrences of a pattern in a string.
//
// Given: Strings Pattern and Text along with an integer d.
//
// Return: All starting positions where Pattern appears as a substring of Text with at most d
// mismatches.
//
// Sample Dataset
// --------------
// ATTCTGGA
// CGCCCGAATCCAGAACGCATTCCCATATTTCGGGACCACTGGCCTCCACGGTACGGACGTCAATCAAATGCCTAGCGGCTTGTGGTTTCTCCTACGCTCC
// 3
// --------------
//
// Sample Output
// -------------
// 6 7 26 27 78
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA1H {

    private static List<Integer>
    approximatePatternMatchingMachinery(String pattern, String text, int d) {
        int patternLength = pattern.length(),
                textLength = text.length();
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < textLength - patternLength + 1; ++i) {
            if (BA1G.HammingDistance(text.substring(i, i + patternLength), pattern) <= d) {
                positions.add(i);
            }
        }

        UTIL.writeToFile(positions);

        return positions;
    }

    private static int approximatePatternCountMachinery(String pattern, String text, int d) {
        int patternLength = pattern.length(),
                textLength = text.length();
        int count = 0;

        for (int i = 0; i < textLength - patternLength + 1; ++i) {
            if (BA1G.HammingDistance(text.substring(i, i + patternLength), pattern) <= d) {
                ++count;
            }
        }

        return count;
    }

    public static int approximatePatternCount(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String pattern = sampleDataset.getFirst(),
                text = sampleDataset.get(1);
        int d = Integer.parseInt(sampleDataset.getLast());

        return approximatePatternCountMachinery(pattern, text, d);
    }

    public static int approximatePatternCount(String pattern, String text, int d) {
        return approximatePatternCountMachinery(pattern, text, d);
    }

    public static List<Integer> approximatePatternMatching(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String pattern = sampleDataset.getFirst(),
                text = sampleDataset.get(1);
        int d = Integer.parseInt(sampleDataset.getLast());

        return approximatePatternMatchingMachinery(pattern, text, d);
    }

    public static List<Integer> approximatePatternMatching(String pattern, String text, int d) {
        return approximatePatternMatchingMachinery(pattern, text, d);
    }
}
