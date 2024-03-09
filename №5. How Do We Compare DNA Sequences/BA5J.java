// Align Two Strings Using Affine Gap Penalties
// --------------------------------------------
//
// A gap is a contiguous sequence of spaces in a row of an alignment. One way to score gaps more
// appropriately is to define an affine penalty for a gap of length k as σ + ε · (k − 1), where σ
// is the gap opening penalty, assessed to the first symbol in the gap, and ε is the gap extension
// penalty, assessed to each additional symbol in the gap. We typically select ε to be smaller than
// σ so that the affine penalty for a gap of length k is smaller than the penalty for k independent
// single-nucleotide indels (σ · k).
//
// Alignment with Affine Gap Penalties Problem
//
// Construct a highest-scoring global alignment of two strings (with affine gap penalties).
//
// Given: Two amino acid strings v and w (each of length at most 100).
//
// Return: The maximum alignment score between v and w, followed by an alignment of v and w
// achieving this maximum score. Use the BLOSUM62 scoring matrix, a gap opening penalty of 11,
// and a gap extension penalty of 1.
//
// Sample Dataset
// --------------
// PRTEINS
// PRTWPSEIN
// --------------
//
// Sample Output
// -------------
// 8
// PRT---EINS
// PRTWPSEIN-
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class BA5J {

    private static int maxAffineGapScore;
    private static final int gapOpeningPenalty = 11;
    private static final int gapExtensionPenalty = 1;
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

    private static void
    LCSBacktrack(String v, String w, int[][] lower, int[][] middle, int[][] upper) {
        int vLen = v.length(), wLen = w.length();
        int vIndex, wIndex;

        for (int i = 1; i <= vLen; ++i) {
            vIndex = aminoAcidToIndex(v.charAt(i - 1));
            for (int j = 1; j <= wLen; ++j) {
                wIndex = aminoAcidToIndex(w.charAt(j - 1));
                lower[i][j] = Math.max(
                        lower[i - 1][j] - gapExtensionPenalty,
                        middle[i - 1][j] - gapOpeningPenalty
                );
                upper[i][j] = Math.max(
                        upper[i][j - 1] - gapExtensionPenalty,
                        middle[i][j - 1] - gapOpeningPenalty
                );
                middle[i][j] = Math.max(
                        Math.max(lower[i][j], upper[i][j]),
                        middle[i - 1][j - 1] + BLOSUM62[vIndex][wIndex]
                );
            }
        }
        maxAffineGapScore = middle[vLen][wLen];
    }

    private static Map.Entry<Integer, Map.Entry<String, String>>
    affineGapAlignmentMachinery(String v, String w) {
        int i = v.length(), j = w.length(), vIndex, wIndex;
        char vChar, wChar;
        int[][] lower = new int[i + 1][j + 1],
                middle = new int[i + 1][j + 1],
                upper = new int[i + 1][j + 1];
        LCSBacktrack(v, w, lower, middle, upper);
        StringBuilder vAligned = new StringBuilder(i),
                wAligned = new StringBuilder(j);

        while (true) {
            vChar = v.charAt(i - 1);
            wChar = w.charAt(j - 1);
            vIndex = aminoAcidToIndex(vChar);
            wIndex = aminoAcidToIndex(wChar);
            if (middle[i][j] == (middle[i - 1][j - 1] + BLOSUM62[vIndex][wIndex])) {
                vAligned.append(vChar);
                wAligned.append(wChar);
                --i; --j;
            } else if (middle[i][j] == lower[i][j]) {
                do {
                    vChar = v.charAt(i - 1);
                    vAligned.append(vChar);
                    wAligned.append('-');
                    --i;
                } while (i > 0 && lower[i + 1][j] == (lower[i][j] - gapExtensionPenalty));
            } else if (middle[i][j] == upper[i][j]) {
                do {
                    wChar = w.charAt(j - 1);
                    vAligned.append('-');
                    wAligned.append(wChar);
                    --j;
                } while (j > 0 && upper[i][j + 1] == (upper[i][j] - gapExtensionPenalty));
            }

            if (i == 0 || j == 0) {
                while (i > 0) {
                    vChar = v.charAt(i - 1);
                    vAligned.append(vChar);
                    wAligned.append('-');
                    --i;
                }
                while (j > 0) {
                    wChar = w.charAt(j - 1);
                    wAligned.append('-');
                    wAligned.append(wChar);
                    --j;
                }
                break;
            }
        }
        vAligned.reverse();
        wAligned.reverse();

        return Map.entry(maxAffineGapScore, Map.entry(vAligned.toString(), wAligned.toString()));
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    affineGapAlignment(String v, String w) {
        return affineGapAlignmentMachinery(v, w);
    }

    public static Map.Entry<Integer, Map.Entry<String, String>> affineGapAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return affineGapAlignment(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        Map.Entry<Integer, Map.Entry<String, String>> scoreAndAlignment = affineGapAlignment(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5j.txt"
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
        new BA5J().run();
    }
}
