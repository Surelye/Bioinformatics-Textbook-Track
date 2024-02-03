// Implement GreedyMotifSearch
// ---------------------------
//
// ----------------------------
// GREEDYMOTIFSEARCH(Dna, k, t)
//        BestMotifs ← motif matrix formed by first k-mers in each string
//                      from Dna
//        for each k-mer Motif in the first string from Dna
//            Motif1 ← Motif
//            for i = 2 to t
//                form Profile from motifs Motif1, …, Motifi - 1
//                Motifi ← Profile-most probable k-mer in the i-th string
//                          in Dna
//            Motifs ← (Motif1, …, Motift)
//            if Score(Motifs) < Score(BestMotifs)
//                BestMotifs ← Motifs
//        return BestMotifs
// ----------------------------
//
// Implement GreedyMotifSearch
//
// Given: Integers k and t, followed by a collection of strings Dna.
//
// Return: A collection of strings BestMotifs resulting from running GreedyMotifSearch(Dna, k, t).
// If at any step you find more than one Profile-most probable k-mer in a given string, use the one
// occurring first.
//
// Sample Dataset
// --------------
// 3 5
// GGCGTTCAGGCA
// AAGAATCAGTCA
// CAAGGAGTTCGC
// CACGTCAATCAC
// CAATAATATTCG
// --------------
//
// Sample Output
// -------------
// CAG
// CAG
// CAA
// CAA
// CAA
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA2D {

    private static List<String> greedyMotifSearchMachinery(List<String> DNA, int k, int t) {
        String fDNA = DNA.getFirst();
        int textLength = fDNA.length();
        List<String> bestMotifs = new ArrayList<>(
                DNA.stream().map(text -> text.substring(0, k)).toList()),
                currentMotifs;
        int bestMotifsScore = BA2UTIL.score(bestMotifs), currentScore;

        for (int i = 0; i < textLength - k + 1; ++i) {
            currentMotifs = new ArrayList<>(List.of(fDNA.substring(i, i + k)));

            for (int j = 1; j < t; ++j) {
                double[][] profile = BA2UTIL.formProfile(currentMotifs);
                currentMotifs.add(BA2C.profileMostProbableKMer(DNA.get(j), k, profile));
            }
            currentScore = BA2UTIL.score(currentMotifs);

            if (bestMotifsScore > currentScore) {
                bestMotifsScore = currentScore;
                bestMotifs = currentMotifs;
            }
        }
        UTIL.writeToFileWithNewlines(bestMotifs);

        return bestMotifs;
    }

    public static List<String> greedyMotifSearch(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> intParams = UTIL.parseIntArray(strDataset.getFirst());

        return greedyMotifSearchMachinery(strDataset.stream().skip(1).toList(),
                intParams.getFirst(), intParams.getLast());
    }

    public static List<String> greedyMotifSearch(List<String> DNA, int k, int t) {
        return greedyMotifSearchMachinery(DNA, k, t);
    }
}
