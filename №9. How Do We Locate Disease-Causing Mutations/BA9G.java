// Construct the Suffix Array of a String
// --------------------------------------
//
// In “Find the Longest Repeat in a String”, we saw that suffix trees can be too memory intensive to
// apply in practice.
//
// In 1993, Udi Manber and Gene Myers introduced suffix arrays as a memory-efficient alternative to
// suffix trees. To construct SuffixArray(Text), we first sort all suffixes of Text
// lexicographically, assuming that "$" comes first in the alphabet. The suffix array is the list of
// starting positions of these sorted suffixes. For example,
//
// SuffixArray("panamabananas$") = (13, 5, 3, 1, 7, 9, 11, 6, 4, 2, 8, 10, 0, 12).
//
// Suffix Array Construction Problem
//
// Construct the suffix array of a string.
//
// Given: A string Text.
//
// Return: SuffixArray(Text).
//
// Sample Dataset
// --------------
// AACGATAGCGGTAGA$
// --------------
//
// Sample Output
// -------------
// 15, 14, 0, 1, 12, 6, 4, 2, 8, 13, 3, 7, 9, 10, 11, 5
// -------------

import auxil.Suffix;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BA9G {

    private static List<Integer> constructSuffixArrayMachinery(String str) {
        int strLength = str.length();
        List<Suffix> suffixes = new ArrayList<>(strLength);
        for (int i = 0; i != strLength - 1; ++i) {
            suffixes.add(
                    new Suffix(str.substring(i), i)
            );
        }
        suffixes.sort(Comparator.comparing(Suffix::suffix));
        suffixes.addFirst(new Suffix("$", strLength - 1));

        return suffixes.stream()
                .map(Suffix::position)
                .toList();
    }

    public static List<Integer> constructSuffixArray(String str) {
        return constructSuffixArrayMachinery(str);
    }

    public static List<Integer> constructSuffixArray(Path path) {
        return constructSuffixArrayMachinery(
                UTIL.readDataset(path).getFirst()
        );
    }

    private void run() {
        List<Integer> suffixArray = constructSuffixArray(
                Path.of(
                    "/home/surelye/Downloads/rosalind_files/rosalind_ba9g.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9g_out.txt")) {
            int suffixArraySize = suffixArray.size();
            for (int i = 0; i != suffixArraySize; ++i) {
                fileWriter.write("%d%s".formatted(
                        suffixArray.get(i),
                        (i == suffixArraySize - 1) ? "\n" : ", "
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9G().run();
    }
}
