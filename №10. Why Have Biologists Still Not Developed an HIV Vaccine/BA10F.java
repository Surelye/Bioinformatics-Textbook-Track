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
import java.util.List;
import java.util.Map;

public class BA10F {

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
            List<List<Double>> tps, double pseudocount, int outerFrom, int outerTo,
            int innerFrom, int innerTo, boolean lastTwoColumnsMode
    ) {
        int numZeroes;
        double rowSum;

        for (int i = outerFrom; i != outerTo; ++i) {
            numZeroes = 0;
            rowSum = 0;
            for (int j = innerFrom; j != innerTo; ++j) {
                if (tps.get(i).get(j) < BA10UTIL.EPS) {
                    ++numZeroes;
                } else {
                    rowSum += tps.get(i).get(j);
                }
            }
            if (!(rowSum < BA10UTIL.EPS)) {
                for (int j = innerFrom; j != innerTo; ++j) {
                    if (tps.get(i).get(j) < BA10UTIL.EPS) {
                        tps.get(i).set(j, pseudocount);
                    } else {
                        tps.get(i).set(j, (1 - numZeroes * pseudocount) * tps.get(i).get(j) / rowSum);
                    }
                }
            } else {
                double prob = 1. / (lastTwoColumnsMode ? 2 : 3);
                for (int j = innerFrom; j != innerTo; ++j) {
                    tps.get(i).set(j, prob);
                }
            }
        }
    }

    private static void reduceTransitionProbabilities(List<List<Double>> tps, double pseudocount) {
        int tpsSize = tps.size(), numSteps = tpsSize / 3 - 2, start, end;

        reduceTransitionProbabilitiesWork(tps, pseudocount, 0, 2, 1, 4, false);
        for (int i = 0; i != numSteps; ++i) {
            start = 3 * (i + 1);
            end = start + 3;
            reduceTransitionProbabilitiesWork(
                    tps, pseudocount, start - 1, end - 1, start + 1, end + 1, false
            );
        }
        reduceTransitionProbabilitiesWork(
                tps, pseudocount, 3 * numSteps + 2, tpsSize - 1, tpsSize - 2, tpsSize, true
        );
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

    private static void reduceEmissionProbabilities(List<List<Double>> emps, double pseudocount) {
        assert !emps.isEmpty();
        int alSize = emps.getFirst().size(), numZeroes;
        double rowSum;

        for (int i = 1; i != emps.size() - 1; ++i) {
            if (i % 3 == 0) {
                continue;
            }
            numZeroes = 0;
            rowSum = 0;
            for (int j = 0; j != alSize; ++j) {
                if (emps.get(i).get(j) < BA10UTIL.EPS) {
                    ++numZeroes;
                } else {
                    rowSum += emps.get(i).get(j);
                }
            }
            if (!(rowSum < BA10UTIL.EPS)) {
                for (int j = 0; j != alSize; ++j) {
                    if (emps.get(i).get(j) < BA10UTIL.EPS) {
                        emps.get(i).set(j, pseudocount);
                    } else {
                        emps.get(i).set(j, (1 - numZeroes * pseudocount) * emps.get(i).get(j) / rowSum);
                    }
                }
            } else {
                double prob = 1. / 5;
                for (int j = 0; j != alSize; ++j) {
                    emps.get(i).set(j, prob);
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

    private static Map.Entry<List<List<Double>>, List<List<Double>>> constructProfileHMMWithPseudocountsMachinery(
            double threshold, double pseudocount, Map<Character, Integer> alphabet, List<String> alignment
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
        reduceTransitionProbabilities(transitionProbabilities, pseudocount);
        reduceEmissionProbabilities(emissionProbabilities, pseudocount);

        return Map.entry(transitionProbabilities, emissionProbabilities);
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
                Path.of("/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10f.txt")
        );
        String[] thresholdAndPseudocount = strDataset.getFirst().split("\\s+");
        Map<Character, Integer> alphabet = BA10UTIL.parseCharacterArrayToMap(strDataset.get(2), "\\s+");
        Map.Entry<List<List<Double>>, List<List<Double>>> matrices = constructProfileHMMWithPseudocounts(
                Double.parseDouble(thresholdAndPseudocount[0]),
                Double.parseDouble(thresholdAndPseudocount[1]),
                alphabet,
                strDataset.subList(4, strDataset.size())
        );
        try (FileWriter fileWriter = new FileWriter("ba10f_out.txt")) {
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
