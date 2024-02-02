// Find the Reverse Complement of a String
// ---------------------------------------
//
// In DNA strings, symbols 'A' and 'T' are complements of each other, as are 'C' and 'G'. Given a
// nucleotide p, we denote its complementary nucleotide as p. The reverse complement of a DNA string
// Pattern = p1…pn is the string Pattern = pn … p1 formed by taking the complement of each
// nucleotide in Pattern, then reversing the resulting string.
//
// For example, the reverse complement of Pattern = "GTCA" is Pattern = "TGAC".
//
// --------------------------
// Reverse Complement Problem
//
// Find the reverse complement of a DNA string.
//
// Given: A DNA string Pattern.
//
// Return: Pattern, the reverse complement of Pattern.
//
// Sample Dataset
// --------------
// AAAACCCGGT
// --------------
//
// Sample Output
// -------------
// ACCGGGTTTT
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA1C {

    private static char getNucleotideComplement(char nucleotide) {
        return
                switch (nucleotide) {
                    case 'A' -> 'T';
                    case 'C' -> 'G';
                    case 'G' -> 'C';
                    case 'T' -> 'A';
                    default -> throw new RuntimeException("Incorrect nucleotide provided: %c".formatted(nucleotide));
                };
    }

    private static String reverseComplementMachinery(String pattern) {
        int patternLength = pattern.length();
        StringBuilder reverseComplement = new StringBuilder(patternLength);

        for (int i = patternLength - 1; i > -1; --i) {
            reverseComplement.append(getNucleotideComplement(pattern.charAt(i)));
        }
        String reverseComplementAns = reverseComplement.toString();

        UTIL.writeToFile(List.of(reverseComplementAns));

        return reverseComplementAns;
    }

    public static String reverseComplement(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String pattern = sampleDataset.getFirst();

        return reverseComplementMachinery(pattern);
    }

    public static String reverseComplement(String pattern) {
        return reverseComplementMachinery(pattern);
    }
}
