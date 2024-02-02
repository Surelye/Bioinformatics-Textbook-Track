// Generate the d-Neighborhood of a String
// ---------------------------------------
//
// The d-neighborhood Neighbors(Pattern, d) is the set of all k-mers whose Hamming distance from
// Pattern does not exceed d.
//
// ---------------------------------------
// Generate the d-Neighborhood of a String
//
// Find all the neighbors of a pattern.
//
// Given: A DNA string Pattern and an integer d.
//
// Return: The collection of strings Neighbors(Pattern, d).
//
// Sample Dataset
// --------------
// ACG
// 1
// --------------
//
// Sample Output
// -------------
// CCG
// TCG
// GCG
// AAG
// ATG
// AGG
// ACA
// ACC
// ACT
// ACG
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA1N {

    private static final List<String> nucleotides = List.of("A", "C", "G", "T");

    private static List<String> neighborsMachinery(String pattern, int d) {
        if (d == 0) {
            return List.of(pattern);
        }
        if (pattern.length() == 1) {
            return nucleotides;
        }

        List<String> neighborhood = new ArrayList<>();
        String firstSymbol = Character.toString(pattern.charAt(0));
        String suffix = pattern.substring(1);
        List<String> suffixNeighborhood = neighborsMachinery(suffix, d);

        for (String neighbor : suffixNeighborhood) {
            if (BA1G.HammingDistance(suffix, neighbor) < d) {
                for (String nucleotide : nucleotides) {
                    neighborhood.add(nucleotide.concat(neighbor));
                }
            } else {
                neighborhood.add(firstSymbol.concat(neighbor));
            }
        }

        return neighborhood;
    }

    public static List<String> neighbors(String pattern, int d) {
        List<String> neighborhood = neighborsMachinery(pattern, d);

        UTIL.writeToFileWithNewlines(neighborhood);

        return neighborhood;
    }

    public static List<String> neighbors(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String pattern = sampleDataset.getFirst();
        int d = Integer.parseInt(sampleDataset.getLast());

        return neighbors(pattern, d);
    }
}
