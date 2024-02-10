// Compute the Score of a Linear Peptide
// -------------------------------------
//
// Linear Peptide Scoring Problem
//
// Compute the score of a linear peptide with respect to a spectrum.
//
// Given: An amino acid string Peptide and a collection of integers LinearSpectrum.
//
// Return: The linear score of Peptide against Spectrum, LinearScore(Peptide, Spectrum).
//
// Sample Dataset
// --------------
// NQEL
// 0 99 113 114 128 227 257 299 355 356 370 371 484
// --------------
//
// Sample Output
// -------------
// 8
// -------------

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA4K {

    private static int
    computeLinearPeptideScoreMachinery(String peptide, List<Integer> linearSpectrum) {
        int score = 0, entries;
        List<Integer> peptideLinearSpectrum = BA4J.getLinearSpectrum(peptide);
        Map<Integer, Integer> massToEntry = new HashMap<>();
        for (int mass : linearSpectrum) {
            if (massToEntry.containsKey(mass)) {
                massToEntry.put(mass, massToEntry.get(mass) + 1);
            } else {
                massToEntry.put(mass, 1);
            }
        }

        for (int subpeptideMass : peptideLinearSpectrum) {
            if (massToEntry.containsKey(subpeptideMass)) {
                entries = massToEntry.get(subpeptideMass);
                if (entries != 0) {
                    massToEntry.put(subpeptideMass, entries - 1);
                    ++score;
                }
            }
        }

        return score;
    }

    public static int computeLinearPeptideScore(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return computeLinearPeptideScoreMachinery(
                strDataset.getFirst(),
                UTIL.parseIntArray(strDataset.getLast())
        );
    }

    public static int computeLinearPeptideScore(String peptide, List<Integer> linearSpectrum) {
        return computeLinearPeptideScoreMachinery(peptide, linearSpectrum);
    }
}
