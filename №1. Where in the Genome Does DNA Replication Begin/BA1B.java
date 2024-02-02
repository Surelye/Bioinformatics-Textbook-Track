// Find the Most Frequent Words in a String
// ----------------------------------------
//
// We say that Pattern is a most frequent k-mer in Text if it maximizes Count(Text, Pattern) among
// all k-mers. For example, "ACTAT" is a most frequent 5-mer in "ACAACTATGCATCACTATCGGGAACTATCCT",
// and "ATA" is a most frequent 3-mer of "CGATATATCCATAG".
//
// ----------------------
// Frequent Words Problem
//
// Find the most frequent k-mers in a string.
//
// Given: A DNA string Text and an integer k.
//
// Return: All most frequent k-mers in Text (in any order).
//
// Sample Dataset
// --------------
// ACGTTGCATGTCGCATGATGCATGAGAGCT
// 4
// --------------
//
// Sample Output
// -------------
// CATG GCAT
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA1B {

    private static Set<String> frequentWordsMachinery(String text, int k) {
        int textLength = text.length();
        int[] count = new int[textLength - k + 1];
        Set<String> frequentPatterns = new HashSet<>();

        for (int i = 0; i < textLength - k + 1; ++i) {
            String pattern = text.substring(i, i + k);
            count[i] = BA1A.patternCount(text, pattern);
        }
        int maxCount = Arrays
                .stream(count)
                .max()
                .orElse(-1);

        for (int i = 0; i < textLength - k + 1; ++i) {
            if (count[i] == maxCount) {
                frequentPatterns.add(text.substring(i, i + k));
            }
        }

        UTIL.writeToFile(frequentPatterns);

        return frequentPatterns;
    }

    public static Set<String> frequentWords(String text, int k) {
        return frequentWordsMachinery(text, k);
    }

    public static Set<String> frequentWords(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String text = sampleDataset.getFirst();
        int k = Integer.parseInt(sampleDataset.getLast());

        return frequentWords(text, k);
    }
}
