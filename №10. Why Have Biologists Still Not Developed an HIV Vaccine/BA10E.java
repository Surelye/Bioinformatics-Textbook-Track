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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static void reduceTransitionProbabilities(List<List<Double>> tps) {
        int numSteps = tps.size() / 3 - 2, start, end;
        double rowSum;

        for (int i = 0; i != 2; ++i) {
            rowSum = 0;
            for (int j = 1; j != 4; ++j) {
                rowSum += tps.get(i).get(j);
            }
            if (!(rowSum < 10e-9)) {
                for (int j = 1; j != 4; ++j) {
                    tps.get(i).set(j, tps.get(i).get(j) / rowSum);
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
                if (!(rowSum < 10e-9)) {
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
            if (!(rowSum < 10e-9)) {
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

    private static void reduceEmissionProbabilities(List<List<Double>> emps) {
        assert !emps.isEmpty();
        int alSize = emps.getFirst().size();
        double rowSum;

        for (List<Double> row : emps) {
            rowSum = 0;
            for (int i = 0; i != alSize; ++i) {
                rowSum += row.get(i);
            }
            if (!(rowSum < 10e-9)) {
                for (int i = 0; i != alSize; ++i) {
                    row.set(i, row.get(i) / rowSum);
                }
            }
        }
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMMMachinery(
            double threshold, Map<Character, Integer> alphabet, List<String> alignment
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
        reduceTransitionProbabilities(transitionProbabilities);
        reduceEmissionProbabilities(emissionProbabilities);

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

    public static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMM(
            double threshold, Map<Character, Integer> alphabet, List<String> alignment
    ) {
        return constructProfileHMMMachinery(threshold, alphabet, alignment);
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMM(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        double threshold = Double.parseDouble(strDataset.getFirst());
        Map<Character, Integer> alphabet = BA10UTIL.parseCharacterArrayToMap(strDataset.get(2), "\\s+");
        return constructProfileHMMMachinery(
                threshold,
                alphabet,
                strDataset.subList(4, strDataset.size())
        );
    }

    private void run() {
        List<String> strDataset = UTIL.readDataset(
                Path.of("/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10e.txt")
        );
        Map<Character, Integer> alphabet = BA10UTIL.parseCharacterArrayToMap(strDataset.get(2), "\\s+");
        Map.Entry<List<List<Double>>, List<List<Double>>> matrices = constructProfileHMM(
                Double.parseDouble(strDataset.getFirst()),
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
        new BA10E().run();
    }
}
