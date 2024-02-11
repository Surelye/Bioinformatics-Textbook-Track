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
import java.util.Comparator;
import java.util.List;

public class BA4L {

    private static List<String>
    trimMachinery(List<String> leaderboard, List<Integer> spectrum, int N) {
        int leaderboardSize = leaderboard.size(), score;
        List<Pair<String, Integer>> peptidesAndTheirScores = new ArrayList<>();
        for (String peptide : leaderboard) {
            score = BA4K.computeLinearPeptideScore(peptide, spectrum);
            peptidesAndTheirScores.add(new Pair<>(
                    peptide,
                    score
            ));
        }
        peptidesAndTheirScores.sort(Comparator.comparing(Pair::getSecond));
        peptidesAndTheirScores = peptidesAndTheirScores.reversed();

        int boundLinearScore = peptidesAndTheirScores
                .get(Math.min(leaderboardSize - 1, N - 1)).getSecond();
        for (int i = N; i < leaderboardSize; ++i) {
            if (peptidesAndTheirScores.get(i).getSecond() < boundLinearScore) {
                return peptidesAndTheirScores
                        .subList(0, i)
                        .stream()
                        .map(Pair::getFirst)
                        .toList();
            }
        }

        return peptidesAndTheirScores
                .stream()
                .map(Pair::getFirst)
                .toList();
    }

    private static List<List<Integer>>
    trimListPeptideMachinery(List<List<Integer>> leaderboard, List<Integer> spectrum, int N) {
        int leaderboardSize = leaderboard.size(), score;
        List<Pair<List<Integer>, Integer>> peptidesAndTheirScores = new ArrayList<>();
        for (List<Integer> peptide : leaderboard) {
            score = BA4F.computeCyclicPeptideScore(peptide, spectrum);
            peptidesAndTheirScores.add(new Pair<>(
                    peptide,
                    score
            ));
        }
        peptidesAndTheirScores.sort(Comparator.comparing(Pair::getSecond));
        peptidesAndTheirScores = peptidesAndTheirScores.reversed();

        int boundLinearScore = peptidesAndTheirScores
                .get(Math.min(leaderboardSize - 1, N - 1)).getSecond();
        for (int i = N; i < leaderboardSize; ++i) {
            if (peptidesAndTheirScores.get(i).getSecond() < boundLinearScore) {
                return peptidesAndTheirScores
                        .subList(0, i)
                        .stream()
                        .map(Pair::getFirst)
                        .toList();
            }
        }

        return peptidesAndTheirScores
                .stream()
                .map(Pair::getFirst)
                .toList();
    }

    public static List<List<Integer>>
    trimListPeptide(List<List<Integer>> leaderboard, List<Integer> spectrum, int N) {
        return trimListPeptideMachinery(leaderboard, spectrum, N);
    }

    public static List<String> trim(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return trimMachinery(
                new ArrayList<>(Arrays.stream(strDataset
                        .getFirst().split("\\s+")).toList()),
                UTIL.parseIntArray(strDataset.get(1)),
                Integer.parseInt(strDataset.getLast())
        );
    }

    public static List<String> trim(List<String> leaderboard, List<Integer> spectrum, int N) {
        return trimMachinery(leaderboard, spectrum, N);
    }
}
