// Generate the k-mer Composition of a String
// ------------------------------------------
//
// Given a string Text, its k-mer composition Composition_k(Text) is the collection of all k-mer
// substrings of Text (including repeated k-mers). For example,
//
// Composition3(TATGGGGTGC) = {ATG, GGG, GGG, GGT, GTG, TAT, TGC, TGG}
//
// Note that we have listed k-mers in lexicographic order (i.e., how they would appear in a
// dictionary) rather than in the order of their appearance in TATGGGGTGC. We have done this because
// the correct ordering of the reads is unknown when they are generated.
//
// --------------------------
// String Composition Problem
//
// Generate the k-mer composition of a string.
//
// Given: An integer k and a string Text.
//
// Return: Compositionk(Text) (the k-mers can be provided in any order).
//
// Sample Dataset
// --------------
// 5
// CAATCCAAC
// --------------
//
// Sample Output
// -------------
// AATCC
// ATCCA
// CAATC
// CCAAC
// TCCAA
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class LexicographicOrderStringComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        int str1Len = str1.length();
        int str2Len = str2.length();
        int lenMin = Math.min(str1Len, str2Len);

        for (int i = 0; i < lenMin; ++i) {
            char str1Char = str1.charAt(i);
            char str2Char = str2.charAt(i);

            if (str1Char != str2Char) {
                return Character.compare(str1Char, str2Char);
            }
        }

        return Integer.compare(str1Len, str2Len);
    }
}

public class BA3A {

    private static List<String> getStringCompositionMachinery(int k, String text) {
        int textLength = text.length();
        List<String> kMers = new ArrayList<>();

        for (int i = 0; i < textLength - k + 1; ++i) {
            kMers.add(text.substring(i, i + k));
        }
        kMers.sort(new LexicographicOrderStringComparator());
        UTIL.writeToFileWithNewlines(kMers);

        return kMers;
    }

    public static List<String> getStringComposition(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return getStringCompositionMachinery(Integer.parseInt(strDataset.getFirst()),
                strDataset.getLast());
    }

    public static List<String> getStringComposition(int k, String text) {
        return getStringCompositionMachinery(k, text);
    }
}
