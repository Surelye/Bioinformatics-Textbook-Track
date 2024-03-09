// Find a Middle Edge in an Alignment Graph in Linear Space
// --------------------------------------------------------
//
// Middle Edge in Linear Space Problem
//
// Find a middle edge in the alignment graph in linear space.
//
// Given: Two amino acid strings.
//
// Return: A middle edge in the alignment graph of these strings, where the optimal path is defined
// by the BLOSUM62 scoring matrix and a linear indel penalty equal to 5. Return the middle edge in
// the form “(i, j) (k, l)”, where (i, j) connects to (k, l).
//
// Sample Dataset
// --------------
// PLEASANTLY
// MEASNLY
// --------------
//
// Sample Output
// -------------
// (4, 3) (5, 4)
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BA5K {

    private static final int indelPenalty = 5;
    private static int[] fromSinkPrev;
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

    private static int[] findFrom(String v, String w) {
        int vLen = v.length(), wLen = w.length(), middle = wLen / 2;
        int vIndex, wIndex;
        int[] cur, next = new int[vLen + 1];

        for (int i = 0; i <= middle; ++i) {
            cur = Arrays.copyOf(next, next.length);
            if (i == middle) {
                fromSinkPrev = cur;
            }
            wIndex = aminoAcidToIndex(w.charAt(i));
            for (int j = 1; j <= vLen; ++j) {
                vIndex = aminoAcidToIndex(v.charAt(j - 1));
                next[j] = Math.max(
                        Math.max(next[j - 1] - indelPenalty, cur[j] - indelPenalty),
                        cur[j - 1] + BLOSUM62[vIndex][wIndex]
                );
            }
        }

        return next;
    }

    private static List<Integer> findMiddleEdgeMachinery(String v, String w) {
        int iFrom = 0, jFrom = w.length() / 2, iTo = 0, jTo = 0,
                length, maxLength = Integer.MIN_VALUE;
        String vRev = new StringBuilder(v).reverse().toString(),
                wRev = new StringBuilder(w).reverse().toString();
        int[] fromSource = findFrom(v, w);
        int[] fromSink = findFrom(vRev, wRev);
        int[] lengths = new int[fromSource.length];
        for (int i = 1; i < lengths.length; ++i) {
            length = fromSource[i] + fromSink[lengths.length - i];
            if (length > maxLength) {
                maxLength = length;
                iFrom = i - 1;
            }
        }

        int vIndex = aminoAcidToIndex(vRev.charAt(iFrom)),
                wIndex = aminoAcidToIndex(wRev.charAt(jFrom));
        if (fromSink[iFrom + 1] == (fromSinkPrev[iFrom] + BLOSUM62[vIndex][wIndex])) {
            iTo = iFrom + 1;
            jTo = jFrom + 1;
        } else if (fromSink[iFrom + 1] == (fromSink[iFrom] - indelPenalty)) {
            iTo = iFrom + 1;
            jTo = jFrom;
        } else if (fromSink[iFrom + 1] == (fromSinkPrev[iFrom + 1] - indelPenalty)) {
            iTo = iFrom;
            jTo = jFrom + 1;
        }

        return new ArrayList<>(List.of(iFrom, jFrom, iTo, jTo));
    }

    public static List<Integer> findMiddleEdge(String v, String w) {
        return findMiddleEdgeMachinery(v, w);
    }

    public static List<Integer> findMiddleEdge(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findMiddleEdgeMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        List<Integer> nodeIndices = findMiddleEdge(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5k.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("out.txt")) {
            fileWriter.write("(%d, %d) (%d, %d)".formatted(
                    nodeIndices.getFirst(),
                    nodeIndices.get(1),
                    nodeIndices.get(2),
                    nodeIndices.getLast()
            ));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA5K().run();
    }
}
