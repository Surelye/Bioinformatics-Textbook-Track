// Find All Occurrences of a Collection of Patterns in a String
// ------------------------------------------------------------
//
// We hope that you have noticed what is still a limitation of BETTERBWMATCHING from “Implement
// BetterBWMatching” — even though this algorithm counts the number of occurrences of Pattern in
// Text, it does not tell us where these occurrences are located in Text! To locate pattern matches
// identified by BETTERBWMATCHING, we can once again use the suffix array.
//
// However, recall that our original motivation for using the Burrows-Wheeler transform was to
// reduce the amount of memory used by the suffix array for pattern matching. If we add the suffix
// array to Burrows-Wheeler-based pattern matching, then we are right back where we started!
//
// The memory-saving device that we will employ is inelegant but useful. We will build a partial
// suffix array of Text, denoted SuffixArrayK(Text), which only contains values that are multiples
// of some positive integer K (see “Construct the Partial Suffix Array of a String”). In real
// applications, partial suffix arrays are often constructed for K = 100, thus reducing memory
// usage by a factor of 100 compared to a full suffix array.
//
// We should also discuss how to improve BETTERBWMATCHING (reproduced below) by resolving the
// trade-off between precomputing the values of Count_{symbol}(i, LastColumn) (requiring
// substantial memory) and computing these values as we go (requiring substantial runtime).
//
// The balance that we strike is similar to the one used for the partial suffix array. Rather than
// storing Count_{symbol}(i, LastColumn) for all positions i, we will only store the Count arrays
// when i is divisible by C, where C is a constant; these arrays are called checkpoint arrays. When
// C is large (C is typically equal to 100 in practice) and the alphabet is small (e.g., 4
// nucleotides), checkpoint arrays require only a fraction of the memory used by BWT(Text).
//
// What about runtime? Using checkpoint arrays, we can compute the top and bottom pointers in a
// constant number of steps (i.e., fewer than C). Since each Pattern requires at most |Pattern|
// pointer updates, the modified BETTERBWMATCHING algorithm now requires O(|Patterns|) runtime,
// which is the same as using a trie or suffix array.
//
// Furthermore, we now only have to store the following data in memory: BWT(Text), FirstOccurrence,
// the partial suffix array, and the checkpoint arrays. Storing this data requires memory
// approximately equal to 1.5 * |Text|. Thus, we have finally knocked down the memory required for
// solving the Multiple Pattern Matching Problem for millions of sequencing reads into a workable
// range, and we can at last solve a full version of the Multiple Pattern Matching Problem.
//
// Multiple Pattern Matching Problem
//
// Find all occurrences of a collection of patterns in a text.
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

import auxil.Symbol;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class BA9N {

    private static final int K = 100;
    private static final int C = 100;

    private static int remainingCount(
            List<Symbol> lc, int end, char symbol,
            Map<Character, List<Integer>> checkpointArrays
    ) {
        int div = end / C, matches = checkpointArrays.get(symbol).get(div);
        for (int i = C * div; i != end; ++i) {
            if (lc.get(i).symbol() == symbol) {
                ++matches;
            }
        }
        return matches;
    }

    private static Map.Entry<Integer, Integer> patternMatchingPositionsFinder(
            StringBuilder pattern, List<Symbol> lc, Map<Character, Integer> firstOccurrence,
            Map<Character, List<Integer>> checkpointArrays
    ) {
        int lcSize = lc.size(), top = 0, bottom = lcSize - 1, patternLastIndex;
        char symbol;

        while (top <= bottom) {
            if (pattern.isEmpty()) {
                return Map.entry(top, bottom);
            } else {
                patternLastIndex = pattern.length() - 1;
                symbol = pattern.charAt(patternLastIndex);
                pattern.deleteCharAt(patternLastIndex);
                top = firstOccurrence.get(symbol) + ((top % C == 0) ?
                        checkpointArrays.get(symbol).get(top / C) :
                        remainingCount(lc, top, symbol, checkpointArrays)
                );
                bottom = firstOccurrence.get(symbol) + (((bottom + 1) % C) == 0 ?
                        checkpointArrays.get(symbol).get((bottom + 1) / C) - 1 :
                        remainingCount(lc, bottom + 1, symbol, checkpointArrays) - 1
                );
            }
        }
        return Map.entry(-1, -1);
    }

    private static List<Integer> patternMatchingEach(
            StringBuilder pattern, List<Symbol> fc, List<Symbol> lc,
            Map<Character, Integer> firstOccurrence, Map<Symbol, Integer> psa,
            Map<Character, List<Integer>> checkpointArrays
    ) {
        List<Integer> positions = new ArrayList<>();
        Symbol fcSymbol, lcSymbol;
        int curRow, steps;
        Map.Entry<Integer, Integer> fcBounds = patternMatchingPositionsFinder(
                pattern, lc, firstOccurrence, checkpointArrays
        );
        if (fcBounds.getKey() == -1) {
            return positions;
        }
        for (int i = fcBounds.getKey(); i <= fcBounds.getValue(); ++i) {
            curRow = i;
            fcSymbol = fc.get(curRow);
            steps = 0;
            while (true) {
                if (psa.containsKey(fcSymbol)) {
                    positions.add(psa.get(fcSymbol) + steps);
                    break;
                }
                lcSymbol = lc.get(curRow);
                curRow = firstOccurrence.get(lcSymbol.symbol()) + lcSymbol.occurrence() - 1;
                fcSymbol = fc.get(curRow);
                ++steps;
            }
        }

        return positions;
    }

    private static List<Integer> patternMatchingMachinery(String text, List<String> patterns) {
        List<Integer> suffixArray = BA9G.constructSuffixArray(text);
        String fc = BA9UTIL.getFirstColumn(text), lc = BA9I.BWT(text, suffixArray);
        List<Symbol> sfc =  BA9UTIL.stringToSymbolList(fc), slc = BA9UTIL.stringToSymbolList(lc);
        Map<Character, Integer> firstOccurrence = BA9UTIL.getFirstOccurrence(fc, true);
        Map<Symbol, Integer> psa = BA9Q.constructPartialSuffixArray(
                text, suffixArray, firstOccurrence, K
        );
        Map<Character, List<Integer>> checkpointArrays = BA9UTIL.getCheckpointArrays(lc, C);
        List<Callable<List<Integer>>> tasks = new ArrayList<>(patterns.size());
        for (String pattern : patterns) {
            tasks.add(
                    () -> patternMatchingEach(
                            new StringBuilder(pattern), sfc, slc,
                            firstOccurrence, psa, checkpointArrays
                    )
            );
        }

        return BA9UTIL.getFutureResults(tasks)
                .stream()
                .flatMap(Collection::stream)
                .sorted()
                .toList();
    }

    public static List<Integer> patternMatching(String text, List<String> patterns) {
        assert !text.isEmpty();
        if (text.charAt(text.length() - 1) == '$') {
            return patternMatchingMachinery(text, patterns);
        } else {
            return patternMatchingMachinery(text + "$", patterns);
        }
    }

    public static List<Integer> patternMatching(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String text = strDataset.getFirst();
        if (text.charAt(text.length() - 1) == '$') {
            return patternMatchingMachinery(text, strDataset.subList(1, strDataset.size()));
        } else {
            return patternMatchingMachinery(
                    text + "$",
                    strDataset.subList(1, strDataset.size())
            );
        }
    }

    private void run() {
        List<Integer> positions = patternMatching(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9n.txt"
                )
        );
        BA9UTIL.writeToFile("ba9n_out.txt", positions, " ");
    }

    public static void main(String[] args) {
        new BA9N().run();
    }
}
