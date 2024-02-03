// Implement GreedyMotifSearch with Pseudocounts
// ---------------------------------------------
//
// We encountered GreedyMotifSearch in “Implement GreedyMotifSearch”. In this problem, we will
// power it up with pseudocounts.
//
// ---------------------------------------------
// Implement GreedyMotifSearch with Pseudocounts
//
// Given: Integers k and t, followed by a collection of strings Dna.
//
// Return: A collection of strings BestMotifs resulting from running GreedyMotifSearch(Dna, k, t)
// with pseudocounts. If at any step you find more than one Profile-most probable k-mer in a given
// string, use the one occurring first.
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
// TTC
// ATC
// TTC
// ATC
// TTC
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA2E {

    private static List<String>
    greedyMotifSearchWithPseudocountsMachinery(List<String> DNA, int k, int t) {
        String fDNA = DNA.getFirst();
        int textLength = fDNA.length();
        List<String> bestMotifs = new ArrayList<>(
                DNA.stream().map(text -> text.substring(0, k)).toList()),
                currentMotifs;
        int bestMotifsScore = BA2UTIL.score(bestMotifs), currentScore;

        for (int i = 0; i < textLength - k + 1; ++i) {
            currentMotifs = new ArrayList<>(List.of(fDNA.substring(i, i + k)));

            for (int j = 1; j < t; ++j) {
                double[][] profile = BA2UTIL.formProfileWithPseudocounts(currentMotifs);
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

    public static List<String> greedyMotifSearchWithPseudocounts(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> intParams = UTIL.parseIntArray(strDataset.getFirst());

        return greedyMotifSearchWithPseudocountsMachinery(strDataset.stream().skip(1).toList(),
                intParams.getFirst(), intParams.getLast());
    }

    public static List<String> greedyMotifSearchWithPseudocounts(List<String> DNA, int k, int t) {
        return greedyMotifSearchWithPseudocountsMachinery(DNA, k, t);
    }

}
