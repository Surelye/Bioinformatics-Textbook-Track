// Align Two Strings Using Linear Space
// ------------------------------------
//
// The pseudocode below for LinearSpaceAlignment describes how to recursively find a longest path
// in the alignment graph constructed for a substring vtop+1 ... vbottom of v and a substring
// wleft+1 ... wright of w. LinearSpaceAlignment calls the function
// MiddleNode(top, bottom, left, right), which returns the coordinate i of the middle node (i, j)
// defined by the sequences vtop+1 ... vbottom and wleft+1 ... wright. LinearSpaceAlignment also
// calls MiddleEdge(top, bottom, left, right), which returns → , ↓, or ↘ depending on whether the
// middle edge is horizontal, vertical, or diagonal. The linear-space alignment of strings v and w
// is constructed by calling LinearSpaceAlignment(0, n, 0, m). The case left = right describes the
// alignment of an empty string against the string vtop+1 ... vbottom, which is trivially computed
// as the score of a gap formed by bottom − top vertical edges.
//
// ----------------------------------------------
// LinearSpaceAlignment(top, bottom, left, right)
//     if left = right
//         return alignment formed by bottom − top vertical edges
//     if top = bottom
//         return alignment formed by right − left horizontal edges
//     middle ← ⌊(left + right)/2⌋
//     midNode ← MiddleNode(top, bottom, left, right)
//     midEdge ← MiddleEdge(top, bottom, left, right)
//     LinearSpaceAlignment(top, midNode, left, middle)
//     output midEdge
//     if midEdge = "→" or midEdge = "↘"
//         middle ← middle + 1
//     if midEdge = "↓" or midEdge ="↘"
//         midNode ← midNode + 1
//     LinearSpaceAlignment(midNode, bottom, middle, right)
// ----------------------------------------------
//
// Global Alignment in Linear Space Problem
//
// Find the highest-scoring alignment between two strings using a scoring matrix in linear space.
//
// Given: Two long amino acid strings (of length approximately 10,000).
//
// Return: The maximum alignment score of these strings, followed by an alignment achieving this
// maximum score. Use the BLOSUM62 scoring matrix and indel penalty σ = 5.
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

public class BA5L {

    private static String v, w;
    private static StringBuilder alignment;
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

    private static char convertEdge(List<Integer> nodeIndices) {
        int iFrom = nodeIndices.getFirst(), jFrom = nodeIndices.get(1),
                iTo = nodeIndices.get(2), jTo = nodeIndices.getLast();
        int iDiff = iTo - iFrom, jDiff = jTo - jFrom;

        if (iDiff + jDiff == 2) {
            return 'm';
        } else if (iDiff + jDiff == 1) {
            if (iDiff == 1) {
                return 'd';
            } else if (jDiff == 1) {
                return 'i';
            }
        }

        throw new RuntimeException("Incorrect adjacent node difference");
    }

    private static void linearSpaceAlignmentMachinery(int top, int bottom, int left, int right) {
        if (left == right) {
            alignment.append("d".repeat(bottom - top));
            return;
        }
        if (top == bottom) {
            alignment.append("i".repeat(right - left));
            return;
        }
        int middle = (left + right) / 2;
        String vSub = BA5L.v.substring(top, bottom);
        String wSub = BA5L.w.substring(left, right);
        List<Integer> midEdge = BA5K.findMiddleEdge(vSub, wSub);
        int midNode = midEdge.getFirst() + top;
        linearSpaceAlignmentMachinery(top, midNode, left, middle);
        char edge = convertEdge(midEdge);
        alignment.append(edge);

        if (edge == 'i' || edge == 'm') {
            ++middle;
        }
        if (edge == 'd' || edge == 'm') {
            ++midNode;
        }
        linearSpaceAlignmentMachinery(midNode, bottom, middle, right);
    }

    private static Map.Entry<Integer, Map.Entry<String, String>>
    linearSpaceAlignmentWrapper(String v, String w) {
        int vLen = v.length(), wLen = w.length(), i = 0, j = 0;
        int linearAlignmentScore = 0;
        StringBuilder vAligned = new StringBuilder(vLen),
                wAligned = new StringBuilder(wLen);
        BA5L.v = v;
        BA5L.w = w;
        BA5L.alignment = new StringBuilder(Math.max(vLen, wLen));
        linearSpaceAlignmentMachinery(0, vLen, 0, wLen);

        char edge, vChar, wChar;
        for (int k = 0; k < alignment.length(); ++k) {
            edge = alignment.charAt(k);
            if (edge == 'm') {
                vChar = v.charAt(i);
                wChar = w.charAt(j);
                linearAlignmentScore += BLOSUM62[aminoAcidToIndex(vChar)][aminoAcidToIndex(wChar)];
                vAligned.append(vChar);
                wAligned.append(wChar);
                ++i; ++j;
            } else if (edge == 'd') {
                vChar = v.charAt(i);
                linearAlignmentScore -= indelPenalty;
                vAligned.append(vChar);
                wAligned.append('-');
                ++i;
            } else if (edge == 'i') {
                wChar = w.charAt(j);
                linearAlignmentScore -= indelPenalty;
                vAligned.append('-');
                wAligned.append(wChar);
                ++j;
            }
        }

        return Map.entry(linearAlignmentScore, Map.entry(vAligned.toString(), wAligned.toString()));
    }

    public static Map.Entry<Integer, Map.Entry<String, String>>
    linearSpaceAlignment(String v, String w) {
        return linearSpaceAlignmentWrapper(v, w);
    }

    public static Map.Entry<Integer, Map.Entry<String, String>> linearSpaceAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return linearSpaceAlignmentWrapper(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        Map.Entry<Integer, Map.Entry<String, String>> scoreAndAlignment = linearSpaceAlignment(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5l.txt"
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
        new BA5L().run();
    }
}
