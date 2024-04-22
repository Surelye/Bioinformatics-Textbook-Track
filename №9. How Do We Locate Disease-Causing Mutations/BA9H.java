// Pattern Matching with the Suffix Array
// --------------------------------------
//
// In “Construct the Suffix Array of a String”, we introduced the suffix array. In this problem, we
// will let you use the suffix array to solve the Multiple Pattern Matching Problem (introduced in
// “Construct a Trie from a Collection of Patterns”).
//
// Multiple Pattern Matching with the Suffix Array
//
// Given: A string Text and a collection of strings Patterns.
//
// Return: All starting positions in Text where a string from Patterns appears as a substring.
//
// Sample Dataset
// --------------
// AATCGGGTTCAATCGGGGT
// ATCG
// GGGT
// --------------
//
// Sample Output
// -------------
// 1 4 11 15
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class BA9H {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static Map.Entry<Integer, Integer> patternMatchingWithSuffixArrayEach(
            String text, String pattern, List<Integer> suffixArray
    ) {
        int patternLength = pattern.length(), pos, len;
        int minIndex = 0, maxIndex = text.length(), midIndex;
        int first, last;
        String suffix;

        while (minIndex < maxIndex) {
            midIndex = (minIndex + maxIndex) / 2;
            suffix = text.substring(suffixArray.get(midIndex));
            if (pattern.compareTo(suffix) > 0) {
                minIndex = midIndex + 1;
            } else {
                maxIndex = midIndex;
            }
        }
        first = minIndex;
        maxIndex = text.length();

        while (minIndex < maxIndex) {
            midIndex = (minIndex + maxIndex) / 2;
            pos = suffixArray.get(midIndex);
            len = text.substring(pos).length();
            suffix = text.substring(pos, pos + Math.min(patternLength, len));
            if (pattern.compareTo(suffix) < 0) {
                maxIndex = midIndex;
            } else {
                minIndex = midIndex + 1;
            }
        }
        last = maxIndex;

        if (first >= last) {
            return Map.entry(-1, -1);
        } else {
            return Map.entry(first, last);
        }
    }

    private static List<Integer> getPositions(
            List<Future<Map.Entry<Integer, Integer>>> futureIndices, List<Integer> suffixArray
    ) throws ExecutionException, InterruptedException {
        Map.Entry<Integer, Integer> indices;
        List<Integer> positions = new ArrayList<>();

        for (Future<Map.Entry<Integer, Integer>> fis : futureIndices) {
            indices = fis.get();
            for (int i = indices.getKey(); i != indices.getValue(); ++i) {
                positions.add(suffixArray.get(i));
            }
        }

        return positions;
    }

    private static List<Integer> patternMatchingWithSuffixArrayMachinery(
            String text, List<String> patterns
    ) {
        List<Integer> suffixArray = BA9G.constructSuffixArray(text);
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        List<Callable<Map.Entry<Integer, Integer>>> tasks = new ArrayList<>(patterns.size());
        for (String p : patterns) {
            tasks.add(() -> patternMatchingWithSuffixArrayEach(text, p, suffixArray));
        }
        List<Integer> positions = new ArrayList<>();

        try {
            List<Future<Map.Entry<Integer, Integer>>> futureIndices = executor.invokeAll(tasks);
            executor.shutdown();
            positions = getPositions(futureIndices, suffixArray);
            positions.sort(Integer::compare);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
            System.exit(1);
        } catch (ExecutionException e) {
            System.out.println("Failed to execute task");
            System.exit(2);
        }
        executor.close();

        return positions;
    }

    public static List<Integer> patternMatchingWithSuffixArray(String text, List<String> patterns) {
        return patternMatchingWithSuffixArrayMachinery(text, patterns);
    }

    public static List<Integer> patternMatchingWithSuffixArray(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return patternMatchingWithSuffixArrayMachinery(
                "%s$".formatted(strDataset.getFirst()),
                strDataset.subList(1, strDataset.size())
        );
    }

    private void run() {
        List<Integer> positions = patternMatchingWithSuffixArray(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9h.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9h_out.txt")) {
            int pSize = positions.size();
            for (int i = 0; i != pSize; ++i) {
                fileWriter.write("%d%s".formatted(
                        positions.get(i), (i == pSize - 1) ? "\n" : " "
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9H().run();
    }
}
