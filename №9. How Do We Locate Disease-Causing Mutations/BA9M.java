// Implement BetterBWMatching
// --------------------------
//
// If you implemented BWMATCHING in “Implement BWMatching”, you probably found the algorithm to be
// slow. The reason for its sluggishness is that updating the pointers top and bottom is
// time-intensive, since it requires examining every symbol in LastColumn between top and bottom
// at each step. To improve BWMATCHING, we introduce a function Countsymbol(i, LastColumn), which
// returns the number of occurrences of symbol in the first i positions of LastColumn. For example,
// Count_{"n”}(10, "smnpbnnaaaaa$a”) = 3, and Count_{"a”}(4, "smnpbnnaaaaa$a”) = 0.
//
// The green lines from BWMATCHING can be compactly described without the First-to-Last mapping by
// the following two lines:
//    top ← position of symbol with rank Countsymbol(top, LastColumn) + 1 in FirstColumn
//    bottom ← position of symbol with rank Countsymbol(bottom + 1, LastColumn) in FirstColumn
//
// Define FirstOccurrence(symbol) as the first position of symbol in FirstColumn. If
// Text = "panamabananas$", then FirstColumn is "$aaaaaabmnnnps", and the array holding all values
// of FirstOccurrence is [0, 1, 7, 8, 9, 11, 12]. For DNA strings of any length, the array
// FirstOccurrence contains only five elements.
//
// The two lines of pseudocode from the previous step can now be rewritten as follows:
//    top ← FirstOccurrence(symbol) + Countsymbol(top, LastColumn)
//    bottom ← FirstOccurrence(symbol) + Countsymbol(bottom + 1, LastColumn) − 1
//
// In the process of simplifying the green lines of pseudocode from BWMATCHING, we have also
// eliminated the need for both FirstColumn and LastToFirst, resulting in a more efficient algorithm
// called BETTERBWMATCHING.
// -------------------------------------------------------------
// BETTERBWMATCHING(FirstOccurrence, LastColumn, Pattern, Count)
//    top ← 0
//    bottom ← |LastColumn| − 1
//    while top ≤ bottom
//       if Pattern is nonempty
//          symbol ← last letter in Pattern
//          remove last letter from Pattern
//          if positions from top to bottom in LastColumn contain an occurrence of symbol
//             top ← FirstOccurrence(symbol) + Countsymbol(top, LastColumn)
//             bottom ← FirstOccurrence(symbol) + Countsymbol(bottom + 1, LastColumn) − 1
//          else
//             return 0
//       else
//          return bottom − top + 1
// -------------------------------------------------------------
//
// Implement BetterBWMatching
//
// Given: A string BWT(Text), followed by a collection of strings Patterns.
//
// Return: A list of integers, where the i-th integer corresponds to the number of substring
// matches of the i-th member of Patterns in Text.
//
// Sample Dataset
// --------------
// GGCGCCGC$TAGTCACACACGCCGTA
// ACC CCG CAG
// --------------
//
// Sample Output
// -------------
// 1 2 1
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BA9M {

    private static int betterBWMatchingEach(
            Map<Character, Integer> firstOccurrence, String lc,
            StringBuilder pattern, Map<Character, List<Integer>> count
    ) {
        int top = 0, bottom = lc.length() - 1, patternLastIndex;
        char symbol;

        while (top <= bottom) {
            if (pattern.isEmpty()) {
                return bottom - top + 1;
            } else {
                patternLastIndex = pattern.length() - 1;
                symbol = pattern.charAt(patternLastIndex);
                pattern.deleteCharAt(patternLastIndex);
                top = firstOccurrence.get(symbol) + count.get(symbol).get(top);
                bottom = firstOccurrence.get(symbol) + count.get(symbol).get(bottom + 1) - 1;
            }
        }
        return 0;
    }

    private static List<Integer> betterBWMatchingMachinery(
            String lc, List<String> patterns
    ) {
        int pSize = patterns.size();
        Map<Character, Integer> firstOccurrence = BA9UTIL.getFirstOccurrence(lc);
        Map<Character, List<Integer>> count = BA9UTIL.getCountArrays(lc);
        List<Callable<Integer>> tasks = new ArrayList<>(pSize);
        for (String pattern : patterns) {
            tasks.add(() -> betterBWMatchingEach(
                    firstOccurrence, lc, new StringBuilder(pattern), count)
            );
        }
        List<Integer> matches = new ArrayList<>(pSize);

        try {
            List<Future<Integer>> fMatches = BA9UTIL.executor.invokeAll(tasks);
            BA9UTIL.executor.shutdown();
            for (Future<Integer> fm : fMatches) {
                int curMatches = fm.get();
                matches.add(curMatches);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("Failed to execute task");
            e.printStackTrace();
        } finally {
            BA9UTIL.executor.close();
        }

        return matches;
    }

    public static List<Integer> betterBWMatching(String lc, List<String> patterns) {
        return betterBWMatchingMachinery(lc, patterns);
    }

    public static List<Integer> betterBWMatching(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return betterBWMatchingMachinery(
                strDataset.getFirst(),
                Arrays.stream(strDataset.get(1).split("\\s+")).toList()
        );
    }

    private void run() {
        List<Integer> matches = betterBWMatching(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9m.txt"
                )
        );
        BA9UTIL.writeToFile("ba9m_out.txt", matches, " ");
    }

    public static void main(String[] args) {
        new BA9M().run();
    }
}
