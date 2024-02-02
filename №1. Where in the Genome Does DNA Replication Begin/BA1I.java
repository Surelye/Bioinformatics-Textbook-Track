// Find the Most Frequent Words with Mismatches in a String
// --------------------------------------------------------
//
// We defined a mismatch in “Compute the Hamming Distance Between Two Strings”. We now generalize
// “Find the Most Frequent Words in a String” to incorporate mismatches as well.
//
// Given strings Text and Pattern as well as an integer d, we define Countd(Text, Pattern) as the
// total number of occurrences of Pattern in Text with at most d mismatches. For example,
// Count_1(AACAAGCTGATAAACATTTAAAGAG, AAAAA) = 4 because AAAAA appears four times in this string
// with at most one mismatch: AACAA, ATAAA, AAACA, and AAAGA. Note that two of these occurrences
// overlap.
//
// A most frequent k-mer with up to d mismatches in Text is simply a string Pattern maximizing
// Count_d(Text, Pattern) among all k-mers. Note that Pattern does not need to actually appear as a
// substring of Text; for example, AAAAA is the most frequent 5-mer with 1 mismatch in
// AACAAGCTGATAAACATTTAAAGAG, even though AAAAA does not appear exactly in this string. Keep this
// in mind while solving the following problem.
//
// --------------------------------------
// Frequent Words with Mismatches Problem
//
// Find the most frequent k-mers with mismatches in a string.
//
// Given: A string Text as well as integers k and d.
//
// Return: All most frequent k-mers with up to d mismatches in Text.
//
// Sample Dataset
// --------------
// ACGTTGCATGTCGCATGATGCATGAGAGCT
// 4 1
// --------------
//
// Sample Output
// -------------
// GATG ATGC ATGT
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA1I {

    private static List<String> frequentWordsWithMismatchesMachinery(String text, int k, int d) {
        int numKMers = (int)Math.pow(4, k);
        int textLength = text.length();
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
                frequencyArray[i] = BA1H.approximatePatternMatching(pattern, text, d).size();
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

    public static List<String> frequentWordsWithMismatches(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String text = sampleDataset.getFirst();
        List<Integer> intParams = UTIL.parseIntArray(sampleDataset.getLast());

        return frequentWordsWithMismatchesMachinery(text, intParams.getFirst(), intParams.getLast());
    }

    public static List<String> frequentWordsWithMismatches(String text, int k, int d) {
        return frequentWordsWithMismatchesMachinery(text, k, d);
    }
}
