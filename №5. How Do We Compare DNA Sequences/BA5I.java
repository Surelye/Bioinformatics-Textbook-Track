// Find a Highest-Scoring Overlap Alignment of Two Strings
// -------------------------------------------------------
//
// When we assembled genomes, we discussed how to use overlapping reads to assemble a genome, a
// problem that was complicated by errors in reads. We would like to find overlaps between
// error-prone reads as well.
//
// An overlap alignment of strings v = v1 ... vn and w = w1 ... wm is a global alignment of a
// suffix of v with a prefix of w. An optimal overlap alignment of strings v and w maximizes the
// global alignment score between an i-suffix of v and a j-prefix of w (i.e., between vi ... vn and
// w1 ... wj) among all i and j.
//
// Overlap Alignment Problem
//
// Construct a highest-scoring overlap alignment between two strings.
//
// Given: Two protein strings v and w, each of length at most 1000.
//
// Return: The score of an optimal overlap alignment of v and w, followed by an alignment of a
// suffix v’ of v and a prefix w’ of w achieving this maximum score. Use an alignment score in
// which matches count +1 and both the mismatch and indel penalties are 2. (If multiple overlap
// alignments achieving the maximum score exist, you may return any one.)
//
// Sample Dataset
// --------------
// PAWHEAE
// HEAGAWGHEE
// --------------
//
// Sample Output
// -------------
// 1
// HEAE
// HEAG
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BA5I {

    private static int maxOverlapScore;

    private static int[][] LCSBacktrack(String v, String w) {
        int vLen = v.length(), wLen = w.length();
        char vChar, wChar;
        int[][] s = new int[vLen + 1][wLen + 1];

        for (int i = 1; i <= vLen; ++i) {
            vChar = v.charAt(i - 1);
            for (int j = 1; j <= wLen; ++j) {
                wChar = w.charAt(j - 1);
                s[i][j] = Math.max(
                        Math.max(s[i - 1][j] - 2, s[i][j - 1] - 2),
                        s[i - 1][j - 1] + ((vChar == wChar) ? 1 : -2)
                );
            }
        }
        maxOverlapScore = Arrays.stream(s[vLen])
                .max()
                .orElse(Integer.MIN_VALUE);

        return s;
    }

    private static Map.Entry<Integer, Map.Entry<String, String>>
    findOverlapAlignmentMachinery(String v, String w) {
        int vLen = v.length();
        char vChar, wChar;
        StringBuilder vAligned = new StringBuilder(),
                wAligned = new StringBuilder();
        int[][] backtrack = LCSBacktrack(v, w);

        for (int k = 1; k <= vLen; ++k) {
            if (backtrack[vLen][k] == maxOverlapScore) {
                int i = vLen, j = k;
                while (true) {
                    vChar = v.charAt(i - 1);
                    wChar = w.charAt(j - 1);
                    if (backtrack[i][j] == (backtrack[i - 1][j - 1] + (vChar == wChar ? 1 : -2))) {
                        vAligned.append(vChar);
                        wAligned.append(wChar);
                        --i; --j;
                    } else if (backtrack[i][j] == backtrack[i - 1][j] - 2) {
                        vAligned.append(vChar);
                        wAligned.append('-');
                        --i;
                    } else if (backtrack[i][j] == backtrack[i][j - 1] - 2) {
                        vAligned.append('-');
                        wAligned.append(wChar);
                        --j;
                    }

                    if (i == 0 || j == 0) {
                        break;
                    }
                }
                break;
            }
        }
        vAligned.reverse();
        wAligned.reverse();

        return Map.entry(maxOverlapScore, Map.entry(vAligned.toString(), wAligned.toString()));
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    findOverlapAlignment(String v, String w) {
        return findOverlapAlignmentMachinery(v, w);
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    findOverlapAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findOverlapAlignmentMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        Map.Entry<Integer, Map.Entry<String, String>> scoreAndAlignment = findOverlapAlignment(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5i.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("out.txt")) {
            fileWriter.write("%d%n%s%n%s%n".formatted(
                    scoreAndAlignment.getKey(),
                    scoreAndAlignment.getValue().getKey(),
                    scoreAndAlignment.getValue().getValue()
            ));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA5I().run();
    }
}
