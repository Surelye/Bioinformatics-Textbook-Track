// Implement GibbsSampler
// ----------------------
//
// We have previously defined the notion of a Profile-most probable k-mer in a string. We now
// define a Profile-randomly generated k-mer in a string Text. For each k-mer Pattern in Text,
// compute the probability Pr(Pattern | Profile), resulting in n = |Text| - k + 1 probabilities
// (p1, …, pn). These probabilities do not necessarily sum to 1, but we can still form the random
// number generator Random(p1, …, pn) based on them. GIBBSSAMPLER uses this random number generator
// to select a Profile-randomly generated k-mer at each step: if the die rolls the number i, then
// we define the Profile-randomly generated k-mer as the i-th k-mer in Text.
//
// --------------------------
// GIBBSSAMPLER(Dna, k, t, N)
//     randomly select k-mers Motifs = (Motif1, …, Motift) in each string from Dna
//     BestMotifs ← Motifs
//     for j ← 1 to N
//         i ← Random(t)
//         Profile ← profile matrix constructed from all strings in Motifs except for Motifi
//         Motifi ← Profile-randomly generated k-mer in the i-th sequence
//         if Score(Motifs) < Score(BestMotifs)
//             BestMotifs ← Motifs
//     return BestMotifs
// --------------------------
//
// Implement GibbsSampler
//
// Given: Integers k, t, and N, followed by a collection of strings Dna.
//
// Return: The strings BestMotifs resulting from running GibbsSampler(Dna, k, t, N) with 20 random
// starts. Remember to use pseudocounts!
//
// Sample Dataset
// --------------
// 8 5 100
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
import java.util.*;

public class BA2G {

    private static List<String> bestMotifs;
    private static int bestMotifsScore = Integer.MAX_VALUE;

    public static int GibbsRandom(double[] probs) {
        int probsLength = probs.length;
        double C = Arrays.stream(probs).sum(), curProb = 0, prob;
        for (int i = 0; i < probsLength; ++i) {
            probs[i] /= C;
        }
        prob = new Random().nextDouble();

        for (int i = 0; i < probsLength; ++i) {
            curProb += probs[i];
            if (curProb > prob) {
                return i;
            }
        }

        return probsLength - 1;
    }

    private static List<String>
    getEveryFromExceptIndex(List<String> motifs, int motifsSize, int index) {
        List<String> everyExcept = new ArrayList<>();

        for (int i = 0; i < motifsSize; ++i) {
            if (i != index) {
                everyExcept.add(motifs.get(i));
            }
        }

        return everyExcept;
    }

    private static double computeKMerProbability(double[][] profile, String kMer, int k) {
        double prob = 1D;
        int index;

        for (int i = 0; i < k; ++i) {
            index = BA2UTIL.symbolToIndex(kMer.charAt(i));
            prob *= profile[index][i];
        }

        return prob;
    }

    private static String
    getProfileRandomlyGeneratedKMerInSequence(double[][] profile, int k, String sequence) {
        int sequenceLength = sequence.length(), index;
        double[] probs = new double[sequenceLength - k + 1];

        for (int i = 0; i < sequenceLength - k + 1; ++i) {
            probs[i] = computeKMerProbability(profile, sequence.substring(i, i + k), k);
        }
        index = GibbsRandom(probs);

        return sequence.substring(index, index + k);
    }

    private static void
    GibbsSamplerMachineryEach(List<String> DNA, int motifLength, Random rnd, int k, int t, int N) {
        List<String> motifs = BA2UTIL.selectRandomKMers(DNA, motifLength, k);
        List<String> bestMotifs = new ArrayList<>(motifs);
        int index, bestMotifsScore = BA2UTIL.score(bestMotifs), motifsScore;
        double[][] profile;

        for (int j = 0; j < N; ++j) {
            index = rnd.nextInt(t);
            profile = BA2UTIL.formProfileWithPseudocounts(
                    getEveryFromExceptIndex(motifs, t, index));
            motifs.set(index, getProfileRandomlyGeneratedKMerInSequence(profile, k,
                    DNA.get(index)));
            motifsScore = BA2UTIL.score(motifs);

            if (bestMotifsScore > motifsScore) {
                bestMotifsScore = motifsScore;
                bestMotifs = motifs;
            }
        }

        if (BA2G.bestMotifsScore > bestMotifsScore) {
            BA2G.bestMotifsScore = bestMotifsScore;
            BA2G.bestMotifs = bestMotifs;
        }
    }

    private static List<String> GibbsSamplerMachinery(List<String> DNA, int k, int t, int N) {
        int motifLength = DNA.getFirst().length();

        for (int i = 0; i < 20; ++i) {
            GibbsSamplerMachineryEach(DNA, motifLength, new Random(), k, t, N);
        }
        UTIL.writeToFileWithNewlines(BA2G.bestMotifs);

        return BA2G.bestMotifs;
    }

    public static List<String> GibbsSampler(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> intParams = UTIL.parseIntArray(strDataset.getFirst());

        return GibbsSamplerMachinery(strDataset.stream().skip(1).toList(), intParams.getFirst(),
                intParams.get(1), intParams.getLast());
    }

    public static List<String> GibbsSampler(List<String> DNA, int k, int t, int N) {
        return GibbsSamplerMachinery(DNA, k, t, N);
    }
}
