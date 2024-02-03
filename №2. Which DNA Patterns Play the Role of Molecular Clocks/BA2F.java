// Implement RandomizedMotifSearch
// -------------------------------
//
// We will now turn to randomized algorithms that flip coins and roll dice in order to search for
// motifs. Making random algorithmic decisions may sound like a disastrous idea; just imagine a
// chess game in which every move would be decided by rolling a die. However, an 18th Century
// French mathematician and naturalist, Comte de Buffon, first proved that randomized algorithms
// are useful by randomly dropping needles onto parallel strips of wood and using the results of
// this experiment to accurately approximate the constant π.
//
// Randomized algorithms may be nonintuitive because they lack the control of traditional
// algorithms. Some randomized algorithms are Las Vegas algorithms, which deliver solutions that
// are guaranteed to be exact, despite the fact that they rely on making random decisions. Yet
// most randomized algorithms are Monte Carlo algorithms. These algorithms are not guaranteed to
// return exact solutions, but they do quickly find approximate solutions. Because of their speed,
// they can be run many times, allowing us to choose the best approximation from thousands of runs.
//
// A randomized algorithm for motif finding is given below.
//
// --------------------------------
// RANDOMIZEDMOTIFSEARCH(Dna, k, t)
//     randomly select k-mers Motifs = (Motif1, …, Motift) in each string from Dna
//     BestMotifs ← Motifs
//     while forever
//         Profile ← Profile(Motifs)
//         Motifs ← Motifs(Profile, Dna)
//         if Score(Motifs) < Score(BestMotifs)
//             BestMotifs ← Motifs
//         else
//             return BestMotifs
// --------------------------------
//
// Implement RandomizedMotifSearch
//
// Given: Positive integers k and t, followed by a collection of strings Dna.
//
// Return: A collection BestMotifs resulting from running RandomizedMotifSearch(Dna, k, t) 1000
// times. Remember to use pseudocounts!
//
// Sample Dataset
// --------------
// 8 5
// CGCCCCTCTCGGGGGTGTTCAGTAAACGGCCA
// GGGCGAGGTATGTGTAAGTGCCAAGGTGCCAG
// TAGTACCGAGACCGAAAGAAGTATACAGGCGT
// TAGATCAAGTTTCAGGTGCACGTCGGTGAACC
// AATCCACCAGCTCCACGTGCAATGTTGGCCTA
// --------------
//
// Sample Output
// -------------
// TCTCGGGG
// CCAAGGTG
// TACAGGCG
// TTCAGGTG
// TCCACGTG
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA2F {

    private static List<String> bestMotifs;
    private static int bestMotifsScore = Integer.MAX_VALUE;

    private static void randomizedMotifSearchEach(List<String> DNA, int motifLength, int k) {
        List<String> motifs = BA2UTIL.selectRandomKMers(DNA, motifLength, k);
        List<String> bestMotifs = motifs;
        int bestMotifsScore = BA2UTIL.score(bestMotifs), currentScore;
        double[][] profile;

        while (true) {
            profile = BA2UTIL.formProfileWithPseudocounts(motifs);
            motifs = BA2UTIL.formMotifs(profile, DNA);
            currentScore = BA2UTIL.score(motifs);

            if (bestMotifsScore > currentScore) {
                bestMotifsScore = currentScore;
                bestMotifs = motifs;
            } else {
                if (BA2F.bestMotifsScore > bestMotifsScore) {
                    BA2F.bestMotifsScore = bestMotifsScore;
                    BA2F.bestMotifs = bestMotifs;
                }
                return;
            }
        }
    }

    private static List<String> randomizedMotifSearchMachinery(List<String> DNA, int k) {
        int motifLength = DNA.getFirst().length();

        for (int i = 0; i < 1000; ++i) {
            randomizedMotifSearchEach(DNA, motifLength, k);
        }
        UTIL.writeToFileWithNewlines(BA2F.bestMotifs);

        return BA2F.bestMotifs;
    }

    public static List<String> randomizedMotifSearch(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> intParams = UTIL.parseIntArray(strDataset.getFirst());

        return randomizedMotifSearchMachinery(strDataset.stream().skip(1).toList(),
                intParams.getFirst());
    }

    public static List<String> randomizedMotifSearch(List<String> DNA, int k) {
        return randomizedMotifSearchMachinery(DNA, k);
    }
}
