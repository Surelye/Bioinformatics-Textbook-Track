// Construct a String Spelled by a Gapped Genome Path
// --------------------------------------------------
//
// Gapped Genome Path String Problem
//
// Reconstruct a string from a sequence of (k,d)-mers corresponding to a path in a paired de Bruijn
// graph.
//
// Given: A sequence of (k, d)-mers (a1|b1), ... , (an|bn) such that Suffix(ai|bi) =
// Prefix(ai+1|bi+1) for all i from 1 to n-1.
//
// Return: A string Text where the i-th k-mer in Text is equal to Suffix(ai|bi) for all i from 1 to
// n, if such a string exists.
//
// Sample Dataset
// --------------
// 4 2
// GACC|GCGC
// ACCG|CGCC
// CCGA|GCCG
// CGAG|CCGG
// GAGC|CGGA
// --------------
//
// Sample Output
// -------------
// GACCGAGCGCCGGA
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA3L {

    private static String stringSpelledByPatterns(List<String> patterns, int k) {
        int decK = k - 1;
        int patternsSize = patterns.size();
        StringBuilder spelledString = new StringBuilder(patterns.getFirst());

        for (int i = 1; i < patternsSize; ++i) {
            spelledString.append(patterns.get(i).charAt(decK));
        }

        return spelledString.toString();
    }

    private static String
    stringSpelledByGappedPatternsMachinery(List<String> gappedPatterns, int k, int d) {
        String prefixString, suffixString, spelledString;
        List<String> firstPatterns = new ArrayList<>(), secondPatterns = new ArrayList<>();
        for (String gappedPattern : gappedPatterns) {
            String[] patterns = gappedPattern.split("\\|");
            firstPatterns.add(patterns[0]);
            secondPatterns.add(patterns[1]);
        }
        prefixString = stringSpelledByPatterns(firstPatterns, k);
        suffixString = stringSpelledByPatterns(secondPatterns, k);
        int prefixStringLength = prefixString.length();

        for (int i = k + d; i < prefixStringLength; ++i) {
            if (prefixString.charAt(i) != suffixString.charAt(i - k - d)) {
                return null;
            }
        }
        spelledString = prefixString.concat(suffixString
                .substring(prefixStringLength - k - d));
        UTIL.writeToFile(List.of(spelledString));

        return spelledString;
    }

    public static String stringSpelledByGappedPatterns(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> intParams = UTIL.parseIntArray(strDataset.getFirst());

        return stringSpelledByGappedPatternsMachinery(strDataset.stream().skip(1).toList(),
                intParams.getFirst(), intParams.getLast());
    }

    public static String stringSpelledByGappedPatterns(List<String> gappedPatterns, int k, int d) {
        return stringSpelledByGappedPatternsMachinery(gappedPatterns, k, d);
    }
}
