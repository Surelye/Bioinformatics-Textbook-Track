// Find Frequent Words with Mismatches and Reverse Complements
// -----------------------------------------------------------
//
// We now extend “Find the Most Frequent Words with Mismatches in a String” to find frequent words
// with both mismatches and reverse complements. Recall that Pattern refers to the reverse
// complement of Pattern.
//
// --------------------------------------------------------------
// Frequent Words with Mismatches and Reverse Complements Problem
//
// Find the most frequent k-mers (with mismatches and reverse complements) in a DNA string.
//
// Given: A DNA string Text as well as integers k and d.
//
// Return: All k-mers Pattern maximizing the sum Count_d(Text, Pattern) + Count_d(Text, Pattern)
// over all possible k-mers.
//
// Sample Dataset
// --------------
// ACGTTGCATGTCGCATGATGCATGAGAGCT
// 4 1
// --------------
//
// Sample Output
// -------------
// ATGT ACAT
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA1J {

    private static List<String> frequentWordsWithMismatchesAndReverseComplementsMachinery(String text, int k, int d) {
        int textLength = text.length();
        int numKMers = (int)Math.pow(4, k);
        int[] close = new int[numKMers];
        int[] frequencyArray = new int[numKMers];
        List<String> frequentPatterns = new ArrayList<>();

        for (int i = 0; i < textLength - k + 1; ++i) {
            List<String> neighborhood = BA1N.neighbors(text.substring(i, i + k), d);

            for (String pattern : neighborhood) {
                int index = (int)BA1L.patternToNumber(pattern);
                close[index] = 1;
            }
        }

        for (int i = 0; i < numKMers; ++i) {
            if (close[i] == 1) {
                String pattern = BA1M.numberToPattern(i, k);
                String patternRevComp = BA1C.reverseComplement(pattern);
                frequencyArray[i] = BA1H.approximatePatternCount(pattern, text, d) +
                        BA1H.approximatePatternCount(patternRevComp, text, d);
            }
        }
        int maxCount = Arrays
                .stream(frequencyArray)
                .max()
                .orElse(-1);

        for (int i = 0; i < numKMers; ++i) {
            if (frequencyArray[i] == maxCount) {
                String pattern = BA1M.numberToPattern(i, k);
                frequentPatterns.add(pattern);
            }
        }
        UTIL.writeToFile(frequentPatterns);

        return frequentPatterns;
    }

    public static List<String> frequentWordsWithMismatchesAndReverseComplements(String text, int k, int d) {
        List<String> frequentKMers = frequentWordsWithMismatchesAndReverseComplementsMachinery(text, k, d);
        UTIL.writeToFile(frequentKMers);

        return frequentKMers;
    }

    public static List<String> frequentWordsWithMismatchesAndReverseComplements(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String text = sampleDataset.getFirst();
        List<Integer> intParams = UTIL.parseIntArray(sampleDataset.getLast());

        return frequentWordsWithMismatchesAndReverseComplements(text, intParams.getFirst(), intParams.getLast());
    }
}
