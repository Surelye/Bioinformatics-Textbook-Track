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
import java.util.Arrays;
import java.util.List;

public class BA5K {

    private static final int indelPenalty = 5;
//    private static int[] fromSinkPrev;
    private static char[] backtrack;
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
        int vLen = v.length(), wLen = w.length();
        int vIndex, wIndex;
        int[] cur, next = new int[vLen + 1];
        backtrack = new char[vLen + 1];
        backtrack[0] = 'i';
        for (int i = 1; i <= vLen; ++i) {
            next[i] = next[i - 1] - indelPenalty;
        }

        for (int i = 0; i < wLen; ++i) {
            cur = Arrays.copyOf(next, next.length);
            wIndex = aminoAcidToIndex(w.charAt(i));
            next[0] = cur[0] - indelPenalty;
            for (int j = 1; j <= vLen; ++j) {
                vIndex = aminoAcidToIndex(v.charAt(j - 1));
                next[j] = Math.max(
                        Math.max(next[j - 1] - indelPenalty, cur[j] - indelPenalty),
                        cur[j - 1] + BLOSUM62[vIndex][wIndex]
                );
                if (i == wLen - 1) {
                    if (next[j] == cur[j - 1] + BLOSUM62[vIndex][wIndex]) {
                        backtrack[j] = 'm';
                    } else if (next[j] == next[j - 1] - indelPenalty) {
                        backtrack[j] = 'd';
                    } else if (next[j] == cur[j] - indelPenalty) {
                        backtrack[j] = 'i';
                    }
                }
            }
        }

        return next;
    }

    private static List<Integer> findMiddleEdgeMachinery(String v, String w) {
        int vLen = v.length(), wLen = w.length(), middle = wLen / 2;
        int iFrom = 0, jFrom = middle, iTo = 0, jTo = 0;
        int maxScore = Integer.MIN_VALUE, score;
        String vRev = new StringBuilder(v).reverse().toString();
        String wRev = new StringBuilder(w).reverse().toString();
        int[] fromSource = findFrom(v, w.substring(0, middle));
        int[] fromSink = findFrom(vRev, wRev.substring(0, wLen - middle));
        for (int i = 0; i <= vLen; ++i) {
            score = fromSource[i] + fromSink[vLen - i];
            if (score > maxScore) {
                maxScore = score;
                iFrom = i;
            }
        }

        int maxIndex = vLen - iFrom;
        if (backtrack[maxIndex] == 'm') {
            iTo = iFrom + 1;
            jTo = jFrom + 1;
        } else if (backtrack[maxIndex] == 'd') {
            iTo = iFrom + 1;
            jTo = jFrom;
        } else if (backtrack[maxIndex] == 'i') {
            iTo = iFrom;
            jTo = jFrom + 1;
        }

        return List.of(iFrom, jFrom, iTo, jTo);
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