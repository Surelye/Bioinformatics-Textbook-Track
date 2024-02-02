// Generate the Frequency Array of a String
// ----------------------------------------
//
// Given an integer k, we define the frequency array of a string Text as an array of length 4^k,
// where the i-th element of the array holds the number of times that the i-th k-mer (in the
// lexicographic order) appears in Text.
//
// ---------------------------
// Computing a Frequency Array
//
// Generate the frequency array of a DNA string.
//
// Given: A DNA string Text and an integer k.
//
// Return: The frequency array of k-mers in Text.
//
// Sample Dataset
// --------------
// ACGCGGCTCTGAAA
// 2
// --------------
//
// Sample Output
// -------------
// 2 1 0 0 0 0 2 2 1 2 1 0 0 1 1 0
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA1K {

    private static List<Integer> computingFrequenciesMachinery(String text, int k) {
        int textLength = text.length();
        List<Integer> frequencyArray = new ArrayList<>();
        for (int i = 0; i < Math.pow(4, k); ++i) {
            frequencyArray.add(0);
        }

        for (int i = 0; i < textLength - k + 1; ++i) {
            String pattern = text.substring(i, i + k);
            int j = (int)BA1L.patternToNumber(pattern);
            frequencyArray.set(j, frequencyArray.get(j) + 1);
        }

        UTIL.writeToFile(frequencyArray);

        return frequencyArray;
    }

    public static List<Integer> computingFrequencies(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String text = sampleDataset.getFirst();
        int k = Integer.parseInt(sampleDataset.getLast());

        return computingFrequenciesMachinery(text, k);
    }

    public static List<Integer> computingFrequencies(String text, int k) {
        return computingFrequenciesMachinery(text, k);
    }
}
