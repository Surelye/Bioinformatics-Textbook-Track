// Implement NumberToPattern
// -------------------------
//
// Implement NumberToPattern
//
// Convert an integer to its corresponding DNA string.
//
// Given: Integers index and k.
//
// Return: NumberToPattern(index, k).
//
// Sample Dataset
// --------------
// 45
// 4
// --------------
//
// Sample Output
// -------------
// AGTC
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA1M {

    private static String numberToSymbol(int index) {
        return
                switch (index) {
                    case 0 -> "A";
                    case 1 -> "C";
                    case 2 -> "G";
                    case 3 -> "T";
                    default -> throw new RuntimeException("Incorrect index provided");
                };
    }

    private static String numberToPatternMachinery(int index, int k) {
        if (k == 1) {
            return numberToSymbol(index);
        }

        int prefixIndex = index / 4,
                r = index % 4;
        String symbol = numberToSymbol(r);
        String prefixPattern = numberToPatternMachinery(prefixIndex, k - 1);

        return prefixPattern.concat(symbol);
    }

    public static String numberToPattern(int index, int k) {
        String pattern = numberToPatternMachinery(index, k);

        UTIL.writeToFile(List.of(pattern));

        return pattern;
    }

    public static String numberToPattern(Path path) {
        List<String> sampleDataset = UTIL.readDataset(path);
        int index = Integer.parseInt(sampleDataset.getFirst());
        int k = Integer.parseInt(sampleDataset.getLast());

        return numberToPattern(index, k);
    }
}
