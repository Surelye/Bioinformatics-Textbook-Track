// Trim a Peptide Leaderboard
// --------------------------
//
// The Trim algorithm, shown below, sorts all peptides in Leaderboard according to their scores,
// resulting in a sorted Leaderboard. Trim then retains the top N scoring peptides including ties,
// and removes all other peptides from Leaderboard.
//
// --------------------------------------------------------
// Trim(Leaderboard, Spectrum, N, AminoAcid, AminoAcidMass)
//     for j ← 1 to |Leaderboard|
//         Peptide ← j-th peptide in Leaderboard
//         LinearScores(j) ← LinearScore(Peptide, Spectrum)
//     sort Leaderboard according to the decreasing order of scores in LinearScores
//     sort LinearScores in decreasing order
//     for j ← N + 1 to |Leaderboard|
//         if LinearScores(j) < LinearScores(N)
//             remove all peptides starting from the j-th peptide from Leaderboard
//         return Leaderboard
//     return Leaderboard
// --------------------------------------------------------
//
// Trim Problem
//
// Trim a leaderboard of peptides.
//
// Given: A leaderboard of linear peptides Leaderboard, a linear spectrum Spectrum, and an integer
// N.
//
// Return: The top N peptides from Leaderboard scored against Spectrum. Remember to use LinearScore.
//
// Sample Dataset
// --------------
// LAST ALST TLLT TQAS
// 0 71 87 101 113 158 184 188 259 271 372
// 2
// -------------
//
// Sample Output
// -------------
// LAST ALST
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BA4L {

    private static void
    quickSort(List<Integer> scores, List<String> peptides, int left, int right) {
        if (left >= right || left < 0) {
            return;
        }

        int pivot = scores.get(right), p = left;
        for (int i = left; i < right; ++i) {
            if (scores.get(i) > pivot) {
                BA4UTIL.swap(scores, i, p);
                BA4UTIL.swap(peptides, i, p);
                ++p;
            }
        }
        BA4UTIL.swap(scores, p, right);
        BA4UTIL.swap(peptides, p, right);

        quickSort(scores, peptides, left, p - 1);
        quickSort(scores, peptides, p + 1, right);
    }

    private static List<String>
    trimMachinery(List<String> leaderboard, List<Integer> spectrum, int N) {
        int leaderboardSize = leaderboard.size(), score;
        List<Integer> linearScores = new ArrayList<>();

        for (String peptide : leaderboard) {
            score = BA4K.computeLinearPeptideScore(peptide, spectrum);
            linearScores.add(score);
        }
        quickSort(linearScores, leaderboard, 0, leaderboardSize - 1);

        int boundLinearScores = linearScores.get(N - 1);
        for (int i = N; i < leaderboardSize; ++i) {
            if (linearScores.get(i) < boundLinearScores) {
                leaderboard = leaderboard.subList(0, N);
            }
        }
        UTIL.writeToFile(leaderboard);

        return leaderboard;
    }

    public static List<String> trim(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return trimMachinery(
                new ArrayList<>(Arrays.stream(strDataset.getFirst().split("\\s+")).toList()),
                UTIL.parseIntArray(strDataset.get(1)),
                Integer.parseInt(strDataset.getLast())
        );
    }

    public static List<String> trim(List<String> leaderboard, List<Integer> spectrum, int N) {
        return trimMachinery(leaderboard, spectrum, N);
    }
}
