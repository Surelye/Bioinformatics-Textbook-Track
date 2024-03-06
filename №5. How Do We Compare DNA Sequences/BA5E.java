// Find a Highest-Scoring Alignment of Two Strings
// -----------------------------------------------
//
// Global Alignment Problem
//
// Find the highest-scoring alignment between two strings using a scoring matrix.
//
// Given: Two amino acid strings.
//
// Return: The maximum alignment score of these strings followed by an alignment achieving this
// maximum score. Use the BLOSUM62 scoring matrix and indel penalty σ = 5. (If multiple alignments
// achieving the maximum score exist, you may return any one.)
//
// Sample Dataset
// --------------
// PLEASANTLY
// MEANLY
// --------------
//
// Sample Output
// -------------
// 8
// PLEASANTLY
// -MEA--N-LY
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class BA5E {

    private static int maxAlignmentScore;
    private static final int indelPenalty = 5;
    private static final int[][] BLOSUM62 = new int[][]{
            new int[]{ 4,  0, -2, -1, -2,  0, -2, -1, -1, -1, -1, -2, -1, -1, -1,  1,  0,  0, -3, -2},
            new int[]{ 0,  9, -3, -4, -2, -3, -3, -1, -3, -1, -1, -3, -3, -3, -3, -1, -1, -1, -2, -2},
            new int[]{-2, -3,  6,  2, -3, -1, -1, -3, -1, -4, -3,  1, -1,  0, -2,  0, -1, -3, -4, -3},
            new int[]{-1, -4,  2,  5, -3, -2,  0, -3,  1, -3, -2,  0, -1,  2,  0,  0, -1, -2, -3, -2},
            new int[]{-2, -2, -3, -3,  6, -3, -1,  0, -3,  0,  0, -3, -4, -3, -3, -2, -2, -1,  1,  3},
            new int[]{ 0, -3, -1, -2, -3,  6, -2, -4, -2, -4, -3,  0, -2, -2, -2,  0, -2, -3, -2, -3},
            new int[]{-2, -3, -1,  0, -1, -2,  8, -3, -1, -3, -2,  1, -2,  0,  0, -1, -2, -3, -2,  2},
            new int[]{-1, -1, -3, -3,  0, -4, -3,  4, -3,  2,  1, -3, -3, -3, -3, -2, -1,  3, -3, -1},
            new int[]{-1, -3, -1,  1, -3, -2, -1, -3,  5, -2, -1,  0, -1,  1,  2,  0, -1, -2, -3, -2},
            new int[]{-1, -1, -4, -3,  0, -4, -3,  2, -2,  4,  2, -3, -3, -2, -2, -2, -1,  1, -2, -1},
            new int[]{-1, -1, -3, -2,  0, -3, -2,  1, -1,  2,  5, -2, -2,  0, -1, -1, -1,  1, -1, -1},
            new int[]{-2, -3,  1,  0, -3,  0,  1, -3,  0, -3, -2,  6, -2,  0,  0,  1,  0, -3, -4, -2},
            new int[]{-1, -3, -1, -1, -4, -2, -2, -3, -1, -3, -2, -2,  7, -1, -2, -1, -1, -2, -4, -3},
            new int[]{-1, -3,  0,  2, -3, -2,  0, -3,  1, -2,  0,  0, -1,  5,  1,  0, -1, -2, -2, -1},
            new int[]{-1, -3, -2,  0, -3, -2,  0, -3,  2, -2, -1,  0, -2,  1,  5, -1, -1, -3, -3, -2},
            new int[]{ 1, -1,  0,  0, -2,  0, -1, -2,  0, -2, -1,  1, -1,  0, -1,  4,  1, -2, -3, -2},
            new int[]{ 0, -1, -1, -1, -2, -2, -2, -1, -1, -1, -1,  0, -1, -1, -1,  1,  5,  0, -2, -2},
            new int[]{ 0, -1, -3, -2, -1, -3, -3,  3, -2,  1,  1, -3, -2, -2, -3, -2,  0,  4, -3, -1},
            new int[]{-3, -2, -4, -3,  1, -2, -2, -3, -3, -2, -1, -4, -4, -2, -3, -3, -2, -3, 11,  2},
            new int[]{-2, -2, -3, -2,  3, -3,  2, -1, -2, -1, -1, -2, -3, -1, -2, -2, -2, -1,  2,  7}
    };

    private static int aminoAcidToIndex(char aminoAcid) {
        return switch (aminoAcid) {
            case 'A' ->  0; case 'C' ->  1; case 'D' ->  2; case 'E' ->  3; case 'F' ->  4;
            case 'G' ->  5; case 'H' ->  6; case 'I' ->  7; case 'K' ->  8; case 'L' ->  9;
            case 'M' -> 10; case 'N' -> 11; case 'P' -> 12; case 'Q' -> 13; case 'R' -> 14;
            case 'S' -> 15; case 'T' -> 16; case 'V' -> 17; case 'W' -> 18; case 'Y' -> 19;
            default -> throw new RuntimeException("Wrong amino acid: %c".formatted(aminoAcid));
        };
    }

    private static char[][] LCSBacktrack(String v, String w) {
        int vLen = v.length(), wLen = w.length();
        int[][] s = new int[vLen + 1][wLen + 1];
        int vIndex, wIndex;
        char[][] backtrack = new char[vLen + 1][wLen + 1];

        for (int i = 1; i <= vLen; ++i) {
            s[i][0] = s[i - 1][0] - indelPenalty;
            backtrack[i][0] = 'd';
        }
        for (int j = 1; j <= wLen; ++j) {
            s[0][j] = s[0][j - 1] - indelPenalty;
            backtrack[0][j] = 'i';
        }
        for (int i = 1; i <= vLen; ++i) {
            vIndex = aminoAcidToIndex(v.charAt(i - 1));
            for (int j = 1; j <= wLen; ++j) {
                wIndex = aminoAcidToIndex(w.charAt(j - 1));
                s[i][j] = Math.max(
                        Math.max(
                                s[i - 1][j] - indelPenalty,
                                s[i][j - 1] - indelPenalty
                        ),
                        s[i - 1][j - 1] + BLOSUM62[vIndex][wIndex]
                );
                if (s[i][j] == s[i - 1][j] - indelPenalty) {
                    backtrack[i][j] = 'd';
                } else if (s[i][j] == s[i][j - 1] - indelPenalty) {
                    backtrack[i][j] = 'i';
                } else {
                    backtrack[i][j] = 'm';
                }
            }
        }
        maxAlignmentScore = s[vLen][wLen];

        return backtrack;
    }

    private static void
    outputLCS(char[][] backtrack, int i, int j, StringBuilder alignment) {
        if (i == 0 || j == 0) {
            while (!(i == 0 && j == 0)) {
                if (i > 0) {
                    alignment.append(backtrack[i--][0]);
                } else if (j > 0) {
                    alignment.append(backtrack[0][j--]);
                }
            }
            return;
        }
        if (backtrack[i][j] == 'd') {
            outputLCS(backtrack, i - 1, j, alignment);
        } else if (backtrack[i][j] == 'i') {
            outputLCS(backtrack, i, j - 1, alignment);
        } else {
            outputLCS(backtrack,i - 1, j - 1, alignment);
        }
        alignment.append(backtrack[i][j]);
    }

    private static Map.Entry<Integer, Map.Entry<String, String>>
    findHighestScoringAlignmentMachinery(String v, String w) {
        StringBuilder alignment = new StringBuilder(),
                vAligned = new StringBuilder(),
                wAligned = new StringBuilder();
        int i = 0, j = 0;
        char[][] backtrack = LCSBacktrack(v, w);
        outputLCS(backtrack, v.length(), w.length(), alignment);

        for (int k = 0; k < alignment.length(); ++k) {
            char edge = alignment.charAt(k);
            if (edge == 'd') {
                vAligned.append(v.charAt(i++));
                wAligned.append('-');
            } else if (edge == 'i') {
                vAligned.append('-');
                wAligned.append(w.charAt(j++));
            } else {
                vAligned.append(v.charAt(i++));
                wAligned.append(w.charAt(j++));
            }
        }

        return Map.entry(maxAlignmentScore, Map.entry(vAligned.toString(), wAligned.toString()));
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    findHighestScoringAlignment(String v, String w) {
        return findHighestScoringAlignmentMachinery(v, w);
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    findHighestScoringAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findHighestScoringAlignmentMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        Map.Entry<Integer, Map.Entry<String, String>> scoreAndAlignment =
                findHighestScoringAlignment(
                        Path.of(
                                "C:\\Users\\sgnot\\Downloads\\rosalind_ba5e.txt"
                        )
                );

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            fileWriter.write("%d%n%s%n%s%n"
                    .formatted(scoreAndAlignment.getKey(),
                            scoreAndAlignment.getValue().getKey(),
                            scoreAndAlignment.getValue().getValue()));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA5E().run();
    }
}
