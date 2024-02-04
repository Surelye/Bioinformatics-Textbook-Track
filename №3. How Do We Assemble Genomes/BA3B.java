// Reconstruct a String from its Genome Path
// -----------------------------------------
//
// String Spelled by a Genome Path Problem
//
// Find the string spelled by a genome path.
//
// Given: A sequence of k-mers Pattern_1, ... , Pattern_n such that the last k - 1 symbols of
// Pattern_i are equal to the first k - 1 symbols of Pattern_{i+1} for i from 1 to n-1.
//
// Return: A string Text of length k+n-1 where the i-th k-mer in Text is equal to Pattern_i for all
// i.
//
// Sample Dataset
// --------------
// ACCGA
// CCGAA
// CGAAG
// GAAGC
// AAGCT
// --------------
//
// Sample Output
// -------------
// ACCGAAGCT
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA3B {

    private static String reconstructAStringFromItsGenomePathMachinery(List<String> sequence) {
        String fPattern = sequence.getFirst();
        int patternLength = fPattern.length(),
                sequenceSize = sequence.size();
        StringBuilder text = new StringBuilder(fPattern);

        for (int i = 1; i < sequenceSize; ++i) {
            text.append(sequence.get(i).charAt(patternLength - 1));
        }
        UTIL.writeToFile(List.of(text.toString()));

        return text.toString();
    }

    public static String reconstructAStringFromItsGenomePath(Path path) {
        return reconstructAStringFromItsGenomePathMachinery(UTIL.readDataset(path));
    }

    public static String reconstructAStringFromItsGenomePath(List<String> sequence) {
        return reconstructAStringFromItsGenomePathMachinery(sequence);
    }
}
