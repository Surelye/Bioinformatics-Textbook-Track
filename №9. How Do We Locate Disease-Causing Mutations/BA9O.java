// Find All Approximate Occurrences of a Collection of Patterns in a String
// ------------------------------------------------------------------------
//
// Multiple Approximate Pattern Matching Problem
//
// Find all approximate occurrences of a collection of patterns in a text.
//
// Given: A string Text, a collection of strings Patterns, and an integer d.
//
// Return: All positions in Text where a string from Patterns appears as a substring with at most d
// mismatches.
//
// Sample Dataset
// --------------
// ACATGCTACTTT
// ATT GCC GCTA TATT
// 1
// --------------
//
// Sample Output
// -------------
// 2 4 4 6 7 8 9
// -------------

import auxil.Symbol;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class BA9O {

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

    private static void update(
            Map<Integer, Integer> map, List<Map.Entry<Integer, Integer>> toPut,
            List<Integer> toRemove
    ) {
        for (Map.Entry<Integer, Integer> entry : toPut) {
            map.put(entry.getKey(), entry.getValue());
        }
        for (int i : toRemove) {
            map.remove(i);
        }
        toPut.clear();
        toRemove.clear();
    }

    private static List<Integer> approximatePatternMatchingPositionsFinder(
            StringBuilder pattern, List<Symbol> lc, Map<Character, Integer> firstOccurrence,
            Map<Character, List<Integer>> checkpointArrays, int d
    ) {
        int patternLastIndex, upd;
        char patSymbol, curSymbol;
        List<Map.Entry<Integer, Integer>> toPut = new ArrayList<>();
        List<Integer> toRemove = new ArrayList<>();
        Map<Integer, Integer> mismatches = new HashMap<>();
        for (int i = 0; i != lc.size(); ++i) {
            mismatches.put(i, 0);
        }

        while (!mismatches.isEmpty()) {
            if (pattern.isEmpty()) {
                return mismatches.keySet().stream().toList();
            } else {
                patternLastIndex = pattern.length() - 1;
                patSymbol = pattern.charAt(patternLastIndex);
                pattern.deleteCharAt(patternLastIndex);
                for (int i : mismatches.keySet()) {
                    if (lc.get(i).symbol() != patSymbol) {
                        if (mismatches.get(i) < d) {
                            toPut.add(Map.entry(i, mismatches.get(i) + 1));
                        } else {
                            toRemove.add(i);
                        }
                    }
                }
                update(mismatches, toPut, toRemove);
                Map<Integer, Integer> updatedMismatches = new HashMap<>();
                for (int i : mismatches.keySet()) {
                    curSymbol = lc.get(i).symbol();
                    upd = firstOccurrence.get(curSymbol) + ((i % C == 0) ?
                            checkpointArrays.get(curSymbol).get(i / C) :
                            remainingCount(lc, i, curSymbol, checkpointArrays)
                    );
                    updatedMismatches.put(upd, mismatches.get(i));
                }
                mismatches = updatedMismatches;
            }
        }
        return List.of();
    }

    private static List<Integer> approximatePatternMatchingEach(
            StringBuilder pattern, List<Symbol> fc, List<Symbol> lc,
            Map<Character, Integer> firstOccurrence, Map<Symbol, Integer> psa,
            Map<Character, List<Integer>> checkpointArrays, int d
    ) {
        List<Integer> positions = new ArrayList<>();
        Symbol fcSymbol, lcSymbol;
        int lcLast = lc.size() - 1, curRow, steps;
        List<Integer> fcPositions = approximatePatternMatchingPositionsFinder(
                pattern, lc, firstOccurrence, checkpointArrays, d
        );
        if (fcPositions.isEmpty()) {
            return positions;
        }
        for (int pos : fcPositions) {
            curRow = pos;
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

        return positions.stream()
                .filter(pos -> (lcLast - pos) > 1)
                .toList();
    }

    private static List<Integer> approximatePatternMatchingMachinery(
            String text, List<String> patterns, int d
    ) {
        List<Integer> suffixArray = BA9G.constructSuffixArray(text);
        String fc = BA9UTIL.getFirstColumn(text), lc = BA9I.BWT(text, suffixArray);
        List<Symbol> sfc = BA9UTIL.stringToSymbolList(fc), slc = BA9UTIL.stringToSymbolList(lc);
        Map<Character, Integer> firstOccurrence = BA9UTIL.getFirstOccurrence(fc, true);
        Map<Symbol, Integer> psa = BA9Q.constructPartialSuffixArray(
                text, suffixArray, firstOccurrence, K
        );
        Map<Character, List<Integer>> checkpointArrays = BA9UTIL.getCheckpointArrays(lc, C);
        List<Callable<List<Integer>>> tasks = new ArrayList<>(patterns.size());
        for (String pattern : patterns) {
            tasks.add(
                    () -> approximatePatternMatchingEach(
                            new StringBuilder(pattern), sfc, slc,
                            firstOccurrence, psa, checkpointArrays, d
                    )
            );
        }

        return BA9UTIL.getFutureResults(tasks)
                .stream()
                .flatMap(Collection::stream)
                .sorted()
                .toList();
    }

    public static List<Integer> approximatePatternMatching(
            String text, List<String> patterns, int d
    ) {
        return approximatePatternMatchingMachinery(
                (text.charAt(text.length() - 1) == '$') ? text : text + "$",
                patterns,
                d
        );
    }

    public static List<Integer> approximatePatternMatching(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String text = strDataset.getFirst();
        return approximatePatternMatchingMachinery(
                (text.charAt(text.length() - 1) == '$') ? text : text + "$",
                Arrays.stream(strDataset.get(1).split("\\s+")).toList(),
                Integer.parseInt(strDataset.getLast())
        );
    }

    private void run() {
        List<Integer> positions = approximatePatternMatching(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9o.txt"
                )
        );
        BA9UTIL.writeToFile("ba9o_out.txt", positions, " ");
    }

    public static void main(String[] args) {
        new BA9O().run();
    }
}
