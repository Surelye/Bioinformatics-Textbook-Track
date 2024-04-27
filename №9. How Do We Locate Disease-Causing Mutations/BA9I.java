// Construct the Burrows-Wheeler Transform of a String
// ---------------------------------------------------
//
// Our goal is to further improve on the memory requirements of the suffix array introduced in
// “Construct the Suffix Array of a String” for multiple pattern matching.
//
// Given a string Text, form all possible cyclic rotations of Text; a cyclic rotation is defined
// by chopping off a suffix from the end of Text and appending this suffix to the beginning of
// Text. Next — similarly to suffix arrays — order all the cyclic rotations of Text
// lexicographically to form a |Text| × |Text| matrix of symbols that we call the Burrows-Wheeler
// matrix and denote by M(Text).
//
// Note that the first column of M(Text) contains the symbols of Text ordered lexicographically.
// In turn, the second column of M(Text) contains the second symbols of all cyclic rotations of
// Text, and so it too represents a (different) rearrangement of symbols from Text. The same
// reasoning applies to show that any column of M(Text) is some rearrangement of the symbols of
// Text. We are interested in the last column of M(Text), called the Burrows-Wheeler transform of
// Text, or BWT(Text).
//
// Burrows-Wheeler Transform Construction Problem
//
// Construct the Burrows-Wheeler transform of a string.
//
// Given: A string Text.
//
// Return: BWT(Text).
//
// Sample Dataset
// --------------
// GCGTGCCTGGTCA$
// --------------
//
// Sample Output
// -------------
// ACTGGCT$TGCGGC
// -------------

import auxil.Symbol;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA9I {

    private static List<Symbol> SBWTMachinery(String text, List<Integer> suffixArray) {
        int textLen = text.length();
        char chr;
        String BWT = BWTMachinery(text, suffixArray);
        Map<Character, Integer> occs = new HashMap<>();
        List<Symbol> SBWT = new ArrayList<>(textLen);
        for (int i = 0; i != textLen; ++i) {
            chr = BWT.charAt(i);
            if (occs.containsKey(chr)) {
                SBWT.add(new Symbol(chr, occs.get(chr)));
                occs.put(chr, occs.get(chr) + 1);
            } else {
                SBWT.add(new Symbol(chr, 1));
                occs.put(chr, 2);
            }
        }
        return SBWT;
    }

    public static List<Symbol> SBWT(String text, List<Integer> suffixArray) {
        return SBWTMachinery(text, suffixArray);
    }

    public static List<Symbol> SBWT(String text) {
        List<Integer> suffixArray = BA9G.constructSuffixArray(text);
        return SBWTMachinery(text, suffixArray);
    }

    private static String BWTMachinery(String text, List<Integer> suffixArray) {
        int textLength = text.length();
        StringBuilder transformed = new StringBuilder(textLength);
        transformed.append(text.charAt(textLength - 2));

        for (int i = 1; i != suffixArray.size(); ++i) {
            if (suffixArray.get(i) - 1 < 0) {
                transformed.append("$");
            } else {
                transformed.append(text.charAt(suffixArray.get(i) - 1));
            }
        }

        return transformed.toString();
    }

    public static String BWT(String text, List<Integer> suffixArray) {
        return BWTMachinery(text, suffixArray);
    }

    public static String BWT(String text) {
        List<Integer> suffixArray = BA9G.constructSuffixArray(text);
        return BWTMachinery(text, suffixArray);
    }

    public static String BWT(Path path) {
        String text = UTIL.readDataset(path).getFirst();
        List<Integer> suffixArray = BA9G.constructSuffixArray(text);
        return BWTMachinery(text, suffixArray);
    }

    private void run() {
        String transformed = BWT(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9i.txt"
                )
        );
        BA9UTIL.writeToFile("ba9i_out.txt", transformed);
    }

    public static void main(String[] args) {
        new BA9I().run();
    }
}
