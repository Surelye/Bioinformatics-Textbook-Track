// Find a Highest-Scoring Fitting Alignment of Two Strings
// -------------------------------------------------------
//
// Say that we wish to compare the approximately 20,000 amino acid-long NRP synthetase from
// Bacillus brevis with the approximately 600 amino acid-long A-domain from Streptomyces
// roseosporus, the bacterium that produces the powerful antibiotic Daptomycin. We hope to find a
// region within the longer protein sequence v that has high similarity with all of the shorter
// sequence w. Global alignment will not work because it tries to align all of v to all of w; local
// alignment will not work because it tries to align substrings of both v and w. Thus, we have a
// distinct alignment application called the Fitting Alignment Problem.
//
// “Fitting” w to v requires finding a substring v′ of v that maximizes the global alignment score
// between v′ and w among all substrings of v.
//
// Fitting Alignment Problem
//
// Construct a highest-scoring fitting alignment between two strings.
//
// Given: Two DNA strings v and w, where v has length at most 10000 and w has length at most 1000.
//
// Return: The maximum score of a fitting alignment of v and w, followed by a fitting alignment
// achieving this maximum score. Use the simple scoring method in which matches count +1 and both
// the mismatch and indel penalties are equal to 1. (If multiple fitting alignments achieving the
// maximum score exist, you may return any one.)
//
// Sample Dataset
// --------------
// GTAGGCTTAAGGTTA
// TAGATA
// --------------
//
// Sample Output
// -------------
// 2
// TAGGCTTA
// TAGA--TA
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BA5H {

    private static int maxFittingScore;

    private static int[][] LCSBacktrack(String v, String w) {
        int vLen = v.length(), wLen = w.length();
        char vChar, wChar;
        int[][] s = new int[wLen + 1][vLen + 1];

        for (int i = 1; i <= wLen; ++i) {
            wChar = w.charAt(i - 1);
            for (int j = 1; j <= vLen; ++j) {
                vChar = v.charAt(j - 1);
                s[i][j] = Math.max(
                        Math.max(s[i - 1][j] - 1, s[i][j - 1] - 1),
                        s[i - 1][j - 1] + ((vChar == wChar) ? 1 : -1)
                );
            }
        }
        maxFittingScore = Arrays.stream(s[wLen])
                .max()
                .orElse(Integer.MIN_VALUE);

        return s;
    }

    private static Map.Entry<Integer, Map.Entry<String, String>>
    findFittingAlignmentMachinery(String v, String w) {
        int vLen = v.length(), wLen = w.length();
        StringBuilder vAligned = new StringBuilder(),
                wAligned = new StringBuilder();
        char vChar, wChar;
        int[][] backtrack = LCSBacktrack(v, w);

        for (int k = vLen; k > -1; --k) {
            if (backtrack[wLen][k] == maxFittingScore) {
                int i = wLen, j = k;
                while (true) {
                    vChar = v.charAt(j - 1);
                    wChar = w.charAt(i - 1);
                    if (backtrack[i][j] == (backtrack[i - 1][j - 1] + (vChar == wChar ? 1 : -1))) {
                        vAligned.append(vChar);
                        wAligned.append(wChar);
                        --j; --i;
                    } else if (backtrack[i][j] == backtrack[i][j - 1] - 1) {
                        vAligned.append(vChar);
                        wAligned.append('-');
                        --j;
                    } else if (backtrack[i][j] == backtrack[i - 1][j] - 1) {
                        vAligned.append('-');
                        wAligned.append(wChar);
                        --i;
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

        return Map.entry(maxFittingScore, Map.entry(vAligned.toString(), wAligned.toString()));
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    findFittingAlignment(String v, String w) {
        return findFittingAlignmentMachinery(v, w);
    }

    public static Map.Entry<Integer, Map.Entry<String, String>> findFittingAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findFittingAlignmentMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        Map.Entry<Integer, Map.Entry<String, String>> scoreAndAlignment = findFittingAlignment(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5h.txt"
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
        new BA5H().run();
    }
}
