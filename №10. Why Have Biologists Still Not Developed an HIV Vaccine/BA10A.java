// Compute the Probability of a Hidden Path
// ----------------------------------------
//
// Probability of a Hidden Path Problem
//
// Given: A hidden path π followed by the states `States` and transition matrix Transition of an
// HMM (Σ, States, Transition, Emission).
//
// Return: The probability of this path, Pr(π). You may assume that initial probabilities are equal.
//
// Sample Dataset
// --------------
// AABBBAABABAAAABBBBAABBABABBBAABBAAAABABAABBABABBAB
// --------
// A   B
// --------
//     A   B
// A   0.194   0.806
// B   0.273   0.727
// --------------
//
// Sample Output
// -------------
// 5.01732865318e-19
// -------------

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class BA10A {

    private static double computeHiddenPathProbabilityMachinery(
            String hiddenPath, List<Character> states,
            Map<Character, Map<Character, Double>> transition
    ) {
        int hpLen = hiddenPath.length();
        double prob = 1. / states.size();
        char curState = hiddenPath.charAt(0);

        for (int i = 1; i != hpLen; ++i) {
            prob *= transition.get(curState).get(hiddenPath.charAt(i));
            curState = hiddenPath.charAt(i);
        }

        return prob;
    }

    public static double computeHiddenPathProbability(
            String hiddenPath, List<Character> states,
            Map<Character, Map<Character, Double>> transition
    ) {
        return computeHiddenPathProbabilityMachinery(hiddenPath, states, transition);
    }

    public static double computeHiddenPathProbability(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String hiddenPath = strDataset.getFirst();
        List<Character> states = Arrays.stream(strDataset.get(2).split("\\s+"))
                .map(str -> str.charAt(0))
                .toList();
        int nStates = states.size();
        Map<Character, Map<Character, Double>> transition = new HashMap<>(nStates);

        for (int i = 5; i != strDataset.size(); ++i) {
            List<Double> row = BA10UTIL.parseDoubleArray(
                    strDataset.get(i).substring(1).strip(), "\\s+"
            );
            transition.put(states.get(i - 5), new HashMap<>(nStates));
            for (int s = 0; s != nStates; ++s) {
                transition.get(states.get(i - 5)).put(states.get(s), row.get(s));
            }
        }

        return computeHiddenPathProbabilityMachinery(hiddenPath, states, transition);
    }

    private void run() {
        double prob = computeHiddenPathProbability(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10a.txt"
                )
        );
        BA10UTIL.writeToFile("ba10a_out.txt", prob);
    }

    public static void main(String[] args) {
        new BA10A().run();
    }
}
