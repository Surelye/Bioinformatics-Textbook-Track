// Find a Median String
// --------------------
//
// Given a k-mer Pattern and a longer string Text, we use d(Pattern, Text) to denote the minimum
// Hamming distance between Pattern and any k-mer in Text, d(Pattern,Text)=
// min_{all k-mers Pattern' in Text} HammingDistance(Pattern,Pattern′).
//
// Given a k-mer Pattern and a set of strings Dna = {Dna1, … , Dnat}, we define d(Pattern, Dna) as
// the sum of distances between Pattern and all strings in Dna, d(Pattern,Dna)=∑_{i=1}^t
// d(Pattern,Dnai).
//
// Our goal is to find a k-mer Pattern that minimizes d(Pattern, Dna) over all k-mers Pattern, the
// same task that the Equivalent Motif Finding Problem is trying to achieve. We call such a k-mer a
// median string for Dna.
//
// ---------------------
// Median String Problem
//
// Find a median string.
//
// Given: An integer k and a collection of strings Dna.
//
// Return: A k-mer Pattern that minimizes d(Pattern, Dna) over all k-mers Pattern. (If multiple
// answers exist, you may return any one.)
//
// Sample Dataset
// --------------
// 3
// AAATTGACGCAT
// GACGACCACGTT
// CGTCAGCGCCTG
// GCTGAGCACCGG
// AGTACGGGACAG
// --------------
//
// Sample Output
// -------------
// GAC
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA2B {

    private static String medianStringMachinery(List<String> DNA, int k) {
        int distance = Integer.MAX_VALUE,
                numKMers = (int)Math.pow(4, k),
                curDist;
        String pattern, median = DNA.getFirst().substring(0, k);

        for (int i = 0; i < numKMers; ++i) {
            pattern = BA1M.numberToPattern(i, k);
            curDist = BA2H.distanceBetweenPatternAndStrings(pattern, DNA);

            if (distance > curDist) {
                distance = curDist;
                median = pattern;
            }
        }

        return median;
    }

    public static String medianString(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return medianStringMachinery(strDataset.stream().skip(1).toList(),
                Integer.parseInt(strDataset.getFirst()));
    }

    public static String medianString(List<String> DNA, int k) {
        return medianStringMachinery(DNA, k);
    }
}
