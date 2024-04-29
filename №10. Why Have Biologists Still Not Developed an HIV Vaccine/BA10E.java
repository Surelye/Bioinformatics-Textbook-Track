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
import java.util.Arrays;
import java.util.List;

public class BA10E {

    private static int pathNodeToIndex(PathNode pn) {
        return switch (pn.nodeType()) {
            case s -> 0;
            case M -> 3 * pn.index() - 1;
            case D -> 3 * pn.index();
            case I -> 3 * pn.index() + 1;
            case e -> 3 * pn.index() + 2;
        };
    }

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

    private static auxil.Path getPathIfNoShadedColumns(String str) {
        auxil.Path path = new auxil.Path();
        PathNode pathNode;

        for (int i = 0; i != str.length(); ++i) {
            if (str.charAt(i) == '-') {
                pathNode = new PathNode(PathNode.NodeType.D, i + 1);
            } else {
                pathNode = new PathNode(PathNode.NodeType.M, i + 1);
            }
            path.addNode(pathNode);
        }
        path.addNode(new PathNode(PathNode.NodeType.e, str.length()));

        return path;
    }

    private static auxil.Path getPathFromString(String str, List<Integer> sc) {
        auxil.Path path = new auxil.Path();
        PathNode pathNode;

        if (sc.isEmpty()) {
            return getPathIfNoShadedColumns(str);
        }

        int scPtr = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (i == sc.get(scPtr)) {
                ++scPtr;
                if (str.charAt(i) != '-') {
                    pathNode = new PathNode(PathNode.NodeType.I, i - scPtr + 1);
                    path.addNode(pathNode);
                }
                while (scPtr < sc.size()) {
                    if (sc.get(scPtr).equals(sc.get(scPtr - 1) + 1)) {
                        if (str.charAt(sc.get(scPtr)) != '-') {
                            pathNode = new PathNode(PathNode.NodeType.I, sc.get(scPtr) - scPtr);
                            path.addNode(pathNode);
                        }
                        ++scPtr;
                    } else {
                        i = i + scPtr - 1;
                        break;
                    }
                }
            } else {
                if (str.charAt(i) == '-') {
                    pathNode = new PathNode(PathNode.NodeType.D, i + 1 - scPtr);
                    path.addNode(pathNode);
                } else {
                    pathNode = new PathNode(PathNode.NodeType.M, i + 1 - scPtr);
                    path.addNode(pathNode);
                }
            }
        }
        path.addNode(new PathNode(PathNode.NodeType.e, str.length() - sc.size()));

        return path;
    }

    private static void updateTransitionProbabilities(double[][] tps, auxil.Path path) {
        PathNode cur = path.getNthNode(0), prev;

        for (int i = 1; i != path.size(); ++i) {
            prev = cur;
            cur = path.getNthNode(i);
            ++tps[pathNodeToIndex(prev)][pathNodeToIndex(cur)];
        }
    }

    private static void reduceTransitionProbabilities(double[][] tps) {
        int numSteps = tps.length / 3 - 2, start, end;
        double rowSum;

        for (int i = 0; i != 2; ++i) {
            rowSum = 0;
            for (int j = 1; j != 4; ++j) {
                rowSum += tps[i][j];
            }
            if (!(rowSum < 10e-9)) {
                for (int j = 1; j != 4; ++j) {
                    tps[i][j] /= rowSum;
                }
            }
        }

        for (int i = 0; i != numSteps; ++i) {
            start = 3 * (i + 1);
            end = start + 3;
            for (int j = start - 1; j != end - 1; ++j) {
                rowSum = 0;
                for (int k = start + 1; k != end + 1; ++k) {
                    rowSum += tps[j][k];
                }
                if (!(rowSum < 10e-9)) {
                    for (int k = start + 1; k != end + 1; ++k) {
                        tps[j][k] /= rowSum;
                    }
                }
            }
        }

        for (int i = 3 * numSteps + 2; i != tps.length - 1; ++i) {
            rowSum = 0;
            for (int j = tps.length - 2; j != tps.length; ++j) {
                rowSum += tps[i][j];
            }
            if (!(rowSum < 10e-9)) {
                for (int j = tps.length - 2; j != tps.length; ++j) {
                    tps[i][j] /= rowSum;
                }
            }
        }
    }

    public static void constructProfileHMMMachinery(
            double threshold, List<Character> alphabet, List<String> alignment
    ) {
        List<Integer> sc = getShadedColumns(alignment, threshold);
        int alignStrLen = alignment.getFirst().length(), seedStrLen = alignStrLen - sc.size();
        int numStates = 3 * seedStrLen + 3;
        double[][] transitionProbabilities = new double[numStates][numStates];
        for (String str : alignment) {
            auxil.Path path = getPathFromString(str, sc);
            updateTransitionProbabilities(transitionProbabilities, path);
        }
        reduceTransitionProbabilities(transitionProbabilities);
        for (double[] row : transitionProbabilities) {
            System.out.println(Arrays.toString(row));
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
