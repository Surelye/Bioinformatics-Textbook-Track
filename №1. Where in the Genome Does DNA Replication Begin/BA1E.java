// Find Patterns Forming Clumps in a String
// ----------------------------------------
//
// Given integers L and t, a string Pattern forms an (L, t)-clump inside a (larger) string Genome
// if there is an interval of Genome of length L in which Pattern appears at least t times. For
// example, TGCA forms a (25,3)-clump in the following Genome:
// gatcagcataagggtcccTGCAATGCATGACAAGCCTGCAgttgttttac.
//
// ---------------------
// Clump Finding Problem
//
// Find patterns forming clumps in a string.
//
// Given: A string Genome, and integers k, L, and t.
//
// Return: All distinct k-mers forming (L, t)-clumps in Genome.
//
// Sample Dataset
// --------------
// CGGACTCGACAGATGTGAAGAAATGTGAAGACTGAGTGAAGAGAAGAGGAAACACGACACGACATTGCGACATAATGTACGAATGTAATGTGCCTATGGC
// 5 75 4
// --------------
//
// Sample Output
// -------------
// CGACA GAAGA AATGT
// -------------

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BA1E {

    private static Set<String> clumpFindingMachinery(String genome, int k, int L, int t) {
        int genomeLength = genome.length();
        Set<String> clumpKMers = new HashSet<>();

        for (int i = 0; i < genomeLength - L + 1; ++i) {
            String clump = genome.substring(i, i + L);
            for (int j = 0; j < L - k + 1; ++j) {
                String pattern = clump.substring(j, j + k);
                if (BA1A.patternCount(clump, pattern) >= t) {
                    clumpKMers.add(pattern);
                }
            }
        }

        UTIL.writeToFile(clumpKMers);

        return clumpKMers;
    }

    public static Set<String> clumpFinding(String genome, int k, int L, int t) {
        return clumpFindingMachinery(genome, k, L, t);
    }

    public static Set<String> clumpFinding(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String genome = sampleDataset.getFirst();
        List<Integer> intParams = UTIL.parseIntArray(sampleDataset.getLast());
        int k = intParams.getFirst(), L = intParams.get(1), t = intParams.getLast();

        return clumpFindingMachinery(genome, k, L, t);
    }
}
