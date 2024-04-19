// Construct the Partial Suffix Array of a String
// ----------------------------------------------
//
// To construct the partial suffix array SuffixArray_{k}(Text), we first need to construct the full
// suffix array and then retain only the elements of this array that are divisible by K, along with
// their indices i.
//
// Partial Suffix Array Construction Problem
//
// Construct the partial suffix array of a string.
//
// Given: A string Text and a positive integer K.
//
// Return: SuffixArrayK(Text), in the form of a list of ordered pairs (i, SuffixArray(i)) for all
// nonempty entries in the partial suffix array.
//
// Sample Dataset
// --------------
// PANAMABANANAS$
// 5
// --------------
//
// Sample Output
// -------------
// 1,5
// 11,10
// 12,0
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA9Q {

    private static Map<Integer, Integer> constructPartialSuffixArrayMachinery(String text, int K) {
        List<Integer> suffixArray = BA9G.constructSuffixArray(text);
        int textLength = text.length();
        Map<Integer, Integer> partialSuffixArray = new HashMap<>();
        for (int i = 0; i != textLength; ++i) {
            if (suffixArray.get(i) % K == 0) {
                partialSuffixArray.put(i, suffixArray.get(i));
            }
        }

        return partialSuffixArray;
    }

    public static Map<Integer, Integer> constructPartialSuffixArray(String text, int K) {
        return constructPartialSuffixArrayMachinery(text, K);
    }

    public static Map<Integer, Integer> constructPartialSuffixArray(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return constructPartialSuffixArrayMachinery(
                strDataset.getFirst(),
                Integer.parseInt(strDataset.getLast())
        );
    }

    private void run() {
        Map<Integer, Integer> partialSuffixArray = constructPartialSuffixArray(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9q.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9q_out.txt")) {
            for (int i : partialSuffixArray.keySet()) {
                fileWriter.write("%d,%d\n".formatted(
                        i,
                        partialSuffixArray.get(i)
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9Q().run();
    }
}
