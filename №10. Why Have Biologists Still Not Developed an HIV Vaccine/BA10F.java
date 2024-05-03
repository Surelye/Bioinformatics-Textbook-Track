// Construct a Profile HMM with Pseudocounts
// -----------------------------------------
//
// Profile HMM with Pseudocounts Problem
//
// Given: A threshold θ and a pseudocount σ, followed by an alphabet Σ, followed by a multiple
// alignment Alignment whose strings are formed from Σ.
//
// Return: The transition and emission probabilities of the profile HMM HMM(Alignment, θ, σ).
//
// Sample Dataset
// --------------
// 0.358   0.01
// --------
// A   B   C   D   E
// --------
// ADA
// ADA
// AAA
// ADC
// -DA
// D-A
// --------
//
// Sample Output
// -------------
// S   I0  M1  D1  I1  M2  D2  I2  M3  D3  I3  E
// S   0   0.01    0.819   0.172   0   0   0   0   0   0   0   0
// I0  0   0.333   0.333   0.333   0   0   0   0   0   0   0   0
// M1  0   0   0   0   0.01    0.786   0.204   0   0   0   0   0
// D1  0   0   0   0   0.01    0.981   0.01    0   0   0   0   0
// I1  0   0   0   0   0.333   0.333   0.333   0   0   0   0   0
// M2  0   0   0   0   0   0   0   0.01    0.981   0.01    0   0
// D2  0   0   0   0   0   0   0   0.01    0.981   0.01    0   0
// I2  0   0   0   0   0   0   0   0.333   0.333   0.333   0   0
// M3  0   0   0   0   0   0   0   0   0   0   0.01    0.99
// D3  0   0   0   0   0   0   0   0   0   0   0.5 0.5
// I3  0   0   0   0   0   0   0   0   0   0   0.5 0.5
// E   0   0   0   0   0   0   0   0   0   0   0   0
// --------
//     A   B   C   D   E
// S   0   0   0   0   0
// I0  0.2 0.2 0.2 0.2 0.2
// M1  0.771   0.01    0.01    0.2 0.01
// D1  0   0   0   0   0
// I1  0.2 0.2 0.2 0.2 0.2
// M2  0.2 0.01    0.01    0.771   0.01
// D2  0   0   0   0   0
// I2  0.2 0.2 0.2 0.2 0.2
// M3  0.803   0.01    0.168   0.01    0.01
// D3  0   0   0   0   0
// I3  0.2 0.2 0.2 0.2 0.2
// E   0   0   0   0   0
// -------------

import auxil.PathNode;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BA10F {

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
            if ((double) numSpaces / alSize > threshold) {
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

        int scPtr = 0, offset;
        for (int i = 0; i != str.length(); ++i) {
            if (scPtr < sc.size()) {
                if (i == sc.get(scPtr)) {
                    offset = 1;
                    ++scPtr;
                    if (str.charAt(i) != '-') {
                        pathNode = new PathNode(PathNode.NodeType.I, i - scPtr + 1);
                        path.addNode(pathNode);
                    }
                    while (scPtr < sc.size() && sc.get(scPtr).equals(sc.get(scPtr - 1) + 1)) {
                        if (str.charAt(sc.get(scPtr)) != '-') {
                            pathNode = new PathNode(PathNode.NodeType.I, sc.get(scPtr) - scPtr);
                            path.addNode(pathNode);
                        }
                        ++offset;
                        ++scPtr;
                    }
                    i += (offset - 1);
                } else {
                    pathNode = new PathNode(
                            (str.charAt(i) == '-') ? PathNode.NodeType.D : PathNode.NodeType.M,
                            i - scPtr + 1
                    );
                    path.addNode(pathNode);
                }
            } else {
                pathNode = new PathNode(
                        (str.charAt(i) == '-') ? PathNode.NodeType.D : PathNode.NodeType.M,
                        i - scPtr + 1
                );
                path.addNode(pathNode);
            }
        }
        path.addNode(new PathNode(PathNode.NodeType.e, str.length() - sc.size()));

        return path;
    }

    private static void updateTransitionProbabilities(List<List<Double>> tps, auxil.Path path) {
        PathNode cur = path.getNthNode(0), prev;
        int prevIdx, curIdx;

        for (int i = 1; i != path.size(); ++i) {
            prev = cur;
            cur = path.getNthNode(i);
            prevIdx = pathNodeToIndex(prev);
            curIdx = pathNodeToIndex(cur);
            tps.get(prevIdx).set(curIdx, tps.get(prevIdx).get(curIdx) + 1);
        }
    }

    private static void reduceTransitionProbabilities(List<List<Double>> tps, double pseudocount) {
        int numSteps = tps.size() / 3 - 2, numZeroes, start, end;
        double rowSum;

        for (int i = 0; i != 2; ++i) {
            numZeroes = 0;
            rowSum = 0;
            for (int j = 1; j != 4; ++j) {
                if (tps.get(i).get(j) < BA10UTIL.EPS) {
                    ++numZeroes;
                } else {
                    rowSum += tps.get(i).get(j);
                }
            }
            if (!(rowSum < BA10UTIL.EPS)) {
                for (int j = 1; j != 4; ++j) {
                    tps.get(i).set(j, tps.get(i).get(j) / rowSum);
                }
            } else {
                double prob = 1. / 3;
                for (int j = 1; j != 4; ++j) {
                    tps.get(i).set(j, prob);
                }
            }
        }

        for (int i = 0; i != numSteps; ++i) {
            start = 3 * (i + 1);
            end = start + 3;
            for (int j = start - 1; j != end - 1; ++j) {
                rowSum = 0;
                for (int k = start + 1; k != end + 1; ++k) {
                    rowSum += tps.get(j).get(k);
                }
                if (!(rowSum < BA10UTIL.EPS)) {
                    for (int k = start + 1; k != end + 1; ++k) {
                        tps.get(j).set(k, tps.get(j).get(k) / rowSum);
                    }
                }
            }
        }

        for (int i = 3 * numSteps + 2; i != tps.size() - 1; ++i) {
            rowSum = 0;
            for (int j = tps.size() - 2; j != tps.size(); ++j) {
                rowSum += tps.get(i).get(j);
            }
            if (!(rowSum < BA10UTIL.EPS)) {
                for (int j = tps.size() - 2; j != tps.size(); ++j) {
                    tps.get(i).set(j, tps.get(i).get(j) / rowSum);
                }
            }
        }
    }

    private static void updateEmissionProbabilities(
            List<List<Double>> emissionProbabilities, auxil.Path path,
            String str, Map<Character, Integer> alphabet
    ) {
        int strPtr = 0, nodeIdx, symbIdx;
        char curSymbol;
        PathNode curPathNode;

        for (int i = 1; i != path.size() - 1; ++i) {
            curPathNode = path.getNthNode(i);
            curSymbol = str.charAt(strPtr);
            if (curPathNode.nodeType() == PathNode.NodeType.M) {
                while (curSymbol == '-') {
                    curSymbol = str.charAt(++strPtr);
                }
                nodeIdx = pathNodeToIndex(curPathNode);
                symbIdx = alphabet.get(curSymbol);
                emissionProbabilities.get(nodeIdx).set(
                        symbIdx,
                        emissionProbabilities.get(nodeIdx).get(symbIdx) + 1
                );
                ++strPtr;
            } else if (curPathNode.nodeType() == PathNode.NodeType.D) {
                ++strPtr;
            } else if (curPathNode.nodeType() == PathNode.NodeType.I) {
                if (curSymbol == '-') {
                    while (curSymbol == '-') {
                        curSymbol = str.charAt(++strPtr);
                    }
                }
                nodeIdx = pathNodeToIndex(curPathNode);
                symbIdx = alphabet.get(curSymbol);
                emissionProbabilities.get(nodeIdx).set(
                        symbIdx,
                        emissionProbabilities.get(nodeIdx).get(symbIdx) + 1
                );
                ++strPtr;
            }
        }
    }

    private static void reduceEmissionProbabilities(List<List<Double>> emps, double pseudocount) {
        assert !emps.isEmpty();
        int alSize = emps.getFirst().size();
        double rowSum;

        for (List<Double> row : emps) {
            rowSum = 0;
            for (int i = 0; i != alSize; ++i) {
                rowSum += row.get(i);
            }
            if (!(rowSum < BA10UTIL.EPS)) {
                for (int i = 0; i != alSize; ++i) {
                    row.set(i, row.get(i) / rowSum);
                }
            }
        }
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMMWithPseudocountsMachinery(
            double threshold, double pseudocount, Map<Character, Integer> alphabet, List<String> alignment
    ) {
        List<Integer> sc = getShadedColumns(alignment, threshold);
        int alignStrLen = alignment.getFirst().length(), seedStrLen = alignStrLen - sc.size();
        int numStates = 3 * seedStrLen + 3;
        List<List<Double>> transitionProbabilities = BA10UTIL.initDoubleMatrix(numStates);
        List<List<Double>> emissionProbabilities = BA10UTIL.initDoubleMatrix(numStates, alphabet.size());
        for (int i = 0; i != alignment.size(); ++i) {
            auxil.Path path = getPathFromString(alignment.get(i), sc);
            updateTransitionProbabilities(transitionProbabilities, path);
            updateEmissionProbabilities(emissionProbabilities, path, alignment.get(i), alphabet);
        }
        reduceTransitionProbabilities(transitionProbabilities, pseudocount);
        reduceEmissionProbabilities(emissionProbabilities, pseudocount);

        return Map.entry(transitionProbabilities, emissionProbabilities);
    }

    private String indexToState(int i) {
        return switch (i % 3) {
            case 0 -> "M";
            case 1 -> "D";
            case 2 -> "I";
            default -> throw new IllegalStateException("Unexpected value: " + i % 3);
        };
    }

    private void writeTransitionMatrixToFile(
            List<List<Double>> transitionMatrix, FileWriter fw
    ) throws IOException {
        int cols = transitionMatrix.size();
        fw.write("S\tI0\t");
        for (int i = 3; i != cols; ++i) {
            fw.write(
                    indexToState(i) +
                            "%d%s".formatted(i / 3, (i == cols - 1) ? "\tE\n" : "\t")
            );
        }
        for (int i = 0; i != 2; ++i) {
            fw.write("%s\t".formatted((i == 0) ? "S" : "I0"));
            for (int j = 0; j != cols; ++j) {
                fw.write("%.3f%c".formatted(
                        transitionMatrix.get(i).get(j),
                        (j == cols - 1) ? '\n' : '\t'
                ));
            }
        }
        for (int i = 3; i != cols; ++i) {
            fw.write("%s%d\t".formatted(indexToState(i), i / 3));
            for (int j = 0; j != cols; ++j) {
                fw.write("%.3f%c".formatted(
                        transitionMatrix.get(i - 1).get(j),
                        (j == cols - 1) ? '\n' : '\t'
                ));
            }
        }
        fw.write("E\t");
        for (int j = 0; j != cols; ++j) {
            fw.write("%.3f%c".formatted(
                    transitionMatrix.get(cols - 1).get(j),
                    (j == cols - 1) ? '\n' : '\t'
            ));
        }
    }

    private void writeEmissionMatrixToFile(
            List<List<Double>> emissionMatrix, Map<Character, Integer> alphabet, FileWriter fw
    ) throws IOException {
        int rows = emissionMatrix.size(), alSize = alphabet.size();
        List<Character> alphabetList = alphabet.keySet().stream().sorted().toList();
        fw.write("\t");
        for (int i = 0; i != alSize; ++i) {
            fw.write("%c%c".formatted(alphabetList.get(i), (i == alSize - 1) ? '\n' : '\t'));
        }
        for (int i = 0; i != 2; ++i) {
            fw.write("%s\t".formatted((i == 0) ? "S" : "I0"));
            for (int j = 0; j != alSize; ++j) {
                fw.write("%.3f%c".formatted(
                        emissionMatrix.get(i).get(j),
                        (j == alSize - 1) ? '\n' : '\t'
                ));
            }
        }
        for (int i = 3; i != rows; ++i) {
            fw.write("%s%d\t".formatted(indexToState(i), i / 3));
            for (int j = 0; j != alSize; ++j) {
                fw.write("%.3f%c".formatted(
                        emissionMatrix.get(i - 1).get(j),
                        (j == alSize - 1) ? '\n' : '\t'
                ));
            }
        }
        fw.write("E\t");
        for (int i = 0; i != alSize; ++i) {
            fw.write("%.3f%c".formatted(
                    emissionMatrix.get(rows - 1).get(i),
                    (i == alSize - 1) ? '\n' : '\t'
            ));
        }
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMMWithPseudocounts(
            double threshold, double pseudocount, Map<Character, Integer> alphabet, List<String> alignment
    ) {
        return constructProfileHMMWithPseudocountsMachinery(threshold, pseudocount, alphabet, alignment);
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMMWithPseudocounts(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String[] thresholdAndPseudocount = strDataset.getFirst().split("\\s+");
        Map<Character, Integer> alphabet = BA10UTIL.parseCharacterArrayToMap(strDataset.get(2), "\\s+");
        return constructProfileHMMWithPseudocountsMachinery(
                Double.parseDouble(thresholdAndPseudocount[0]),
                Double.parseDouble(thresholdAndPseudocount[1]),
                alphabet,
                strDataset.subList(4, strDataset.size())
        );
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void run() {
        List<String> strDataset = UTIL.readDataset(
                Path.of("/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10e.txt")
        );
        String[] thresholdAndPseudocount = strDataset.getFirst().split("\\s+");
        Map<Character, Integer> alphabet = BA10UTIL.parseCharacterArrayToMap(strDataset.get(2), "\\s+");
        Map.Entry<List<List<Double>>, List<List<Double>>> matrices = constructProfileHMMWithPseudocounts(
                Double.parseDouble(thresholdAndPseudocount[0]),
                Double.parseDouble(thresholdAndPseudocount[1]),
                alphabet,
                strDataset.subList(4, strDataset.size())
        );
        try (FileWriter fileWriter = new FileWriter("ba10e_out.txt")) {
            writeTransitionMatrixToFile(matrices.getKey(), fileWriter);
            fileWriter.write("%s\n".formatted(BA10UTIL.separator));
            writeEmissionMatrixToFile(matrices.getValue(), alphabet, fileWriter);
        } catch (IOException e) {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BA10F().run();
    }
}
