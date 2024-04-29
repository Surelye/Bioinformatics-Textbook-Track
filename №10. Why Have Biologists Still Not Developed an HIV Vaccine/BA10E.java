// Construct a Profile HMM
// -----------------------
//
// Profile HMM Problem
//
// Given: A threshold θ, followed by an alphabet Σ, followed by a multiple alignment `Alignment`
// whose strings are formed from Σ.
//
// Return: The transition and emission probabilities of the profile HMM HMM(Alignment, θ).
//
// Sample Dataset
// --------------
// 0.289
// --------
// A   B   C   D   E
// --------
// EBA
// EBD
// EB-
// EED
// EBD
// EBE
// E-D
// EBD
// --------------
//
// Sample Output
// -------------
// S   I0  M1  D1  I1  M2  D2  I2  M3  D3  I3  E
// S   0   0   1.0 0   0   0   0   0   0   0   0   0
// I0  0   0   0   0   0   0   0   0   0   0   0   0
// M1  0   0   0   0   0   0.875   0.125   0   0   0   0   0
// D1  0   0   0   0   0   0   0   0   0   0   0   0
// I1  0   0   0   0   0   0   0   0   0   0   0   0
// M2  0   0   0   0   0   0   0   0   0.857   0.143   0   0
// D2  0   0   0   0   0   0   0   0   1.0 0   0   0
// I2  0   0   0   0   0   0   0   0   0   0   0   0
// M3  0   0   0   0   0   0   0   0   0   0   0   1.0
// D3  0   0   0   0   0   0   0   0   0   0   0   1.0
// I3  0   0   0   0   0   0   0   0   0   0   0   0
// E   0   0   0   0   0   0   0   0   0   0   0   0
// --------
//     A   B   C   D   E
// S   0   0   0   0   0
// I0  0   0   0   0   0
// M1  0   0   0   0   1.0
// D1  0   0   0   0   0
// I1  0   0   0   0   0
// M2  0   0.857   0   0   0.143
// D2  0   0   0   0   0
// I2  0   0   0   0   0
// M3  0.143   0   0   0.714   0.143
// D3  0   0   0   0   0
// I3  0   0   0   0   0
// E   0   0   0   0   0
// -------------

import auxil.PathNode;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA10E {

    private static List<Integer> getShadedColumns(List<String> alignment, double threshold) {
        int alSize = alignment.size(), strLen = alignment.getFirst().length();
        List<Integer> shadedColumns = new ArrayList<>(strLen);

        for (int i = 0; i != strLen; ++i) {
            int numSpaces = 0;
            for (int j = 0; j != alSize; ++j) {
                if (alignment.get(j).charAt(i) == '-') {
                    ++numSpaces;
                }
            }
            if ((double)numSpaces / alSize > threshold) {
                shadedColumns.add(i);
            }
        }

        return shadedColumns;
    }

//    public static List<String> getSeedAlignment(
//            List<String> alignment, List<Integer> shadedColumns
//    ) {
//        int strLen = alignment.getFirst().length(), scSize = shadedColumns.size(), scPointer;
//        StringBuilder seedStr = new StringBuilder(strLen - scSize);
//        List<String> seedAlignment = new ArrayList<>(alignment.size());
//
//        for (String str : alignment) {
//            scPointer = 0;
//            seedStr.delete(0, seedStr.length());
//            for (int i = 0; i != strLen; ++i) {
//                if (i == shadedColumns.get(scPointer)) {
//                    ++scPointer;
//                    if (!(scPointer < scSize)) {
//                        seedStr.append(str.substring(i + 1));
//                        seedAlignment.add(seedStr.toString());
//                        break;
//                    }
//                } else {
//                    seedStr.append(str.charAt(i));
//                }
//            }
//        }
//
//        return seedAlignment;
//    }

//    private static void updateTransitionProbabilities(
//            double[][] transitionProbabilities, String str, List<Integer> shadedColumns
//    ) {
//        char prev, cur;
//        if (shadedColumns.isEmpty()) {
//            cur = str.charAt(0);
//            ++transitionProbabilities[0][(cur == '-') ? 3 : 2];
//            for (int i = 1; i != str.length() - 1; ++i) {
//                prev = cur;
//                cur = str.charAt(i);
//                if (prev == '-') {
//                    ++transitionProbabilities[3 * i][3 * (i + 1) - ((cur == '-') ? 0 : 1)];
//                } else {
//                    ++transitionProbabilities[3 * i - 1][3 * (i + 1) - ((cur == '-') ? 0 : 1)];
//                }
//            }
//            cur = str.charAt(str.length() - 1);
//            ++transitionProbabilities[3 * str.length() - ((cur == '-') ? 0 : 1)][3 * str.length() + 2];
//            return;
//        }
//
//        int scPointer = 0;
//    }

    private static auxil.Path getPathFromString(String str, List<Integer> sc) {
        auxil.Path path = new auxil.Path();
        PathNode pathNode;

        if (sc.isEmpty()) {
            for (int i = 0; i != str.length(); ++i) {
                if (str.charAt(i) == '-') {
                    pathNode = new PathNode(PathNode.NodeType.D, i + 1);
                    path.addNode(pathNode);
                } else {
                    pathNode = new PathNode(PathNode.NodeType.M, i + 1);
                    path.addNode(pathNode);
                }
            }
            path.addNode(new PathNode(PathNode.NodeType.e, 0));
            return path;
        }

        return new auxil.Path();
    }

    public static void constructProfileHMMMachinery(
            double threshold, List<Character> alphabet, List<String> alignment
    ) {
        List<Integer> sc = getShadedColumns(alignment, threshold);
        int alignStrLen = alignment.getFirst().length(), seedStrLen = alignStrLen - sc.size();
        int numStates = 3 * seedStrLen + 3;
        double[][] transitionProbabilities = new double[numStates][numStates];
        List<auxil.Path> paths = new ArrayList<>(alignment.size());
        for (String str : alignment) {
            auxil.Path path = getPathFromString(str, sc);
            paths.add(path);
        }
    }

    public static void constructProfileHMM(
            double threshold, List<Character> alphabet, List<String> alignment
    ) {
        constructProfileHMMMachinery(threshold, alphabet, alignment);
    }

    public static void constructProfileHMM(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        double threshold = Double.parseDouble(strDataset.getFirst());
        List<Character> alphabet = BA10UTIL.parseCharacterArray(strDataset.get(2), "\\s+");
        constructProfileHMMMachinery(
                threshold,
                alphabet,
                strDataset.subList(4, strDataset.size())
        );
    }

    private void run() {
        constructProfileHMM(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10e.txt"
                )
        );
    }

    public static void main(String[] args) {
        new BA10E().run();
    }
}
