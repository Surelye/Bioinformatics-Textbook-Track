// Implement DistanceBetweenPatternAndStrings
// ------------------------------------------
//
// The first potential issue with implementing MedianString from “Find a Median String” is writing a
// function to compute d(Pattern, Dna) = ∑ti=1 d(Pattern, Dnai), the sum of distances between
// Pattern and each string in Dna = {Dna1, ..., Dnat}. This task is achieved by the following
// pseudocode.
//
// ----------------------------------------------
// DistanceBetweenPatternAndStrings(Pattern, Dna)
//     k ← |Pattern|
//     distance ← 0
//     for each string Text in Dna
//         HammingDistance ← ∞
//         for each k-mer Pattern’ in Text
//             if HammingDistance > HammingDistance(Pattern, Pattern’)
//                 HammingDistance ← HammingDistance(Pattern, Pattern’)
//         distance ← distance + HammingDistance
//     return distance
// -----------------------------------------------
//
// Compute DistanceBetweenPatternAndStrings
//
// Find the distance between a pattern and a set of strings.
//
// Given: A DNA string Pattern and a collection of DNA strings Dna.
//
// Return: DistanceBetweenPatternAndStrings(Pattern, Dna).
//
// Sample Dataset
// --------------
// AAA
// TTACCTTAAC GATATCTGTC ACGGCGTTCG CCCTAAAGAG CGTCAGAGGT
// --------------
//
// Sample Output
// -------------
// 5
// -------------

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class BA2H {

    private static int
    distanceBetweenPatternAndStringsMachinery(String pattern, List<String> DNA) {
        int k = pattern.length(),
                textLength = DNA.getFirst().length();
        int distance = 0, curHamDist;

        for (String text : DNA) {
            int HammingDistance = Integer.MAX_VALUE;
            for (int i = 0; i < textLength - k + 1; ++i) {
                curHamDist = BA1G.HammingDistance(pattern, text.substring(i, i + k));
                if (HammingDistance > curHamDist) {
                    HammingDistance = curHamDist;
                }
            }
            distance += HammingDistance;
        }

        return distance;
    }

    public static int distanceBetweenPatternAndStrings(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return distanceBetweenPatternAndStringsMachinery(strDataset.getFirst(),
                Arrays.stream(strDataset.get(1).split("\\s+")).toList());
    }

    public static int distanceBetweenPatternAndStrings(String pattern, List<String> DNA) {
        return distanceBetweenPatternAndStringsMachinery(pattern, DNA);
    }
}
