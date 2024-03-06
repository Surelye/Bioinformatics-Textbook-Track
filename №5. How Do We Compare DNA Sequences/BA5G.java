// Compute the Edit Distance Between Two Strings
// ---------------------------------------------
//
// In 1966, Vladimir Levenshtein introduced the notion of the edit distance between two strings as
// the minimum number of edit operations needed to transform one string into another. Here, an edit
// operation is the insertion, deletion, or substitution of a single symbol. For example, TGCATAT
// can be transformed into ATCCGAT with five edit operations, implying that the edit distance
// between these strings is at most 5.
//
// Edit Distance Problem
//
// Find the edit distance between two strings.
//
// Given: Two amino acid strings.
//
// Return: The edit distance between these strings.
//
// Sample Dataset
// --------------
// PLEASANTLY
// MEANLY
// --------------
//
// Sample Output
// -------------
// 5
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA5G {

    private static int computeEditDistanceMachinery(String v, String w) {
        int vLen = v.length(), wLen = w.length(), subCost;
        int[][] d = new int[vLen + 1][wLen + 1];
        for (int i = 1; i <= vLen; ++i) {
            d[i][0] = i;
        }
        for (int j = 1; j <= wLen; ++j) {
            d[0][j] = j;
        }

        for (int i = 1; i <= vLen; ++i) {
            for (int j = 1; j <= wLen; ++j) {
                subCost = (v.charAt(i - 1) == w.charAt(j - 1)) ? 0 : 1;
                d[i][j] = Math.min(
                        Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1),
                        d[i - 1][j - 1] + subCost
                );
            }
        }

        return d[vLen][wLen];
    }

    public static int computeEditDistance(String v, String w) {
        return computeEditDistanceMachinery(v, w);
    }

    public static int computeEditDistance(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return computeEditDistanceMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        int editDistance = computeEditDistance(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5g.txt"
                )
        );
        UTIL.writeToFile(List.of(editDistance));
    }

    public static void main(String[] args) {
        new BA5G().run();
    }
}
