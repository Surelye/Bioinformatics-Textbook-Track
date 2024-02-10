// Compute the Score of a Cyclic Peptide Against a Spectrum
// --------------------------------------------------------
//
// To generalize the Cyclopeptide Sequencing Problem from “Find a Cyclic Peptide with Theoretical
// Spectrum Matching an Ideal Spectrum” to handle noisy spectra, we need to relax the requirement
// that a candidate peptide’s theoretical spectrum must match the experimental spectrum exactly, and
// instead incorporate a scoring function that will select the peptide whose theoretical spectrum
// matches the given experimental spectrum the most closely. Given a cyclic peptide Peptide and a
// spectrum Spectrum, we define Score(Peptide, Spectrum) as the number of masses shared between
// Cyclospectrum(Peptide) and Spectrum. Recalling our example above, if
//
// >Spectrum = {0, 99, 113, 114, 128, 227, 257, 299, 355, 356, 370, 371, 484},
//
// then Score(NQEL, Spectrum) = 11.
//
// The scoring function should take into account the multiplicities of shared masses, i.e., how many
// times they occur in each spectrum. For example, suppose that Spectrum is the theoretical spectrum
// of NQEL; for this spectrum, mass 242 has multiplicity 2. If 242 has multiplicity 1 in the
// theoretical spectrum of Peptide, then 242 contributes 1 to Score(Peptide, Spectrum). If 242 has
// larger multiplicity in the theoretical spectrum of Peptide, then 242 contributes 2 to
// Score(Peptide, Spectrum).
//
// ------------------------------
// Cyclic Peptide Scoring Problem
//
// Compute the score of a cyclic peptide against a spectrum.
//
// Given: An amino acid string Peptide and a collection of integers Spectrum.
//
// Return: The score of Peptide against Spectrum, Score(Peptide, Spectrum).
//
// Sample Dataset
// --------------
// NQEL
// 0 99 113 114 128 227 257 299 355 356 370 371 484
// --------------
//
// Sample Output
// -------------
// 11
// -------------

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA4F {

    private static int computeCyclicPeptideScoreMachinery(String peptide, List<Integer> spectrum) {
        int score = 0, entries;
        List<Integer> cyclospectrum = BA4C.getCyclicSpectrum(peptide);
        Map<Integer, Integer> massToEntryMapping = new HashMap<>();
        for (int mass : spectrum) {
            if (massToEntryMapping.containsKey(mass)) {
                massToEntryMapping.put(mass, massToEntryMapping.get(mass) + 1);
            } else {
                massToEntryMapping.put(mass, 1);
            }
        }

        for (int subpeptideMass : cyclospectrum) {
            if (massToEntryMapping.containsKey(subpeptideMass)) {
                entries = massToEntryMapping.get(subpeptideMass);
                if (entries != 0) {
                    massToEntryMapping.put(subpeptideMass, entries - 1);
                    ++score;
                }
            }
        }

        return score;
    }

    public static int computeCyclicPeptideScore(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return computeCyclicPeptideScoreMachinery(
                strDataset.getFirst(),
                UTIL.parseIntArray(strDataset.getLast())
        );
    }

    public static int computeCyclicPeptideScore(String peptide, List<Integer> spectrum) {
        return computeCyclicPeptideScoreMachinery(peptide, spectrum);
    }
}
