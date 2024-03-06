// Find a Highest-Scoring Local Alignment of Two Strings
// -----------------------------------------------------
//
// Local Alignment Problem
//
// Find the highest-scoring local alignment between two strings.
//
// Given: Two amino acid strings.
//
// Return: The maximum score of a local alignment of the strings, followed by a local alignment of
// these strings achieving the maximum score. Use the PAM250 scoring matrix and indel penalty σ = 5.
// (If multiple local alignments achieving the maximum score exist, you may return any one.)

// Sample Dataset
// --------------
// MEANLY
// PENALTY
// --------------
//
// Sample Output
// -------------
// 15
// EANL-Y
// ENALTY
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class BA5F {

    private static int maxLocalAlignmentScore = Integer.MIN_VALUE;
    private static Map.Entry<Integer, Integer> jump;
    private static final int indelPenalty = 5;
    private static final int[][] PAM250 = new int[][]{
            new int[]{ 2, -2,  0,  0, -3,  1, -1, -1, -1, -2, -1,  0,  1,  0, -2,  1,  1,  0, -6, -3},
            new int[]{-2, 12, -5, -5, -4, -3, -3, -2, -5, -6, -5, -4, -3, -5, -4,  0, -2, -2, -8,  0},
            new int[]{ 0, -5,  4,  3, -6,  1,  1, -2,  0, -4, -3,  2, -1,  2, -1,  0,  0, -2, -7, -4},
            new int[]{ 0, -5,  3,  4, -5,  0,  1, -2,  0, -3, -2,  1, -1,  2, -1,  0,  0, -2, -7, -4},
            new int[]{-3, -4, -6, -5,  9, -5, -2,  1, -5,  2,  0, -3, -5, -5, -4, -3, -3, -1,  0,  7},
            new int[]{ 1, -3,  1,  0, -5,  5, -2, -3, -2, -4, -3,  0,  0, -1, -3,  1,  0, -1, -7, -5},
            new int[]{-1, -3,  1,  1, -2, -2,  6, -2,  0, -2, -2,  2,  0,  3,  2, -1, -1, -2, -3,  0},
            new int[]{-1, -2, -2, -2,  1, -3, -2,  5, -2,  2,  2, -2, -2, -2, -2, -1,  0,  4, -5, -1},
            new int[]{-1, -5,  0,  0, -5, -2,  0, -2,  5, -3,  0,  1, -1,  1,  3,  0,  0, -2, -3, -4},
            new int[]{-2, -6, -4, -3,  2, -4, -2,  2, -3,  6,  4, -3, -3, -2, -3, -3, -2,  2, -2, -1},
            new int[]{-1, -5, -3, -2,  0, -3, -2,  2,  0,  4,  6, -2, -2, -1,  0, -2, -1,  2, -4, -2},
            new int[]{ 0, -4,  2,  1, -3,  0,  2, -2,  1, -3, -2,  2,  0,  1,  0,  1,  0, -2, -4, -2},
            new int[]{ 1, -3, -1, -1, -5,  0,  0, -2, -1, -3, -2,  0,  6,  0,  0,  1,  0, -1, -6, -5},
            new int[]{ 0, -5,  2,  2, -5, -1,  3, -2,  1, -2, -1,  1,  0,  4,  1, -1, -1, -2, -5, -4},
            new int[]{-2, -4, -1, -1, -4, -3,  2, -2,  3, -3,  0,  0,  0,  1,  6,  0, -1, -2,  2, -4},
            new int[]{ 1,  0,  0,  0, -3,  1, -1, -1,  0, -3, -2,  1,  1, -1,  0,  2,  1, -1, -2, -3},
            new int[]{ 1, -2,  0,  0, -3,  0, -1,  0,  0, -2, -1,  0,  0, -1, -1,  1,  3,  0, -5, -3},
            new int[]{ 0, -2, -2, -2, -1, -1, -2,  4, -2,  2,  2, -2, -1, -2, -2, -1,  0,  4, -6, -2},
            new int[]{-6, -8, -7, -7,  0, -7, -3, -5, -3, -2, -4, -4, -6, -5,  2, -2, -5, -6, 17,  0},
            new int[]{-3,  0, -4, -4,  7, -5,  0, -1, -4, -1, -2, -2, -5, -4, -4, -3, -3, -2,  0, 10}
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
                        Math.max(0, s[i - 1][j] - indelPenalty),
                        Math.max(
                                s[i][j - 1] - indelPenalty,
                                s[i - 1][j - 1] + PAM250[vIndex][wIndex]
                        )
                );
                if (s[i][j] == 0) {
                    backtrack[i][j] = 't';
                } else if (s[i][j] == s[i - 1][j] - indelPenalty) {
                    backtrack[i][j] = 'd';
                } else if (s[i][j] == s[i][j - 1] - indelPenalty) {
                    backtrack[i][j] = 'i';
                } else {
                    backtrack[i][j] = 'm';
                }
                if (s[i][j] > maxLocalAlignmentScore) {
                    maxLocalAlignmentScore = s[i][j];
                    jump = Map.entry(i, j);
                }
            }
        }

        return backtrack;
    }

    private static void outputLCS(char[][] backtrack, int i, int j, StringBuilder alignment) {
        if (backtrack[i][j] == 'd') {
            outputLCS(backtrack, i - 1, j, alignment);
        } else if (backtrack[i][j] == 'i') {
            outputLCS(backtrack, i, j - 1, alignment);
        } else if (backtrack[i][j] == 'm') {
            outputLCS(backtrack, i - 1, j - 1, alignment);
        } else {
            return;
        }
        alignment.append(backtrack[i][j]);
    }

    private static Map.Entry<Integer, Map.Entry<String, String>>
    findHighestScoringLocalAlignmentMachinery(String v, String w) {
        StringBuilder alignment = new StringBuilder(),
                vAligned = new StringBuilder(),
                wAligned = new StringBuilder();
        char[][] backtrack = LCSBacktrack(v, w);
        int i = jump.getKey() - 1, j = jump.getValue() - 1;
        outputLCS(backtrack, i + 1, j + 1, alignment);
        alignment.reverse();

        for (int k = 0; k < alignment.length(); ++k) {
            char edge = alignment.charAt(k);
            if (edge == 'd') {
                vAligned.append(v.charAt(i--));
                wAligned.append('-');
            } else if (edge == 'i') {
                vAligned.append('-');
                wAligned.append(w.charAt(j--));
            } else {
                vAligned.append(v.charAt(i--));
                wAligned.append(w.charAt(j--));
            }
        }
        vAligned.reverse();
        wAligned.reverse();

        return Map.entry(
                maxLocalAlignmentScore,
                Map.entry(vAligned.toString(), wAligned.toString())
        );
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    findHighestScoringLocalAlignment(String v, String w) {
        return findHighestScoringLocalAlignmentMachinery(v, w);
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    findHighestScoringLocalAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findHighestScoringLocalAlignmentMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        Map.Entry<Integer, Map.Entry<String, String>> maxLocalScoreAndAlignment =
                findHighestScoringLocalAlignment(
                        Path.of(
                                "C:\\Users\\sgnot\\Downloads\\rosalind_ba5f.txt"
                        )
                );

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            fileWriter.write("%d%n%s%n%s%n".formatted(
                    maxLocalScoreAndAlignment.getKey(),
                    maxLocalScoreAndAlignment.getValue().getKey(),
                    maxLocalScoreAndAlignment.getValue().getValue()
            ));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA5F().run();
    }
}
