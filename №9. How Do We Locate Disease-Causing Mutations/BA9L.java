// Implement BWMatching
// --------------------
//
// We are now ready to describe BWMATCHING, an algorithm that counts the total number of matches
// of Pattern in Text, where the only information that we are given is FirstColumn and
// LastColumn = BWT(Text) in addition to the Last-to-First mapping (from “Generate the
// Last-to-First Mapping of a String”). The pointers top and bottom are updated by the green lines
// in the following pseudocode.
// ---------------------------------------------------------
// BWMATCHING(FirstColumn, LastColumn, Pattern, LastToFirst)
//    top ← 0
//    bottom ← |LastColumn| − 1
//    while top ≤ bottom
//       if Pattern is nonempty
//          symbol ← last letter in Pattern
//          remove last letter from Pattern
//          if positions from top to bottom in LastColumn contain an occurrence of symbol
//             topIndex ← first position of symbol among positions from top to bottom in LastColumn
//             bottomIndex ← last position of symbol among positions from top to bottom in LastColumn
//             top ← LastToFirst(topIndex)
//             bottom ← LastToFirst(bottomIndex)
//          else
//             return 0
//       else
//          return bottom − top + 1
// ---------------------------------------------------------
//
// Implement BWMatching
//
// Given: A string BWT(Text), followed by a collection of strings Patterns.
//
// Return: A list of integers, where the i-th integer corresponds to the number of substring
// matches of the i-th member of Patterns in Text.
//
// Sample Dataset
// --------------
// TCCTCTATGAGATCCTATTCTATGAAACCTTCA$GACCAAAATTCTCCGGC
// CCT CAC GAG CAG ATC
// --------------
//
// Sample Output
// -------------
// 2 1 1 0 1
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class BA9L {

    private static Map.Entry<Integer, Integer> getPositions(String lcSubstr, char symbol) {
        int topIndex = lcSubstr.indexOf(symbol);
        return (topIndex == -1) ?
                Map.entry(-1, -1) :
                Map.entry(topIndex, lcSubstr.lastIndexOf(symbol));
    }

    private static int BWMatchingEach(
            String lc, StringBuilder pattern, Map<Integer, Integer> lastToFirst
    ) {
        int top = 0, bottom = lc.length() - 1, offset, patternLastIndex;
        char symbol;
        Map.Entry<Integer, Integer> indices;

        while (top <= bottom) {
            if (!pattern.isEmpty()) {
                patternLastIndex = pattern.length() - 1;
                symbol = pattern.charAt(patternLastIndex);
                pattern.deleteCharAt(patternLastIndex);
                indices = getPositions(lc.substring(top, bottom + 1), symbol);
                if (indices.getKey() != -1) {
                    offset = top;
                    top = lastToFirst.get(offset + indices.getKey());
                    bottom = lastToFirst.get(offset + indices.getValue());
                } else {
                    return 0;
                }
            } else {
                return bottom - top + 1;
            }
        }
        throw new RuntimeException("Top became less than bottom");
    }

    private static List<Integer> BWMatchingMachinery(String transform, List<String> patterns) {
        int pSize = patterns.size();
        String sfc = new String(transform.chars().sorted().toArray(), 0, transform.length());
        Map<Integer, Integer> lastToFirst = BA9UTIL.lastToFirst(transform, sfc);
        ExecutorService executor = Executors.newFixedThreadPool(BA9UTIL.POOL_SIZE);
        List<Callable<Integer>> tasks = new ArrayList<>(pSize);
        for (String pattern : patterns) {
            tasks.add(
                    () -> BWMatchingEach(transform, new StringBuilder(pattern), lastToFirst)
            );
        }
        List<Integer> matches = new ArrayList<>(pSize);

        try {
            List<Future<Integer>> fMatches = executor.invokeAll(tasks);
            executor.shutdown();
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
            executor.close();
        }

        return matches;
    }

    public static List<Integer> BWMatching(String transform, List<String> patterns) {
        return BWMatchingMachinery(transform, patterns);
    }

    public static List<Integer> BWMatching(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return BWMatchingMachinery(
                strDataset.getFirst(),
                Arrays.stream(strDataset.get(1).split(" ")).toList()
        );
    }

    private void run() {
        List<Integer> matches = BWMatching(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9l.txt"
                )
        );
        BA9UTIL.writeToFile("ba9l_out.txt", matches, " ");
    }

    public static void main(String[] args) {
        new BA9L().run();
    }
}
