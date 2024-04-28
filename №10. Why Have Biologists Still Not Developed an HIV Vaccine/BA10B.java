// Compute the Probability of an Outcome Given a Hidden Path
// ---------------------------------------------------------
//
// Probability of an Outcome Given a Hidden Path Problem
//
// Given: A string x, followed by the alphabet Σ from which x was constructed, followed by a
// hidden path π, followed by the states `States` and emission matrix Emission of an HMM
// (Σ, States, Transition, Emission).
//
// Return: The conditional probability Pr(x|π) that string x will be emitted by the HMM given the
// hidden path π.
//
// Sample Dataset
// --------------
// xxyzyxzzxzxyxyyzxxzzxxyyxxyxyzzxxyzyzxzxxyxyyzxxzx
// --------
// x   y   z
// --------
// BBBAAABABABBBBBBAAAAAABAAAABABABBBBBABAABABABABBBB
// --------
// A   B
// --------
//     x   y   z
// A   0.612   0.314   0.074
// B   0.346   0.317   0.336
// --------------
//
// Sample Output
// -------------
// 1.93157070893e-28
// -------------

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA10B {

    private static double computeHiddenPathOutcomeProbabilityMachinery(
            String outcome, String hiddenPath, Map<Character, Map<Character, Double>> emission
    ) {
        int outLen = outcome.length();
        double prob = 1;
        for (int i = 0; i != outLen; ++i) {
            prob *= emission.get(hiddenPath.charAt(i)).get(outcome.charAt(i));
        }
        return prob;
    }

    public static double computeHiddenPathOutcomeProbability(
            String outcome, String hiddenPath, Map<Character, Map<Character, Double>> emission
    ) {
        return computeHiddenPathOutcomeProbabilityMachinery(outcome, hiddenPath, emission);
    }

    public static double computeHiddenPathOutcomeProbability(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String outcome = strDataset.getFirst();
        List<Character> alphabet = BA10UTIL.parseCharacterArray(strDataset.get(2), "\\s+");
        String hiddenPath = strDataset.get(4);
        List<Character> states = BA10UTIL.parseCharacterArray(strDataset.get(6), "\\s+");
        Map<Character, Map<Character, Double>> emission = new HashMap<>(states.size());
        for (int i = 9; i != strDataset.size(); ++i) {
            char state = states.get(i - 9);
            emission.put(state, new HashMap<>(alphabet.size()));
            List<Double> emissionRow = BA10UTIL.parseDoubleArray(
                    strDataset.get(i).substring(1).strip(),
                    "\\s+"
            );
            for (int j = 0; j != alphabet.size(); ++j) {
                emission.get(state).put(alphabet.get(j), emissionRow.get(j));
            }
        }

        return computeHiddenPathOutcomeProbabilityMachinery(outcome, hiddenPath, emission);
    }

    private void run() {
        double prob = computeHiddenPathOutcomeProbability(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10b.txt"
                )
        );
        BA10UTIL.writeToFile("ba10b_out.txt", prob);
    }

    public static void main(String[] args) {
        new BA10B().run();
    }
}
