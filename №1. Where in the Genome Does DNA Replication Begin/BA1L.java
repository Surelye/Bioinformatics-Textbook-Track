// Implement PatternToNumber
// -------------------------
//
// Implement PatternToNumber
//
// Convert a DNA string to a number.
//
// ---------------------------------
//
// Given: A DNA string Pattern.
//
// Return: PatternToNumber(Pattern).
//
// Sample Dataset
// --------------
// AGT
// --------------
//
// Sample Output
// -------------
// 11
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA1L {

    private static int symbolToNumber(char symbol) {
        return
                switch (symbol) {
                    case 'A' -> 0;
                    case 'C' -> 1;
                    case 'G' -> 2;
                    case 'T' -> 3;
                    default -> throw new RuntimeException("Incorrect nucleotide: %c".
                            formatted(symbol));
                };
    }

    private static long patternToNumber(String pattern, int n) {
        if (n == 0) {
            return 0;
        }

        char symbol = pattern.charAt(n - 1);
        String prefix = pattern.substring(0, n - 1);

        return 4 * patternToNumber(prefix) + symbolToNumber(symbol);
    }

    public static long patternToNumber(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        String pattern = sampleDataset.getFirst();

        return patternToNumber(pattern, pattern.length());
    }

    public static long patternToNumber(String pattern) {
        return patternToNumber(pattern, pattern.length());
    }
}
