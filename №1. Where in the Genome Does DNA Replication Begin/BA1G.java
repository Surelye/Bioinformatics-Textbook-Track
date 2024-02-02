// Compute the Hamming Distance Between Two Strings
// ------------------------------------------------
//
// We say that position i in k-mers p1 … pk and q1 … qk is a mismatch if pi ≠ qi. For example,
// CGAAT and CGGAC have two mismatches. The number of mismatches between strings p and q is called
// the Hamming distance between these strings and is denoted HammingDistance(p, q).
//
// ------------------------
// Hamming Distance Problem
//
// Compute the Hamming distance between two DNA strings.
//
// Given: Two DNA strings.
//
// Return: An integer value representing the Hamming distance.
//
// Sample Dataset
// --------------
// GGGCCGTTGGT
// GGACCGTTGAC
// --------------
//
// Sample Output
// -------------
// 3
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA1G {

    private static int HammingDistanceMachinery(String fDNA, String sDNA) {
        int mismatches = 0;
        int fDNALength = fDNA.length(),
                sDNALength = sDNA.length();
        assert fDNALength == sDNALength;

        for (int i = 0; i < fDNALength; ++i) {
            if (fDNA.charAt(i) != sDNA.charAt(i)) {
                ++mismatches;
            }
        }

        return mismatches;
    }

    public static int HammingDistance(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String fDNA = sampleDataset.getFirst(),
                sDNA = sampleDataset.getLast();

        return HammingDistanceMachinery(fDNA, sDNA);
    }

    public static int HammingDistance(String fDNA, String sDNA) {
        return HammingDistanceMachinery(fDNA, sDNA);
    }
}
