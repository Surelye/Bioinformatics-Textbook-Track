// Compute the Probability of a String Emitted by an HMM
// -----------------------------------------------------
//
// Outcome Likelihood Problem
//
// Given: A string x, followed by the alphabet Σ from which x was constructed, followed by the
// states `States`, transition matrix Transition, and emission matrix Emission of an HMM
// (Σ, States, Transition, Emission).
//
// Return: The probability Pr(x) that the HMM emits x.
//
// Sample Dataset
// --------------
// xzyyzzyzyy
// --------
// x   y   z
// --------
// A   B
// --------
//     A   B
// A   0.303   0.697
// B   0.831   0.169
// --------
//     x   y   z
// A   0.533   0.065   0.402
// B   0.342   0.334   0.324
// --------------
//
// Sample Output
// -------------
// 1.1005510319694847e-06
// -------------

import auxil.HMM;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class BA10D {

    private static double computeProbabilityOfAnOutcomeMachinery(String outcome, HMM hmm) {
        int numStates = hmm.numStates(), outcomeLast = outcome.length() - 1;
        char curSymbol = outcome.charAt(0), fromState, toState;
        double[][] forward = new double[numStates][outcomeLast + 1];
        for (int i = 0; i != numStates; ++i) {
            forward[i][0] = hmm.getEmissionProbability(hmm.getNthState(i), curSymbol) / numStates;
        }
        for (int i = 1; i != outcome.length(); ++i) {
            curSymbol = outcome.charAt(i);
            for (int j = 0; j != numStates; ++j) {
                fromState = hmm.getNthState(j);
                for (int k = 0; k != numStates; ++k) {
                    toState = hmm.getNthState(k);
                    forward[k][i] += forward[j][i - 1] *
                            hmm.getTransitionProbability(fromState, toState) *
                            hmm.getEmissionProbability(toState, curSymbol);
                }
            }
        }

        return IntStream.range(0, numStates)
                .mapToDouble(i -> forward[i][outcomeLast])
                .sum();
    }

    public static double computeProbabilityOfAnOutcome(String outcome, HMM hmm) {
        return computeProbabilityOfAnOutcomeMachinery(outcome, hmm);
    }

    public static double computeProbabilityOfAnOutcome(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String outcome = strDataset.getFirst();
        HMM hmm = BA10UTIL.parseHMM(strDataset.subList(1, strDataset.size()));
        return computeProbabilityOfAnOutcomeMachinery(outcome, hmm);
    }

    private void run() {
        double outcomeProbability = computeProbabilityOfAnOutcome(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10d.txt"
                )
        );
        BA10UTIL.writeToFile("ba10d_out.txt", outcomeProbability);
    }

    public static void main(String[] args) {
        new BA10D().run();
    }
}
