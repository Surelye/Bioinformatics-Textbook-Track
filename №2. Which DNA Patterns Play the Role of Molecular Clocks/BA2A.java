// Implement MotifEnumeration
// --------------------------
//
// Given a collection of strings Dna and an integer d, a k-mer is a (k,d)-motif if it appears in
// every string from Dna with at most d mismatches. The following algorithm finds (k,d)-motifs.
//
// ------------------------------
//    MOTIFENUMERATION(Dna, k, d)
//        Patterns ← an empty set
//        for each k-mer Pattern in Dna
//            for each k-mer Pattern’ differing from Pattern by at most d
//              mismatches
//                if Pattern' appears in each string from Dna with at most d
//                mismatches
//                    add Pattern' to Patterns
//        remove duplicates from Patterns
//        return Patterns
// -------------------------------
//
// Implanted Motif Problem
//
// Implement MotifEnumeration (shown above) to find all (k, d)-motifs in a collection of strings.
//
// Given: Integers k and d, followed by a collection of strings Dna.
//
// Return: All (k, d)-motifs in Dna.
//
// Sample Dataset
// --------------
// 3 1
// ATTTGGC
// TGCCTTA
// CGGTATC
// GAAAATT
// --------------
//
// Sample Output
// -------------
// ATA ATT GTT TTT
// -------------

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BA2A {

    private static boolean appearsInStringWithAtMostDMismatches(String pattern, String text, int d) {
        int patternLength = pattern.length(),
                textLength = text.length();

        for (int i = 0; i < textLength - patternLength + 1; ++i) {
            if (BA1G.HammingDistance(pattern, text.substring(i, i + patternLength)) <= d) {
                return true;
            }
        }

        return false;
    }

    private static boolean appearsInEachStringWithAtMostDMismatches(String pattern, List<String> DNA, int d) {
        for (String text : DNA) {
            if (!appearsInStringWithAtMostDMismatches(pattern, text, d)) {
                return false;
            }
        }

        return true;
    }

    private static Set<String> motifEnumerationMachinery(int k, int d, List<String> DNA) {
        int textLength = DNA.getFirst().length();
        Set<String> patterns = new HashSet<>();

        for (String text : DNA) {
            for (int i = 0; i < textLength - k + 1; ++i) {
                String pattern = text.substring(i, i + k);
                List<String> neighbors = BA1N.neighbors(pattern, d);

                for (String neighbor : neighbors) {
                    if (appearsInEachStringWithAtMostDMismatches(neighbor, DNA, d)) {
                        patterns.add(neighbor);
                    }
                }
            }
        }
        UTIL.writeToFile(patterns);

        return patterns;
    }

    public static Set<String> motifEnumeration(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> intParams = UTIL.parseIntArray(strDataset.getFirst());

        return motifEnumerationMachinery(intParams.getFirst(), intParams.getLast(),
                strDataset.stream().skip(1).toList());
    }

    public static Set<String> motifEnumeration(int k, int d, List<String> DNA) {
        return motifEnumerationMachinery(k, d, DNA);
    }
}
