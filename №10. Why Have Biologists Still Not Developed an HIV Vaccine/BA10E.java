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
import java.util.List;
import java.util.Map;

public class BA10E {

    private static void updateTransitionProbabilities(List<List<Double>> tps, auxil.Path path) {
        PathNode cur = path.getNthNode(0), prev;
        int prevIdx, curIdx;

        for (int i = 1; i != path.size(); ++i) {
            prev = cur;
            cur = path.getNthNode(i);
            prevIdx = BA10UTIL.pathNodeToIndex(prev);
            curIdx = BA10UTIL.pathNodeToIndex(cur);
            tps.get(prevIdx).set(curIdx, tps.get(prevIdx).get(curIdx) + 1);
        }
    }

    private static void reduceTransitionProbabilitiesWork(
            List<List<Double>> tps, int outerFrom, int outerTo, int innerFrom, int innerTo
    ) {
        double rowSum;

        for (int i = outerFrom; i != outerTo; ++i) {
            rowSum = 0;
            for (int j = innerFrom; j != innerTo; ++j) {
                rowSum += tps.get(i).get(j);
            }
            if (!(rowSum < BA10UTIL.EPS)) {
                for (int j = innerFrom; j != innerTo; ++j) {
                    tps.get(i).set(j, tps.get(i).get(j) / rowSum);
                }
            }
        }
    }

    private static void reduceTransitionProbabilities(List<List<Double>> tps) {
        int tpsSize = tps.size(), numSteps = tpsSize / 3 - 2, start, end;

        reduceTransitionProbabilitiesWork(tps, 0, 2, 1, 4);
        for (int i = 0; i != numSteps; ++i) {
            start = 3 * (i + 1);
            end = start + 3;
            reduceTransitionProbabilitiesWork(tps, start - 1, end - 1, start + 1, end + 1);
        }
        reduceTransitionProbabilitiesWork(tps, 3 * numSteps + 2, tpsSize - 1, tpsSize - 2, tpsSize);
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
                nodeIdx = BA10UTIL.pathNodeToIndex(curPathNode);
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
                nodeIdx = BA10UTIL.pathNodeToIndex(curPathNode);
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

        for (int i = 1; i != emps.size() - 1; ++i) {
            if (i % 3 == 0) {
                continue;
            }
            rowSum = 0;
            for (int j = 0; j != alSize; ++j) {
                rowSum += emps.get(i).get(j);
            }
            if (!(rowSum < BA10UTIL.EPS)) {
                for (int j = 0; j != alSize; ++j) {
                    emps.get(i).set(j, emps.get(i).get(j) / rowSum);
                }
            }
        }
    }

    private void writeMatrixToFile(
            List<List<Double>> mat, int dim1, int dim2, FileWriter fw
    ) throws IOException {
        for (int i = 0; i != 2; ++i) {
            fw.write("%s\t".formatted((i == 0) ? "S" : "I0"));
            for (int j = 0; j != dim2; ++j) {
                fw.write("%.3f%c".formatted(
                        mat.get(i).get(j),
                        (j == dim2 - 1) ? '\n' : '\t'
                ));
            }
        }
        for (int i = 3; i != dim1; ++i) {
            fw.write("%s%d\t".formatted(BA10UTIL.indexToState(i), i / 3));
            for (int j = 0; j != dim2; ++j) {
                fw.write("%.3f%c".formatted(
                        mat.get(i - 1).get(j),
                        (j == dim2 - 1) ? '\n' : '\t'
                ));
            }
        }
        fw.write("E\t");
        for (int i = 0; i != dim2; ++i) {
            fw.write("%.3f%c".formatted(
                    mat.get(dim1 - 1).get(i),
                    (i == dim2 - 1) ? '\n' : '\t'
            ));
        }
    }

    private void writeTransitionMatrixToFile(
            List<List<Double>> transitionMatrix, FileWriter fw
    ) throws IOException {
        int cols = transitionMatrix.size();
        fw.write("S\tI0\t");
        for (int i = 3; i != cols; ++i) {
            fw.write(BA10UTIL.indexToState(i) + "%d%s".formatted(i / 3, (i == cols - 1) ? "\tE\n" : "\t"));
        }
        writeMatrixToFile(transitionMatrix, cols, cols, fw);
    }

    private void writeEmissionMatrixToFile(
            List<List<Double>> emissionMatrix, Map<Character, Integer> alphabet, FileWriter fw
    ) throws IOException {
        int alSize = alphabet.size();
        List<Character> alphabetList = alphabet.keySet().stream().sorted().toList();
        fw.write("\t");
        for (int i = 0; i != alSize; ++i) {
            fw.write("%c%c".formatted(alphabetList.get(i), (i == alSize - 1) ? '\n' : '\t'));
        }
        writeMatrixToFile(emissionMatrix, emissionMatrix.size(), alSize, fw);
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMMMachinery(
            double threshold, Map<Character, Integer> alphabet, List<String> alignment
    ) {
        List<Integer> sc = BA10UTIL.getShadedColumns(alignment, threshold);
        int alignStrLen = alignment.getFirst().length(), seedStrLen = alignStrLen - sc.size();
        int numStates = 3 * seedStrLen + 3;
        List<List<Double>> transitionProbabilities = BA10UTIL.initDoubleMatrix(numStates);
        List<List<Double>> emissionProbabilities = BA10UTIL.initDoubleMatrix(numStates, alphabet.size());
        for (int i = 0; i != alignment.size(); ++i) {
            auxil.Path path = BA10UTIL.getPathFromString(alignment.get(i), sc);
            updateTransitionProbabilities(transitionProbabilities, path);
            updateEmissionProbabilities(emissionProbabilities, path, alignment.get(i), alphabet);
        }
        reduceTransitionProbabilities(transitionProbabilities);
        reduceEmissionProbabilities(emissionProbabilities);

        return Map.entry(transitionProbabilities, emissionProbabilities);
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

    @SuppressWarnings("CallToPrintStackTrace")
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
