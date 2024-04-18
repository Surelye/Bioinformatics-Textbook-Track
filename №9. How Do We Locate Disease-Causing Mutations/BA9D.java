// Find the Longest Repeat in a String
// -----------------------------------
//
// Although the suffix tree decreases memory requirements from O(|Text|^2) to O(|Text|), on average
// it still requires about 20 times as much memory as Text. In the case of a 3 GB human genome,
// 60 GB of RAM still presents a memory challenge for most machines. This reveals a dark secret of
// big-O notation, which is that it ignores constant factors. For long strings such as the human
// genome, we will need to pay attention to this constant factor, since the expression O(|Text|)
// applies to both an algorithm with 2·|Text| memory and an algorithm with 1000·|Text| memory.
//
// Yet before seeing how we can further reduce the memory needed for multiple pattern matching, we
// ask you to solve three problems showing how suffix trees can be applied to other pattern matching
// challenges. The first such problem is the Longest Repeat Problem.
//
// Longest Repeat Problem
//
// Find the longest repeat in a string.
//
// Given: A string Text.
//
// Return: A longest substring of Text that appears in Text more than once. (Multiple solutions
// may exist, in which case you may return any one.)
//
// Sample Dataset
// --------------
// ATATCGTTTTATCGTT
// --------------
//
// Sample Output
// -------------
// TATCGTT
// -------------

import auxil.Suffix;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BA9D {

    private static String findLongestRepeatMachinery(String str) {
        int strLen = str.length();
        List<Suffix> suffixes = new ArrayList<>(strLen);
        for (int i = 0; i != strLen - 1; ++i) {
            suffixes.add(
                    new Suffix(str.substring(i), i)
            );
        }
        suffixes.sort(Comparator.comparing(Suffix::suffix));
        suffixes.addFirst(new Suffix("$", strLen - 1));
        List<Integer> lcp = BA9UTIL.constructLCP(
                str,
                suffixes.stream().map(Suffix::position).toList()
        );
        int max = Integer.MIN_VALUE, indexMax = 0;
        for (int i = 0; i != strLen; ++i) {
            if (lcp.get(i) > max) {
                max = lcp.get(i);
                indexMax = i;
            }
        }

        return suffixes
                .get(indexMax)
                .suffix()
                .substring(0, lcp.get(indexMax));
    }

    public static String findLongestRepeat(String str) {
        return findLongestRepeatMachinery(str);
    }

    public static String findLongestRepeat(Path path) {
        String str = UTIL.readDataset(path).getFirst();
        return findLongestRepeatMachinery(str);
    }

    private void run() {
        String longestRepeat = findLongestRepeat(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9d.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9d_out.txt")) {
            fileWriter.write("%s\n".formatted(
                    longestRepeat
            ));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9D().run();
    }
}
