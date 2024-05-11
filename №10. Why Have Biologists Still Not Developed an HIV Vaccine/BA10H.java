// Estimate the Parameters of an HMM
// ---------------------------------
//
// HMM Parameter Estimation Problem
//
// Given: A sequence of emitted symbols x = x1 . . . xn in an alphabet ∑ and a path
// π = π1 . . . πn generated by a k-state HMM with unknown transition and emission probabilities.
//
// Return: A matrix of transition probabilities Transition and a matrix of emission probabilities
// Emission that maximize Pr(x,π) over all possible matrices of transition and emission
// probabilities.
//
// Sample Dataset
// --------------
// yzzzyxzxxx
// --------
// x   y   z
// --------
// BBABABABAB
// --------
// A   B   C
// --------------
//
// Sample Output
// -------------
// A   B   C
// A   0.0 1.0 0.0
// B   0.8 0.2 0.0
// C   0.333   0.333   0.333
// --------
//     x   y   z
// A   0.25    0.25    0.5
// B   0.5 0.167   0.333
// C   0.333   0.333   0.333
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BA10H {

    private static void reduce(List<List<Double>> mat, int dim1, int dim2) {
        double rowSum;

        for (int i = 0; i != dim1; ++i) {
            rowSum = 0;
            for (int j = 0; j != dim2; ++j) {
                rowSum += mat.get(i).get(j);
            }
            if (Math.abs(rowSum) < BA10UTIL.EPS) {
                double avg = 1. / dim2;
                for (int j = 0; j != dim2; ++j) {
                    mat.get(i).set(j, avg);
                }
            } else {
                for (int j = 0; j != dim2; ++j) {
                    mat.get(i).set(j, mat.get(i).get(j) / rowSum);
                }
            }
        }
    }

    private static List<List<Double>> computeTransitionProbabilities(
            String path, Map<Character, Integer> states
    ) {
        int nStates = states.size(), pathLen = path.length();
        int curIdx = states.get(path.charAt(0)), prevIdx;
        List<List<Double>> transProbs = new ArrayList<>(nStates);
        for (int i = 0; i != nStates; ++i) {
            transProbs.add(
                    new ArrayList<>(Collections.nCopies(nStates, 0.))
            );
        }

        for (int i = 1; i != pathLen; ++i) {
            prevIdx = curIdx;
            curIdx = states.get(path.charAt(i));
            transProbs.get(prevIdx).set(curIdx, transProbs.get(prevIdx).get(curIdx) + 1);
        }
        reduce(transProbs, nStates, nStates);

        return transProbs;
    }

    private static List<List<Double>> computeEmissionProbabilities(
            String emitted, Map<Character, Integer> alphabet, String path, Map<Character, Integer> states
    ) {
        int strLen = emitted.length(), nSymbols = alphabet.size(), nStates = states.size();
        int symbIdx, stateIdx;
        List<List<Double>> emissionProbs = new ArrayList<>(nStates);
        for (int i = 0; i != nStates; ++i) {
            emissionProbs.add(
                    new ArrayList<>(Collections.nCopies(nSymbols, 0.))
            );
        }

        for (int i = 0; i != strLen; ++i) {
            symbIdx = alphabet.get(emitted.charAt(i));
            stateIdx = states.get(path.charAt(i));
            emissionProbs.get(stateIdx).set(symbIdx, emissionProbs.get(stateIdx).get(symbIdx) + 1);
        }
        reduce(emissionProbs, nStates, nSymbols);

        return emissionProbs;
    }

    private static Map.Entry<List<List<Double>>, List<List<Double>>> estimateHmmParametersMachinery(
            String emitted, Map<Character, Integer> alphabet, String path, Map<Character, Integer> states
    ) {
        return Map.entry(
                computeTransitionProbabilities(path, states),
                computeEmissionProbabilities(emitted, alphabet, path, states)
        );
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> estimateHmmParameters(
            String emitted, Map<Character, Integer> alphabet, String path, Map<Character, Integer> states
    ) {
        return estimateHmmParametersMachinery(emitted, alphabet, path, states);
    }

    public static Map.Entry<List<List<Double>>, List<List<Double>>> estimateHmmParameters(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return estimateHmmParametersMachinery(
                strDataset.getFirst(),
                BA10UTIL.parseCharacterArrayToMap(strDataset.get(2), "\\s+"),
                strDataset.get(4),
                BA10UTIL.parseCharacterArrayToMap(strDataset.getLast(), "\\s+")
        );
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void run() {
        List<String> strDataset = UTIL.readDataset(
                Path.of("/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10h.txt")
        );
        String emitted = strDataset.getFirst();
        Map<Character, Integer> alphabet = BA10UTIL.parseCharacterArrayToMap(strDataset.get(2), "\\s+");
        String path = strDataset.get(4);
        Map<Character, Integer> states = BA10UTIL.parseCharacterArrayToMap(strDataset.getLast(), "\\s+");

        Map.Entry<List<List<Double>>, List<List<Double>>> parameters = estimateHmmParameters(
                emitted, alphabet, path, states
        );
        List<Character> rowLabels = states.keySet().stream().sorted().toList();
        List<Character> columnLabels = alphabet.keySet().stream().sorted().toList();

        try (FileWriter fw = new FileWriter("ba10h_out.txt")) {
            BA10UTIL.writeMatrixToFile(parameters.getKey(), fw, rowLabels, rowLabels);
            fw.write("%s%c".formatted(BA10UTIL.separator, '\n'));
            BA10UTIL.writeMatrixToFile(parameters.getValue(), fw, rowLabels, columnLabels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BA10H().run();
    }
}
