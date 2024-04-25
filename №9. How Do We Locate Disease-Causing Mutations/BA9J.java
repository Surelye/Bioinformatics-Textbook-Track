// Reconstruct a String from its Burrows-Wheeler Transform
// -------------------------------------------------------
//
// In “Construct the Burrows-Wheeler Transform of a String”, we introduced the Burrows-Wheeler
// transform of a string Text. In this problem, we give you the opportunity to reverse this
// transform.
//
// Inverse Burrows-Wheeler Transform Problem
//
// Reconstruct a string from its Burrows-Wheeler transform.
//
// Given: A string Transform (with a single "$" sign).
//
// Return: The string Text such that BWT(Text) = Transform.
//
// Sample Dataset
// --------------
// TTCCTAACG$A
// --------------
//
// Sample Output
// -------------
// TACATCACGT$
// -------------

import auxil.Symbol;

import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BA9J {

    private static String invertBWT(List<Symbol> fc, List<Symbol> lc) {
        int textLen = fc.size(), idx;
        Symbol cur = new Symbol('$', 0);
        StringBuilder text = new StringBuilder(textLen);

        for (int i = 1; i != textLen; ++i) {
            idx = lc.indexOf(cur);
            cur = fc.get(idx);
            text.append(cur.symbol());
        }
        text.append('$');

        return text.toString();
    }

    private static String IBWTMachinery(String transform) {
        int textLen = transform.length(), occ;
        char cur;
        String sfc = new String(transform.chars().sorted().toArray(), 0, textLen);
        Map<Character, Integer> si = new HashMap<>();
        List<Symbol> fc = new ArrayList<>(textLen), lc = new ArrayList<>(textLen);
        for (int i = 0; i != textLen; ) {
            cur = sfc.charAt(i);
            si.put(cur, 0);
            occ = 0;
            while (i < textLen && cur == sfc.charAt(i)) {
                fc.add(new Symbol(cur, occ++));
                ++i;
            }
        }
        for (int i = 0; i != textLen; ++i) {
            cur = transform.charAt(i);
            lc.add(new Symbol(cur, si.get(cur)));
            si.put(cur, si.get(cur) + 1);
        }

        return invertBWT(fc, lc);
    }

    public static String IBWT(String transform) {
        return IBWTMachinery(transform);
    }

    public static String IBWT(Path path) {
        return IBWTMachinery(UTIL.readDataset(path).getFirst());
    }

    private void run() {
        String text = IBWT(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9j.txt"
                )
        );
        BA9UTIL.writeToFile("ba9j_out.txt", text);
    }

    public static void main(String[] args) {
        new BA9J().run();
    }
}
