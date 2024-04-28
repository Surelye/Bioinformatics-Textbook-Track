// Implement the Viterbi Algorithm
// -------------------------------
//
// Decoding Problem
//
// Given: A string x, followed by the alphabet Σ from which x was constructed, followed by the
// states `States`, transition matrix Transition, and emission matrix Emission of an HMM
// (Σ, States, Transition, Emission).
//
// Return: A path that maximizes the (unconditional) probability Pr(x, π) over all possible paths
// π.
//
// Sample Dataset
// --------------
// xyxzzxyxyy
// --------
// x   y   z
// --------
// A   B
// --------
//     A   B
// A   0.641   0.359
// B   0.729   0.271
// --------
//     x   y   z
// A   0.117   0.691   0.192
// B   0.097   0.42    0.483
// --------------
//
// Sample Output
// -------------
// AAABBAAAAA
// -------------

import auxil.HMM;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class BA10C {

    private static String backtrackPath(double[][] pathLengths, String outcome, HMM hmm) {
        int pathLen = outcome.length(), numStates = hmm.numStates();
        int mostProbableState = 0;
        char curSymbol, toState;
        double maxPathLength = -Double.MAX_VALUE, logProb, prevPathLen;
        StringBuilder path = new StringBuilder(pathLen);
        for (int i = 0; i != numStates; ++i) {
            if (pathLengths[i][pathLen - 1] > maxPathLength) {
                maxPathLength = pathLengths[i][pathLen - 1];
                mostProbableState = i;
            }
        }
        path.append(hmm.getNthState(mostProbableState));

        for (int i = pathLen - 1; i >= 1; --i) {
            curSymbol = outcome.charAt(i);
            toState = hmm.getNthState(mostProbableState);
            for (int j = 0; j != numStates; ++j) {
                logProb = Math.log(
                        hmm.getTransitionProbability(hmm.getNthState(j), toState) *
                        hmm.getEmissionProbability(toState, curSymbol)
                );
                prevPathLen = pathLengths[j][i - 1] + logProb;
                if (Math.abs(maxPathLength - prevPathLen) < 10e-6) {
                    maxPathLength = pathLengths[j][i - 1];
                    mostProbableState = j;
                    break;
                }
            }
            path.append(hmm.getNthState(mostProbableState));
        }

        return path.reverse().toString();
    }

    private static String ViterbiAlgorithmMachinery(String outcome, HMM hmm) {
        int numStates = hmm.numStates();
        char curSymbol = outcome.charAt(0), fromState, toState;
        double logProb, updPathLen;
        double[][] pathLengths = new double[numStates][outcome.length()];
        for (double[] row : pathLengths) {
            Arrays.fill(row, -Double.MAX_VALUE);
        }
        for (int i = 0; i != numStates; ++i) {
            pathLengths[i][0] = Math.log(
                    hmm.getEmissionProbability(hmm.getNthState(i), curSymbol)
            );
        }
        for (int i = 1; i != outcome.length(); ++i) {
            curSymbol = outcome.charAt(i);
            for (int j = 0; j != numStates; ++j) {
                fromState = hmm.getNthState(j);
                for (int k = 0; k != numStates; ++k) {
                    toState = hmm.getNthState(k);
                    logProb = Math.log(
                            hmm.getTransitionProbability(fromState, toState) *
                            hmm.getEmissionProbability(toState, curSymbol)
                    );
                    updPathLen = pathLengths[j][i - 1] + logProb;
                    if (updPathLen > pathLengths[k][i]) {
                        pathLengths[k][i] = updPathLen;
                    }
                }
            }
        }

        return backtrackPath(pathLengths, outcome, hmm);
    }

    public static String ViterbiAlgorithm(String outcome, HMM hmm) {
        return ViterbiAlgorithmMachinery(outcome, hmm);
    }

    public static String ViterbiAlgorithm(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String outcome = strDataset.getFirst();
        HMM hmm = BA10UTIL.parseHMM(strDataset.subList(1, strDataset.size()));
        return ViterbiAlgorithmMachinery(outcome, hmm);
    }

    private void run() {
        String path = ViterbiAlgorithm(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10c.txt"
                )
        );
        BA10UTIL.writeToFile("ba10c_out.txt", path);
    }

    public static void main(String[] args) {
        new BA10C().run();
    }
}
